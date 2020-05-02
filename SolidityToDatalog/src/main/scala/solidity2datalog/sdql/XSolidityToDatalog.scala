
package solidity2datalog.sdql


import XSolidityDataStructures._
import java.io.FileWriter
//import trap.dsl.DSLConfig
import trap.dsl.AbstractDSLConfig
import trap.dsl.FactDataStructures._
import trap.dsl.FactWriter
import trap.Util._ 

class XSolidityToDatalog(dslConfig:AbstractDSLConfig) { 
  type ParaName = String
  val sNodeMap:Map[Name, Seq[(ParaName, ParaType[_])]] = dslConfig.reducedBasis.groupBy(_.factName).map{
    case (k, v) => 
      val paraDefs:Seq[(String, ParaType[_])] = v.head.paramDefs.map{
        paraDef => (paraDef.paraName, paraDef.paraType)
      } //.toMap
      k -> paraDefs
  }
  type PriKeyName = String
  val sNodePriKeyMap:Map[Name, PriKeyName] = dslConfig.reducedBasis.groupBy(_.factName).map{
    case (k, v) => k -> v.head.paramDefs.head.primaryKey
      // v.head because groupBy returns a list and we need the first element
      // ...paramDefs.head because we are (have to!) ensure that the 
      // primary key is always the first parameter
  }.collect{
    case (k, Some(v)) => k -> v
  }

  
  def getSNodeData(sNode:SNode) = {
    val paraDefs = sNodeMap.get(sNode.nodeName).getOrElse(throw new Exception(s"No such basis pattern: ${sNode.nodeName}"))
    
    val invalidNames = Seq("id", "startChar", "numChars")
    paraDefs.collect{
      case (paraName, paraType) if !invalidNames.contains(paraName)=>
        sNode.attributes.get(paraName) match {
          case Some(attrValue) =>
            try paraType.fromString(attrValue) catch {
              case e:Any => throw new Exception(s"${sNode.nodeName}: $attrValue => $paraType [${e.getMessage}]", e)
            }
          case _ => 
            throw new Exception(s"${sNode.nodeName}: no attribute $paraName")
        }
    }.toArray
    
    //    (values zip paraTypes.drop(3)).map{ // dropping 3 because they represend ID, srcStart, srcChars respectively
    //      case (value, paraType) => 
    //        try paraType.fromString(value) catch {
    //          case e:Any => throw new Exception(s"${sNode.nodeName}: $value => $paraType [${e.getMessage}]", e)
    //        }
    //    }.toArray
  }
  
  def getPriKeyName(sNode:SNode) = sNodePriKeyMap.get(sNode.nodeName).get
  type SNodeFact = (String, List[Any])
  
  def getFacts(sNodes:Seq[SNode]):Seq[SNodeFact] = {
    def getSNodeFact(sNode:SNode, optParent:Option[SNode]):(Long, Seq[SNodeFact]) = {
      val children = sNode.children
      val (numDescendentsSeq, childFactsSeq) = children.map{child =>
        getSNodeFact(child, Some(sNode))
        //getSNodeFact(_, Some(sNode)))
      }.unzip
      val childrenFacts:Seq[SNodeFact] = childFactsSeq.flatten
      val numDescendents = numDescendentsSeq.sum+sNode.children.size

      val thisFacts = Seq[SNodeFact](
        (sNode.nodeName, List(sNode.id, sNode.srcChars, sNode.srcChars) ++ getSNodeData(sNode)),
        ("NumChildren", List(sNode.id, children.size)),
        ("NumDescendents", List(sNode.id, numDescendents))
      )
      val thisFactsString = thisFacts(0)._1+":"+thisFacts(0)._2.reduceLeft(_+","+_)
/*
---- {"Src":"if (!receiver.send(howMany)) t[...]","NODE":"IfStatement"}

----- {"Src":"!receiver.send(howMany)","NODE":"UnaryOperation","prefix":"true","type":"bool","operator":"!"}
------ {"Src":"receiver.send(howMany)","NODE":"FunctionCall","typeconversion":"false","type":"bool"}
------- {"Src":"receiver.send","NODE":"MemberAccess","type":"function (uint256) returns (bool)","membername":"send"}
-------- {"Src":"receiver","NODE":"Identifier","type":"address","value":"receiver"}
------- {"Src":"howMany","NODE":"Identifier","type":"uint256","value":"howMany"}

----- {"Src":"throw","NODE":"Throw"}

---- {"Src":"bar()","NODE":"ExpressionStatement"}
----- {"Src":"bar()","NODE":"FunctionCall","typeconversion":"false","type":"tuple()"}
------ {"Src":"bar","NODE":"Identifier","type":"function ()","value":"bar"}
 */
    
      val predFacts = if (sNode.children.isEmpty) Nil:Seq[SNodeFact] else children.tail.foldLeft(
        (children.head.id, Nil:Seq[SNodeFact])
      )(
        (leftID, right) => {
            (right.id, leftID._2 :+ ("Pred", List(leftID._1, right.id)))
        }
      )._2
      
      val firstLastChildFacts = 
        if (sNode.children.isEmpty) Nil:Seq[SNodeFact] else {
          Seq(
            ("FirstChild", List(sNode.id, children.head.id)),
            ("LastChild", List(sNode.id, children.last.id))
          )
        }
      val srcFacts = Seq(("Src", List(sNode.id, sNode.srcSt, sNode.srcChars, sNode.getSrc)))
      val parentFacts:Seq[SNodeFact] = optParent match {
        case Some(parent) =>
          // we assume that there are 3 primary keys: 
          //  statementID, functionID, contractID 
          // and their parent-child relationship is captured using the following logic extracted from basis. 
          // Note that if Basis changes, then this code might need to change
          // 
          //  ParentChildSS                (parentStatement:Int{statementID}, childStatement:Int{statementID});\
          //	ParentChildFS                (parentFunction:Int{functionID}, childStatement:Int{statementID});\
          //	ParentChildCF                (parentContract:Int{contractID}, childFunction:Int{functionID});\
          //	ParentChildCS                (parentContract:Int{contractID}, childStatement:Int{statementID}) 
          // 
          val factName = (getPriKeyName(parent), getPriKeyName(sNode)) match {
            case ("statementID", "statementID") => "Parent"
            case (p, c) =>
              throw new Exception(
                s"${sNode.nodeName}: Unexpected parent-child pair ($p:${parent.nodeName}, $c: ${sNode.nodeName})"
              )
          }
          Seq((factName, List(parent.id, sNode.id)))
        case _ => 
          Nil
      }
      (numDescendents, thisFacts ++ childrenFacts ++ parentFacts ++ firstLastChildFacts ++ predFacts ++ srcFacts)
    }
    sNodes.flatMap(getSNodeFact(_, None)._2)
  }
  def writeFactFile(facts:Seq[SNodeFact], outFile:String) = {
    using(new FileWriter(outFile)) {fileWriter =>
      val factWriter = new FactWriter(fileWriter, dslConfig.reducedBasis)
      facts.foreach{
        case (factName, factData) =>
        factWriter.fact(factName, factData: _*)
      }
    }
    facts.size
  }
}
