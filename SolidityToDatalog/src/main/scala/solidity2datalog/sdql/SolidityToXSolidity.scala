package solidity2datalog.sdql

import org.ethereum.solidity.compiler.SolidityCompiler
import trap.Util
import scala.collection.JavaConverters._

object SolidityToXSolidity {
  def getXML(solFile:String):xml.Elem = {
    org.sh.utils.Util.trycatch{
      List(
        () => getXMLJS(solFile),
        () => getXMLJava(solFile)
      )
    }
  }
  def getXMLJS(solFile:String):xml.Elem = {
    val (code, out, err) = Util.runCommand(Seq("solc", "--ast-json", solFile))
    // val (code, out, err) = runCommand(Seq("solc", "--combined-json", "metadata", solFile))
    if (code == 0){
      val lines = out.lines 
      val json = lines.iterator().asScala.filterNot(line => line.startsWith("=======") || line.startsWith("JSON AST:")).mkString
      solidity2datalog.util.JSONUtil.jsonStringToXML(json)
    } else throw new Exception(err)
  }  
  def getXMLJava(solFile:String) = {
    val solCode = org.sh.utils.file.Util.readTextFileToString(solFile)
    val x = SolidityCompiler.compile(solCode.getBytes, false, SolidityCompiler.Options.ASTJSON)
    val (out, err) = {
      (x.output, x.errors)
    }
    
    if (err == "") {
      val lines = out.lines 
      val json = lines.iterator().asScala.filterNot(line => line.startsWith("=======") || line.startsWith("JSON AST:")).mkString
      solidity2datalog.util.JSONUtil.jsonStringToXML(json)
    } else throw new Exception(err)
      
  }
}
