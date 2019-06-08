
package sdql

import java.io.{ByteArrayOutputStream, File, PrintStream}

import solidity2datalog.SolidityToDatalog
import solidity2datalog.sdql.XSolidityLoader
import solidity2datalog.util.JSONUtil
import dql.DQLUtil
import org.sh.db.config.DBConfigFromFile
import org.sh.easyweb.Text
import org.sh.utils.common.Util._
import sdql.util.AnalyzerUtil._
import sdql.util.BugPattern
import sdql.util.FileUtil._
import sdql.util.SDQLUtil._
import trap.file.Util._

import scala.collection.JavaConverters._
import scala.util.Random._

object Compiler {
  //val solidityRulesFile = "rules.txt"
  DQLUtil.loadBasis(SolidityToDatalog.solidityBootStrapFile)
  DQLUtil.loadRules("rules.txt")
  DQLUtil.loadMappings("mappings.txt")
  
  def compileSDQLCode(sdqlCode:Text) = {
    val $sdqlCode$ = """
// replace with your own code
    
patterns solidity
find #ContractDefinition
"""
    DQLUtil.compileDQL(sdqlCode.getText) // check for errors first        
    "Compilation successful"
  }
  //  def compileSDQLFile(sdqlFile:File) = {
  //    compileSDQLCode(new Text(readTextFileToString(sdqlFile.getAbsolutePath)))
  //  }

  def compileSolidityCode(solidityCode:Text, printTree:Boolean) = {
    val $info$ = "If printTree is true then it will output AST, else it will output Datalog facts"
    val $solidityCode$ = """
// replace with your solidity code
// this is used to understand how the tool constructs its internal tree
    
pragma solidity ^0.4.0;
    
contract SimpleStorage {
    uint storedData; // State variable
}"""
    val tempFile = tmpDir+"/"+nextInt.abs+".sol"
    writeToTextFile(tempFile, solidityCode.getText)


    Array("Compilation successful") ++ {
      if (printTree) {
        val baos = new ByteArrayOutputStream();
        val ps = new PrintStream(baos);
        val children = XSolidityLoader.getChildren(tempFile)
        children.foreach{child =>
          XSolidityLoader.printTree(child)(ps)
        }
        val output = baos.toString.lines.toArray
        output
      } else compileSolidityFile(new File(tempFile))
    }
  }
  //  def compileSolidityFile(solidityFile:File) = {
  //    SolidityToXSolidity.getXML(solidityFile.getAbsolutePath)   
  //    "Compilation successful"
  //  }
}
import org.sh.db.BetterDB._
import org.sh.db.core.DataStructures._
import org.sh.db.{DBManager => DB}
object AnalyzerMultiTenantUtil {
  val STR = VARCHAR(255)
  val userIDCol = Col("userID", STR)
  val timeCol = Col("time", ULONG)
  
  implicit val config = new DBConfigFromFile("sdql.properties")
  val patternIDCol = Col("patternID", STR)
  
  val userDB = DB("users")(userIDCol, timeCol)(userIDCol)
  
  val userPatternsDB = DB("userPatterns")(userIDCol, patternIDCol)(userIDCol, patternIDCol)
  
  def userGetBugPatterns(userID:String) = {
    userPatternsDB.select(patternIDCol).where(userIDCol === userID).firstAsT[String]
  }
  def userAddBugPatterns(userID:String, patternIDs:Array[String]) = {
    val m = patternIDs.groupBy(x => x)
    m.foreach{
      case (patternID, list) => if (list.size > 1) throw new Exception(s"Duplicate patternID $patternID")
    }
    val toAdd = patternIDs.map {patternID => 
      getAllBugPatterns.find(_.id == patternID) match {
        case Some(pattern) => pattern        
        case _ => throw new Exception(s"No such patternID $patternID") 
      }
    }
    val time = getTime
    toAdd.map{patternID =>
      userPatternsDB.insert(userID, patternID)
    }.sum
  }
  def userAddAllBugPatterns(userID:String) = {
    val time = getTime
    getAllBugPatterns.map{pattern =>
      userPatternsDB.insert(userID, pattern.id)
    }.sum
  }
}
object AnalyzerMultiTenant {
  import AnalyzerMultiTenantUtil._
  def userCreate(userID:String) = if (userDB.exists(userIDCol === userID)) throw new Exception(s"User exists $userID") else {
    userDB.insert(userID, getTime)    
    userAddAllBugPatterns(userID)
  }
  def userDeleteBugPattern(userID:String, patternID:String) = {
    if (userPatternsDB.exists(userIDCol === userID, patternIDCol === patternID)) {
      userPatternsDB.deleteWhere(userIDCol === userID, patternIDCol === patternID)
    } else throw new Exception(s"No such userID-patternID pair ($userID, $patternID)") 
  }
  
  def userGetBugPatterns(userID:String) = { // outputs JSON
    val p = AnalyzerMultiTenantUtil.userGetBugPatterns(userID)
    JSONUtil.createJSONObject(
      Array("userID", "numPatterns", "patterns"), 
      Array(userID, p.size, JSONUtil.encodeJSONSeq(p))
    )
  }
  
  def userDoAnalysis(userID:String, solidityCode:Text) = {
    val $info$ = "Replace below sample code with your own. The sample code contains the bugs we are catching"
    val tempSolidityFile = tmpDir+"/"+nextInt.abs+".sol"
    writeToTextFile(tempSolidityFile, solidityCode.getText)

    val userPatternIDs = AnalyzerMultiTenantUtil.userGetBugPatterns(userID)
    val sdql = getAllBugPatterns.flatMap{a =>
      if (userPatternIDs.contains(a.id)) a.sdql else a.sdql.filterNot(_.startsWith("find"))
    }
    val sdqlCode = (Seq("patterns sdql") ++ sdql).reduceLeft(_+"\n"+_)
    doAnalysisRealtimeJSON(sdqlCode, new File(tempSolidityFile))
  }
  
}

object Analyzer {
//  def getBugPatterns = bugPatterns.flatMap(_.text)
  def getBugPatterns = AnalyzerBackend.getBugPatterns
  def addBugPattern(name:String, description:String, sdql:Text, level:Int) = {
    val $info$ = "Replace with your own values. NOTE: Bug pattern will not be saved across restarts!"
    val $name$ = "Invoke with successors"
    val $description$ = "Invoke statements followed by some code (i.e., invoke is not the last statement)"
    val $sdql$ = """
// finds all invoke statememts having control-flow to any other statement
// (i.e., not the last statement)
    
find #Flow:firstSrc where {@src = #Invoke}"""
    
    val id = "Bug-"+(getAllBugPatterns.size+1)
    val newCode = sdql.getText.lines.iterator().asScala.toSeq
    val newPattern = BugPattern(id, name, description, newCode, level)
    val header = "patterns sdql"
    val olderCode = getAllBugPatterns.flatMap(_.sdql).filterNot{_.startsWith("find")}
    val dslCode = (Seq(header) ++ olderCode ++ newCode).reduceLeft(_+"\n"+_)
    val datalog = DQLUtil.getDatalog(dslCode)
    val rules = DQLUtil.compileDQL(dslCode)          
    bugPatterns = bugPatterns :+ newPattern
    "Ok"
  }
  
  def doAnalysis(solidityCode:Text) = {
    val $info$ = "Replace below sample code with your own. The sample code contains the bugs we are catching"
    val $solidityCode$ = """
    
// replace with your code
pragma solidity ^0.4.0;

contract BadSend {
    uint storedData; // State variable
    function badSend(address receiver, uint howMany, uint abc) {
      if (!receiver.send(howMany)) throw;
      bar(howMany, abc, receiver);              // code after send (Bug #2)
      if (!msg.sender.send(howMany)) throw;     // multiple sends (Bug #1)
    }
    function bar(uint x, uint y, address z) {   // dummy method
       throw;
    }
}

contract ExternalCaller {
    function foo(address ss) {
       BadSend s1 = BadSend(ss);             // existing contract deployment (Bug #3)
    }       
    function bar(address ss) {
       BadSend s2 = new BadSend();           // new contract deployment
    }       
}
contract metaCoin {
    mapping (address => uint) public balances;
    function metaCoin() {
        balances[msg.sender] = 10000;
    }
    function sendToken(address receiver, uint amount) returns(bool successful){
        if (balances[msg.sender] < amount) return false;
        balances[msg.sender] -= amount;
        balances[receiver] += amount;
        return false;
    }
}
contract MetaCoinCaller{
    function sendCoin(address coinContractAddress, address receiver, uint amount){
        metaCoin m = metaCoin(coinContractAddress);  // bad! use of external contract (bug #3)
        if (!m.sendToken(receiver, amount)) throw;              
        address sender = tx.origin;                  // bad! use of tx.origin (bug #4)
        if (!m.sendToken(sender, amount)) throw;
        if (!coinContractAddress.call.value(amount)()) throw; // bad! use of call (bug #5)
    }
}    
    
contract LargeFallback{
    function() payable { // fallback function with heavy processing (Bug #6)
       uint amount = msg.value;
       address coinContractAddress = 0x234;
       address receiver = 0x12;
       if (!msg.sender.send(10000)) throw;
       metaCoin m = metaCoin(coinContractAddress);  // bad! use of external contract (bug #3)
       if (!m.sendToken(receiver, amount)) throw;              
       address sender = tx.origin;                  // bad! use of tx.origin (bug #4)
       if (!m.sendToken(sender, amount)) throw;
       if (!coinContractAddress.call.value(amount)()) throw; // bad! use of call (bug #5)
    }
    
}
    
contract BugInLoop {
    uint a;
    function whileExample() {
        while (msg.sender != 0x123) {
            a += 1; // writes in loop
        }
    }
    function forExample() {
        for (uint p = 0; p < 30; p++) {
            if (!msg.sender.send(100)) throw; // writes in loop
        }
    }
}
"""
    AnalyzerBackend.doAnalysis(solidityCode.getText)
  }
}

object AnalyzerBackend {
  // for connecting to NodeJS or some other front-end
  def doAnalysis(solidityCode:String) = {
    val tempSolidityFile = tmpDir+"/"+nextInt.abs+".sol"
    writeToTextFile(tempSolidityFile, solidityCode)
    
    val sdql = getAllBugPatterns.flatMap{a =>
      if (a.active) a.sdql else a.sdql.filterNot(_.startsWith("find"))
    }
    
//    sdql foreach println
    // val sdqlCode = (Seq("patterns sdql") ++ getActiveBugPatterns.flatMap(_.sdql)).reduceLeft(_+"\n"+_)
    val sdqlCode = (Seq("patterns sdql") ++ sdql).reduceLeft(_+"\n"+_)
    //println(s"sdqlCode:---->\n $sdqlCode\n---->")
    doAnalysisRealtimeJSON(sdqlCode, new File(tempSolidityFile))
    //doAnalysisRealtimeStr(sdqlCode, new File(tempSolidityFile))
  }
  def setActiveBugPatterns(activeStr:String) = setActiveBugs(activeStr)
  
  def getBugPatterns = {
    val problems = getAllBugPatterns.map{
      bugPattern =>
        JSONUtil.createJSONObject(
          Array("bugID", "name", "description", "level", "active"), 
          Array(bugPattern.id, bugPattern.name, bugPattern.description, bugPattern.level, bugPattern.active)
        )
    }
    JSONUtil.createJSONObject(
      Array("numProblems", "problems"), 
      Array(getAllBugPatterns.size, JSONUtil.encodeJSONSeq(problems))
    )
    /*
{“numProblems”:”3”, “problems”:[{“name”:”use of multiple sends”, “description”:”some description”, “level”:2”}, {“name”:”problem 2”, “description”:”some description 2”, “level”:3”}, {“name”:”problem 3”, “description”:”some description 3”, “level”:1”}]}     */
  }
}
/* object AnalyzerRealTime {
  def doAnalysis(sdqlCode:Text, solidityCode:Text) = {
    val $info$ = "replace below codes with your own"
    val $sdqlCode$ = """
// replace with your SDQL code
    
patterns solidity
find #ContractDefinition
"""
    val $solidityCode$ = """
// replace with your solidity code
// this is used to understand how the tool constructs its internal tree
pragma solidity ^0.4.0;
    
contract SimpleStorage {
    uint storedData; // State variable
}"""
    val tempSolidityFile = tmpDir+"/"+nextInt.abs+".sol"
    writeToTextFile(tempSolidityFile, solidityCode.getText)
    doAnalysisRealtime(sdqlCode, new File(tempSolidityFile))
  }
} */
/* 
object AnalyzerTempDir {
  def getTmpDir = DQLUtil.dqlDir
  def clearTempDirs(password:String) = {
    if (password == "0925-924-5924-5092-59134239") {
      deleteRecursive(new File(systemTmp))
      org.sh.utils.common.file.Util.createDir(systemTmp)
      org.sh.utils.common.file.Util.createDir(tmpDir)
      org.sh.utils.common.file.Util.createDir(FileStore.uploadDir)
    } else throw new Exception("Invalid password.")
  }
  def getAllTmpDirs(password:String) = {
    if (password == "0925-924-5924-5092-59134239") {
      getSubDirs(new File(systemTmp))
    } else throw new Exception("Invalid password.")
  }
}
 */
object Config {
  def getBasis:Array[String] = {
    val $info$ = "In the output [T] indicates that pattern is traversible (i.e., returns primary keys)"
    val basisUtil = DQLUtil.optBasisUtil.getOrElse(throw new Exception("No basis defined"))
    val config = DQLUtil.optConfig.getOrElse(throw new Exception("No basis defined"))
    val reduced = config.reducedBasis.map(_.factName)
    val (left, right) = basisUtil.patterns.partition(x => reduced.contains(x.name.tail)) // tail removes the '#' from pattern name

    val leftSize = left.size
    val rightSize = right.size
    
    val baos = new ByteArrayOutputStream();
    val ps = new PrintStream(baos);
    ps.println(s" ======= REDUCED BASIS ($leftSize facts) =======")
    ps.println
    left.foreach{l => ps.println(l.signature)}
    ps.println
    ps.println
    ps.println(s" ======= EXTENDED BASIS ($rightSize facts) =======")
    ps.println
    right.foreach{l => ps.println(l.signature)}
    baos.toString.lines.iterator().asScala.toArray; // e.g. ISO-8859-1
  }  
  def getRules = {
    val ruleFilter = DQLUtil.optRuleFilter.getOrElse(throw new Exception("No rules defined"))
    ruleFilter.rules
  }
  def getMappings = {
    val mappings = DQLUtil.optMappings.getOrElse(throw new Exception("No mappings defined"))
    mappings.loadedMappings.map(_.getCode)
  }
   
  def setMappings(mappings:Text) = {
    val $mappings$ = """mappings sdql

map :invoke as $statementID => #Invoke;
map :src as $statementID => #Src;
map :text as $statementID => #Src.@text, $statementID => #Src.@text;
map :bothSrc as $statementID => #Src, $statementID => #Src;
map :firstSrc as $statementID => #Src, $statementID => ; 
map :function as $statementID => #FunctionDefinition.@startChar,@numChars,@name;

"""
    uploadNewMappings(mappings) // some implicit conversion from Text to File happening here
  }
  def setBasisAndRules(basis:Text, rules:Text):String = {
    val $basis$ = """reducedBasis = \
  ContractDefinition           (id:Int[statementID], startChar:Int, numChars:Int, fullyImplemented:Boolean, linearizedBaseContracts:Int, isLibrary:Boolean, name:String);\
  Return                       (id:Int[statementID], startChar:Int, numChars:Int);\
  FunctionDefinition           (id:Int[statementID], startChar:Int, numChars:Int, constant:Boolean, payable:Boolean, visibility:String, name:String);\
  IfStatement                  (id:Int[statementID], startChar:Int, numChars:Int);\
  UserDefinedTypeName          (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  FunctionCall                 (id:Int[statementID], startChar:Int, numChars:Int, typeconversion:Boolean, type:String);\
  IndexAccess                  (id:Int[statementID], startChar:Int, numChars:Int, type:String);\
  Literal                      (id:Int[statementID], startChar:Int, numChars:Int, hexvalue:String, subdenomination:String, token:String, type:String, value:String);\
  UnaryOperation               (id:Int[statementID], startChar:Int, numChars:Int, prefix:Boolean, type:String, operator:String);\
  Mapping                      (id:Int[statementID], startChar:Int, numChars:Int);\
  WhileStatement               (id:Int[statementID], startChar:Int, numChars:Int);\
  StructDefinition             (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  ParameterList                (id:Int[statementID], startChar:Int, numChars:Int);\
  Break                        (id:Int[statementID], startChar:Int, numChars:Int);\
  PragmaDirective            ! (id:Int[statementID], startChar:Int, numChars:Int, literals:String);\
  VariableDeclaration          (id:Int[statementID], startChar:Int, numChars:Int, name:String, type:String);\
  ElementaryTypeName           (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  PlaceholderStatement         (id:Int[statementID], startChar:Int, numChars:Int);\
  ForStatement                 (id:Int[statementID], startChar:Int, numChars:Int);\
  Identifier                   (id:Int[statementID], startChar:Int, numChars:Int, type:String, value:String);\
  ModifierDefinition           (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  Assignment                   (id:Int[statementID], startChar:Int, numChars:Int, type:String, operator:String);\
  DoWhileStatement             (id:Int[statementID], startChar:Int, numChars:Int);\
  ModifierInvocation           (id:Int[statementID], startChar:Int, numChars:Int);\
  Block                        (id:Int[statementID], startChar:Int, numChars:Int);\
  EventDefinition              (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  BinaryOperation              (id:Int[statementID], startChar:Int, numChars:Int, type:String, operator:String);\
  Continue                     (id:Int[statementID], startChar:Int, numChars:Int);\
  Throw                        (id:Int[statementID], startChar:Int, numChars:Int);\
  ExpressionStatement          (id:Int[statementID], startChar:Int, numChars:Int);\
  MemberAccess                 (id:Int[statementID], startChar:Int, numChars:Int, membername:String, type:String);\
  VariableDeclarationStatement (id:Int[statementID], startChar:Int, numChars:Int);\
  UsingForDirective            (id:Int[statementID], startChar:Int, numChars:Int);\
  ElementaryTypeNameExpression (id:Int[statementID], startChar:Int, numChars:Int, type:String, value:String);\
  ArrayTypeName                (id:Int[statementID], startChar:Int, numChars:Int);\
  InheritanceSpecifier         (id:Int[statementID], startChar:Int, numChars:Int);\
  TupleExpression              (id:Int[statementID], startChar:Int, numChars:Int);\
  NewExpression                (id:Int[statementID], startChar:Int, numChars:Int, type:String);\
  EnumDefinition               (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  EnumValue                    (id:Int[statementID], startChar:Int, numChars:Int, name:String);\
  Parent                       (parent:Int{statementID}, child:Int{statementID});\
  Pred                         (pred:Int{statementID}, succ:Int{statementID});\
  LastChild                    (parent:Int{statementID}, child:Int{statementID});\
  FirstChild                   (parent:Int{statementID}, child:Int{statementID});\
  Src                          (id:Int[statementID], startChar:Int, numChars:Int, text:String);\
  NumChildren                  (id:Int[statementID], value:Int);\
  NumDescendents               (id:Int[statementID], value:Int);\

extendedBasis = \
  Flow                         (src:Int{statementID}, dest:Int{statementID});\
  Nop                          (id :Int[statementID]);\
  FlowAcross                   (src:Int{statementID}, dest:Int{statementID});\
  FunctionCode                 (funcID:Int[functionID], id:Int[statementID]);\
  SecondChild                  (parent:Int{statementID}, child:Int{statementID});\
  FuncParamDef                 (stmt:Int{statementID}, params:Int{statementID});\
  FuncRetDef                   (stmt:Int{statementID}, returns:Int{statementID});\
  FuncBody                     (stmt:Int{statementID}, body:Int{statementID});\
  Func                         (funcID:Int[functionID], id:Int[statementID]);\
  Ancestor                     (parent:Int{statementID}, child:Int{statementID});\
  Invoke                       (id :Int[statementID], objType:String, instanceName:String, methodName:String, retType:String);\
  Assign                       (id :Int[statementID], variableName:String, variableType:String, rhsType:String, rhsValue:String);\
  InLoop                       (stmt:Int{statementID});\
"""
    val $rules$ = """
// declarations
Declaration(?x, ?variableName, ?userDefinedType, ?variableType) :- 
              FunctionCode(?f, ?x), 
              VariableDeclarationStatement(?x, ?startChar1, ?numChar1), 
              VariableDeclaration(?y, ?startChar2, ?numChar2, ?variableName, ?variableType),
              UserDefinedTypeName(?z, ?startChar3, ?numChar3, ?userDefinedType),
              Parent(?x, ?y).

Declaration(?x, ?variableName, ?elementaryType, ?variableType) :- 
              FunctionCode(?f, ?x), 
              VariableDeclarationStatement(?x, ?startChar1, ?numChar1), 
              VariableDeclaration(?y, ?startChar2, ?numChar2, ?variableName, ?variableType),
              ElementaryTypeName(?z, ?startChar3, ?numChar3, ?elementaryType),
              Parent(?x, ?y).

Assign(?x, ?variableName, ?elementaryType, ?literalType, ?literalValue) :- 
              Declaration(?x, ?variableName, ?elementaryType, ?variableType), 
              Literal(?y, ?startChar1, ?numChar1, ?hex, ?subden, ?token, ?literalType, ?literalValue),
              Parent(?x, ?y).
              
Assign(?x, ?identifierValue, ?variableType, ?identifierType, ?identifierValue) :-
              FunctionCode(?f, ?x), 
              Parent(?x, ?y),
              Assignment(?y, ?startChar1, ?numChar1, ?variableType, ?operator),
              Identifier(?z, ?startChar2, ?numChar2, ?identifierType, ?identifierValue),
              Parent(?y, ?z).
              
Nop(?x) :- ExpressionStatement(?x, ?startChar, ?numChar).
Nop(?x) :- Identifier(?x, ?startChar, ?numChars, ?type, ?value).
Nop(?x) :- Literal(?x, ?startChar, ?numChars, ?hexvalue, ?subdenomination, ?token, ?type, ?value).
Nop(?x) :- BinaryOperation(?x, ?startChar, ?numChar, ?type, ?operator).
Nop(?x) :- UnaryOperation(?x, ?startChar, ?numChar, ?prefix, ?type, ?operator).
Nop(?x) :- Block(?x, ?startChar, ?numChar).
Nop(?x) :- UserDefinedTypeName(?x, ?startChar, ?numChar, ?name).
Nop(?x) :- IfStatement(?x, ?startChar, ?numChar).
Nop(?x) :- WhileStatement(?x, ?startChar, ?numChar).
Nop(?x) :- DoWhileStatement(?x, ?startChar, ?numChar).
Nop(?x) :- MemberAccess(?x, ?startChar, ?numChars, ?membername, ?type).
Nop(?x) :- BreakStmt(?x).

// loops

FirstChildAncestor(?ancestor, ?descendent) :- Ancestor(?x, ?descendent), FirstChild(?ancestor, ?x).

InLoop(?x) :- WhileStatement(?y, ?startChar, ?numChars), Ancestor(?y, ?x).
InLoop(?x) :- ForStatement(?y, ?startChar, ?numChars), Ancestor(?y, ?x), ! FirstChildAncestor(?y, ?x).

BreakStmt(?x) :- Throw(?x, ?startChar, ?numChars).   
BreakStmt(?x) :- Return(?x, ?startChar, ?numChars).   
BreakStmt(?x) :- Break(?x, ?startChar, ?numChars).   
BreakStmt(?x) :- Continue(?x, ?startChar, ?numChars).   

Flow(?x, ?y) :- FunctionCode(?z, ?x), FunctionCode(?z, ?y),
              Ancestor(?ax, ?x),
              Pred(?ax, ?y), 
              Ancestor(?x, ?someStmt), 
              !BreakStmt(?someStmt).

// there is flow from x to y if: 
//     both x and y are inside the same function's code for some function z, and
//     some x's ancestor ax is a predecessor of some y's ancestor ay, and
//     those two ancestors ax and ay are descendents of function z, and
//     x is not the ancestor of a BreakStmt statement
// or ... (see below)

Flow(?x, ?y) :- FunctionCode(?f, ?x), FunctionCode(?f, ?y), Parent(?x, ?y).
Flow(?x, ?y) :- Flow(?x, ?z), Flow(?z, ?y).
//     x is a parent of y and both are in the same function's code

FlowAcross(?x, ?y) :- Invoke(?x, ?objType, ?instanceName, ?methodName, ?retType), 
                      FunctionDefinition(?z, ?startChar1, ?numChars1, ?constant, ?payable, ?visibility, ?methodName),
                      Block(?y, ?startChar2, ?numChars2),
                      Parent(?z, ?y).                     
// from one method to another
                    
FunctionCode(?z, ?y) :- Ancestor(?x, ?y), 
                     FunctionDefinition(?z, ?startChar1, ?numChars1, ?constant, ?payable, ?visibility, ?name),
                     Block(?x, ?startChar2, ?numChars2),
                     Parent(?z, ?x).                     
// y is code inside function z if:
//    y is inside a 'Block' node x that is the child of a 'FunctionDefinition' node z


Ancestor(?x, ?y) :- Ancestor(?x, ?z), Ancestor(?z, ?y).

Ancestor(?x, ?y) :- Parent(?x, ?y).

SecondChild(?x, ?y) :- FirstChild(?x, ?z), Pred(?z, ?y).

FuncParamDef(?x, ?y) :- FirstChild(?x, ?y), FunctionDefinition(?x, ?startChar, ?numChars, ?constant, ?payable, ?visibility, ?name).

FuncRetDef(?x, ?y) :- FuncParamDef(?x, ?z), Pred(?z, ?y).

FuncBody(?x, ?y) :- FuncRetDef(?x, ?z), Pred(?z, ?y).

Func(?x, ?x) :- FuncParamDef(?x, ?y).

Invoke(?statementID, ?objType, ?instanceName, ?methodName, ?retType) :- 
       FunctionCall(?statementID, ?startChar1, ?numChars1, ?typeConversion, ?retType), 
       MemberAccess(?subStatementID, ?startChar2, ?numChars2, ?methodName, ?type1), 
       FirstChild(?statementID, ?subStatementID),  
       MemberAccess(?subSubStatementID, ?startChar3, ?numChars3, ?instanceName, ?objType),
       FirstChild(?subStatementID, ?subSubStatementID).
       
Invoke(?statementID, ?objType, ?instanceName, ?methodName, ?retType) :-        
       FunctionCall(?statementID, ?startChar1, ?numChars1, ?typeConversion, ?retType), 
       MemberAccess(?subStatementID, ?startChar2, ?numChars2, ?methodName, ?type1), 
       FirstChild(?statementID, ?subStatementID),  
       Identifier(?subSubStatementID, ?startChar3, ?numChars3, ?objType, ?instanceName),
       Parent(?subStatementID, ?subSubStatementID).
       
Invoke(?statementID, 'unknown', 'this', ?methodName, ?retType) :-        
       FunctionCall(?statementID, ?startChar1, ?numChars1, ?typeConversion, ?retType), 
       Identifier(?subStatementID, ?startChar2, ?numChars2, ?idType, ?methodName), 
       FirstChild(?statementID, ?subStatementID).
       
Invoke(?statementID, 'unknown', 'this', '<constructor>', ?retType) :-        
       FunctionCall(?statementID, ?startChar1, ?numChars1, ?typeConversion, ?retType), 
       NewExpression(?subStatementID, ?startChar2, ?numChars2, ?idType), 
       FirstChild(?statementID, ?subStatementID)."""
    uploadNewConfig(basis, rules)
  }
 
}
