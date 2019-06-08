
package solidity2datalog

//import solidity2datalog.sdql.SNodeFact
import solidity2datalog.sdql.XSolidityDataStructures._
import solidity2datalog.sdql._
import trap.dsl.DSLConfig
import trap.file.Util

object SolidityToDatalog {
  import XSolidityLoader._
  val defaultBootStrapFile = "basis.txt"
  var solidityBootStrapFile = defaultBootStrapFile
  
  def main(a:Array[String]):Unit = {
    if (a.size != 2) {
      println("usage [compile mode] java -jar dist/SolidityToDatalog.jar <solidity_file> <output_datalog_file>")
      println("   or [print atoms]   java -jar dist/SolidityToDatalog.jar <solidity_file> -atoms")      
      println("   or [print AST]     java -jar dist/SolidityToDatalog.jar <solidity_file> -ast")      
      println("   or [print facts]   java -jar dist/SolidityToDatalog.jar <solidity_file> -facts")      
    } else {
    
      val outFile = a(1)
      val srcFile = a(0)
      outFile match {
        case "-atoms" =>
          val children = getChildren(srcFile) // "/home/amitabh/Ballot.sol"
          getCodeAtoms(children) foreach println
        case "-ast" =>
          val children = getChildren(srcFile) // "/home/amitabh/Ballot.sol"
          children.foreach(printTree)
        case "-facts" =>
          if (!Util.fileExists(srcFile)) throw new Exception("srcFile does not exist")
          val dslConfig = new DSLConfig(solidityBootStrapFile)
          val xSolidityCompiler = new XSolidityToDatalog(dslConfig)
          val sNodes:Seq[SNode] = getChildren(srcFile)
          val facts = xSolidityCompiler.getFacts(sNodes)
          facts foreach println
        case _ => 
          val numFacts = generateDatalog(srcFile, outFile)
          println(s"$numFacts facts written to $outFile")
      }
    }
  }
  def generateDatalog(solidityFile:String, outputDatalogFile:String) = {
    if (Util.fileExists(outputDatalogFile)) throw new Exception("outputFile already exists")
    val (xSolidityCompiler, facts) = generateDatalogFacts(solidityFile)
    xSolidityCompiler.writeFactFile(facts, outputDatalogFile)
  }
  type SNodeFact = (String, List[Any])

  def generateDatalogFacts(solidityFile:String) = {
    if (!Util.fileExists(solidityFile)) throw new Exception("srcFile does not exist")
    val dslConfig = new DSLConfig(solidityBootStrapFile)
    val xSolidityCompiler = new XSolidityToDatalog(dslConfig)
    val sNodes:Seq[SNode] = getChildren(solidityFile)
    (xSolidityCompiler, xSolidityCompiler.getFacts(sNodes):Seq[SNodeFact])
  }
  
}
