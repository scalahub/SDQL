package sdql.util

import trap.file.TraitFilePropertyReader

case class BugPattern(id:String, name:String, description:String, sdql:Seq[String], level:Int) {
  var active = true
  def text = Seq(
    "=============== Bug START ===============",
    s"ID: $id",
    s"Name: $name",
    s"Description: $description",
    s"  [detector code below]"
  ) ++ sdql ++Seq(
    "=============== Bug END ===============",
    ""
  )
}


/* 
object AnalyzerBackground {
  def doAnalysis(sdqlCode:Text, solidityCode:Text) = {
    val $sdqlCode$ = """
patterns solidity
find #ContractDefinition
"""
    val $solidityCode$ = """pragma solidity ^0.4.0;
    
contract SimpleStorage {
    uint storedData; // State variable
}"""
    val tempSDQLFile = tmpDir+"/"+nextInt.abs+".jdql"
    val tempSolidityFile = tmpDir+"/"+nextInt.abs+".sol"
    writeToTextFile(tempSDQLFile, sdqlCode.getText)
    writeToTextFile(tempSolidityFile, solidityCode.getText)
    doAnalysisBackground(new File(tempSDQLFile), new File(tempSolidityFile))
  }
  def getAnalysisResult(analysisID:String) = {
    val $info$ = """If analysis is complete, this will return the result in a text file"""
    val tmpWorkDir = tmpDir + "/" +analysisID
    val tmpWork = new File(tmpWorkDir)
    if (!tmpWork.exists) throw new Exception("analysis not started for analysisID: "+analysisID)

    val resultFile = tmpWorkDir + "/result.txt"
    val result = new File(resultFile)
    val completeFile = tmpWorkDir + "/complete.txt"
    val complete = new File(completeFile)
    if (complete.exists && result.exists) result else throw new Exception("result not yet available for analysisID: "+analysisID+". Please wait some more.")
  }
  //  def removeAnalysis(analysisID:String, reallyDelete:Boolean) = if (reallyDelete) {
  //    val tmpWorkDir = tmpDir + "/" +analysisID
  //    val tmpWork = new File(tmpWorkDir)
  //    if (!tmpWork.exists) throw new Exception("analysis not started for analysisID: "+analysisID)
  //    val completeFile = tmpWorkDir + "/complete.txt"
  //    val complete = new File(completeFile)
  //    if (!complete.exists) throw new Exception("analysis not completed for analysisID: "+analysisID)
  //    deleteRecursive(tmpWork)
  //  } else throw new Exception("reallyDelete must be true")
}
 */
object AnalyzerUtil extends TraitFilePropertyReader {
  val propertyFile = "bugs.properties"

  val defaultBugPatterns = Seq(
    BugPattern(
      "Bug-1",
      "multiple sends", "Multiple sends in a single method", 
      Seq(
        "def #send as #Invoke where {(@methodName = 'send' or @methodName = 'call') and @objType = 'address'}",
        "def #multipleSends as #Flow where {@src = #send and @dest = #send}",
        // "find #multipleSends:text",        
        // "find #multipleSends:bothSrc", 
        "find #multipleSends:firstSrc"        
      ), 
      1
    ),
    BugPattern(
      "Bug-2",
      "code after send", "There should not be any methods calls or state changes after a send", 
      Seq(
        "def #nop as #Flow where {@src = #send and @dest = #Nop}",
        "def #all as #Flow where {@src = #send}",
        "def #codeAfterSend as {#all not #nop}",
        //"find #codeAfterSend:bothSrc",
        "find #codeAfterSend:firstSrc"
      ), 
      2
    ),
    BugPattern(
      "Bug-3",
      "extrnal contract call", "There should not be any external contract calls", 
      Seq(
        // "find #Invoke:invoke where {@retType ~ 'contract *'}"
        "find #Invoke:src where {@retType ~ 'contract *'}"
      ),
      3
    ),
    BugPattern(
      "Bug-4",
      "use of tx.origin", "Use msg.sender instead of tx.origin", 
      Seq(
        "def #origin as #MemberAccess where {@membername = 'origin'}",
        "def #tx as #Identifier where {@type = 'tx'}",
        "find #Parent:firstSrc where {@parent = #origin and @child = #tx}"
      ), 
      1
    ),
    BugPattern(
      "Bug-5",
      "use of address.call", "Use address.send instead of address.call", 
      Seq(
        "def #call as #MemberAccess where {@membername = 'call'}",
        "def #address as #Identifier where {@type = 'address'}",
        "find #Parent:firstSrc where {@parent = #call and @child = #address}"
      ), 
      3
    ),
    BugPattern(
      "Bug-6",
      "large amount of code in fallback function", "Do not have large code in fallback function", 
      Seq(
        "def #fallback as #FunctionDefinition where {@name = ''}",
        "def #heavyChild as #NumDescendents where {@value > 5}",
        "find #Ancestor:firstSrc where {@parent = #fallback and @child = #heavyChild}"  //        "def #bug as #heavyFallback heavyFallback as #Ancestor where {@parent = #fallback and @child = #heavyChild}"
      ), 
      2
    ),
    BugPattern(
      "Bug-7",
      "writing to blockchain in a loop", "Do not write to blockchain in a loop. Write is call/send or assignment", 
      Seq(
        "def #write as {#Assign or #send}",
        "find #InLoop:src where {@stmt = #write}"
      ), 
      1
    )
  )
  
  private[sdql] var bugPatterns:Seq[BugPattern] = defaultBugPatterns

  def getActiveBugPatterns = bugPatterns.filter(_.active)
  def getAllBugPatterns = bugPatterns
  ///////////////////////////////////////
  var activeBugsStr = read("activeBugs", "1"*bugPatterns.size)
  
  private def getBugString = bugPatterns.map(_.active).map{
    case true => "1"
    case false => "0"
  }.mkString

  private def writeActiveBugs = write("activeBugs", getBugString, "active bug patterns (will be scanned)")

  def setActiveBugs(activeStr:String) = {
    
//    println (" ----------------------------> 1")
    val active = activeStr.toCharArray.map{
      case '1' => true
      case '0' => false
      case x => throw new Exception(s"Invalid character $x. Should be 1 or 0")
    }
//    println (" ----------------------------> 2")
    if (active.size != bugPatterns.size) {
      throw new Exception(s"Total bug patterns (${bugPatterns.size}) != Number of Inputs (${active.size})")
    }
//    println (" ----------------------------> 3")
    
    bugPatterns zip active foreach{
      case (b, a) => b.active = a
    }    
//    println (" ----------------------------> 4")
    
    writeActiveBugs
//    println (" ----------------------------> 5")
    
    "Ok"
  }
  
  setActiveBugs(activeBugsStr)
//  println (" ----------------------------> 6 --> "+activeBugsStr)
  
}





//    ,
//    BugPattern(
//      "Bug-7",
//      "large amount of code in fallback function", "Do not have large code in fallback function", 
//      Seq(
////        "def #fallback as #FunctionDefinition where {@name = ''}",
////        "def #heavyChild as #NumDescendents where {@value > 5}",
//        "def #heavyFallback as #Ancestor where {@parent = #fallback and @child = #heavyChild}",
//        "def #bug as {#heavyFallback and #FuncBody}",
//        "find #bug:firstSrc "
//        
////        "def #bug as #heavyFallback heavyFallback as #Ancestor where {@parent = #fallback and @child = #heavyChild}"
//      )
//    )