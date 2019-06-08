package solidity2datalog.sdql

import solidity2datalog.util.JSONUtil.JsonFormatted

object XSolidityDataStructures {
  
  // SNode is short for "Solidity Node"; represents a piece of code
  
  type Key = String
  type Value = String
  type Name = String
  type Attributes = Map[Key, Value]
  type Keys = Seq[Key]
  type Children = Seq[SNode]
  
  case class SNode(srcSt:Int, srcChars:Int, nodeName:Name, id:Long, attributes:Attributes, children:Children)(
    implicit src:Option[String], showDetails:Boolean 
  ) extends JsonFormatted {
    val detailKeys = if (showDetails) Seq("id", "startChar", "numChars") else Nil
    val detailVals = if (showDetails) Seq[Any](id, srcSt, srcChars) else Nil
    val keys = Array("NODE") ++ attributes.keys ++ detailKeys ++ (if (src.isDefined) Seq("Src") else Nil)
    val vals = Array[Any](nodeName) ++ attributes.values ++ detailVals ++ (if (src.isDefined) Seq(getSrc) else Nil)
    
    def getSrc = {
      if (srcChars > 30) 
        src.get.drop(srcSt).take(30) + "[...]" 
      else 
        src.get.drop(srcSt).take(srcChars)
    }.replace("\n", " ")
  }
  
  case class NameAttrs(nodeName:Name, attrNames:Keys) extends JsonFormatted{
    val keys = Array("name") ++ (if (attrNames.size > 0) Seq("attributes") else Nil)
    val vals = Array[Any](nodeName) ++ (if (attrNames.size > 0) Seq(attrNames.reduceLeft(_ +","+_)) else Nil)
  }
}
