package acc.sdql

import XSolidityDataStructures._

import java.io.OutputStream
import java.io.PrintStream
import scala.xml.Elem
import scala.xml.Node

object XSolidityLoader {
  // X implies that the Solidity code is first converted to XML. We call this XML as XSolidity. This object loads XSolidity into Scala objects
  
  def getChildren(srcFile:String)(implicit showDetails:Boolean = true):Seq[SNode] = {
    implicit val text = Some(trap.file.Util.readTextFileToString(srcFile))
    getChildren(SolidityToXSolidity.getXML(srcFile))
  }
  def getChildren(node:Elem)(
    implicit Src:Option[String], showDetails:Boolean
  ):Seq[SNode] = {
    (node \ "children").map(getChild)
  }

  private def getChild(node:Node)(implicit srcTxt:Option[String], showDetails:Boolean):SNode = {
    val src = (node \ "src").text.split(":")
    val srcSt = src(0).toInt
    val srcEnd = src(1).toInt
    val name = (node \ "name").text
    val id = (node \ "id").text.toLong
    val attributes = (node \ "attributes").flatMap(getAttribute).toMap
    val children = (node \ "children").map(getChild)
    SNode(srcSt:Int, srcEnd:Int, name:String, id:Long, attributes:Map[Key, Value], children:Seq[SNode])
  }
  private def getAttribute(node:Node) = {
    (node \ "_").map{x =>
      trap.xml.Util.getElementName(x).replace("_", "") -> x.text
    }
  }

  def printTree(node:SNode)(implicit out:PrintStream = System.out) = printSubTree(node, 1)
  
  private def printSubTree(node:SNode, level:Int)(implicit out:PrintStream):Unit = {
    val levStr = "-" * level
    out.println(levStr+" "+node)
    node.children.foreach(printSubTree(_, level+1))    
  }

  private def getNameDetails(sNode:SNode):Seq[NameAttrs] = {
    Seq(NameAttrs(sNode.nodeName, sNode.attributes.keys.toSeq)) ++ sNode.children.map(getNameDetails).flatten
  }
  
  def getCodeAtoms(sNodes:Seq[SNode]) = {
    val nameDetails = sNodes.flatMap(getNameDetails).groupBy(_.nodeName).map{
      case (name, list) => name -> list.map(_.attrNames)
    }
    nameDetails.map{
      case (name, keySeq) => 
        val allEqualSize = keySeq.forall{x => 
          x.size == keySeq(0).size
        }
        if (!allEqualSize) throw new Exception(s"Name: $name has non-consistent attributes")
        val allEqualValues = keySeq.forall{x => 
          x zip keySeq(0) forall{case (a, b) => a == b}
        }
        NameAttrs(name, keySeq(0))
    }
  }
}
