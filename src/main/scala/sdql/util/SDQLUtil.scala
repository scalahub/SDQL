package sdql.util

import java.io.File
import solidity2datalog.SolidityToDatalog
import solidity2datalog.util.JSONUtil
import dql.DQLUtil
import dql.DQLUtil._
import org.sh.easyweb.Text
import sdql.util.AnalyzerUtil._
import sdql.util.FileUtil._
import trap.CryptoUtil
import trap.dsl.DSLToXDSL
import trap.file.Util._

object SDQLUtil {
  def compileSolidityFile(solidityFile:File) = {
    val solidityPath = solidityFile.getAbsolutePath
    val outputDatalogFile = tmpDir + "/"+solidityFile.getName+org.sh.utils.Util.randomAlphanumericString(10)+".facts"
    if (fileExists(outputDatalogFile)) throw new Exception("outputFile already exists")
    val (xSolidityCompiler, facts) = SolidityToDatalog.generateDatalogFacts(solidityPath)
    xSolidityCompiler.writeFactFile(facts, outputDatalogFile)
    readTextFileToString(outputDatalogFile).lines.toArray
    //    "Compilation successful"
  }
  
  def doAnalysisRealtimeText(dslCode:Text, solidityFile:File):Seq[String] = {
    doAnalysisRealtimeStr(dslCode.getText, solidityFile)
  }
  def doAnalysisRealtimeStr(dslCode:String, solidityFile:File) = {
    doAnalysisRealtime(dslCode, solidityFile) match {
      case Left(a) =>
        DQLUtil.convertResultsToStrings(a)
      case Right(t) => throw t
    }
  }


//  def convertResultsToJSON(countsRowsComments:Seq[Seq[((Count, Rows), Comment)]]) = {
//    countsRowsComments.flatMap{
//      case countRowComment:Seq[((Int, Seq[String]), String)] =>
//        countRowComment.flatMap{
//          case ((count:Int, rows:Seq[String]), comment:String) =>
//            val result = if (count > 10) {
//              rows.take(10) ++ Seq("...", s"[showing top 10 of $count rows]", "") 
//            } else rows ++ Seq(s"[$count rows]", "")
//
//            // Seq(comment)++Seq("// Rows returned: "+count)++result++Seq("// ----------------------")
//            Seq(comment)++result // ++Seq("// ----------------------")
//        }
//    }
//  }
//  

  def doAnalysisRealtimeJSON(dslCode:String, solidityFile:File) = {
    val str = readTextFileToString(solidityFile.getAbsolutePath)
    var stCtr = 0
    val lineMap = str.split("\n").zipWithIndex.map{
      case (line, num) => 
        val oldSt = stCtr
        val endCtr = stCtr + line.size 
        stCtr = endCtr + 1 // one for \n  
        num -> (oldSt, endCtr)        
    }.toMap

    def getLineCol(stChar:Int) = {
      val (line, (st, end)) = lineMap.find{
        case (line, (st, end)) => st <= stChar && end >= stChar
      }.get 
      (line, stChar - st)
    }
    doAnalysisRealtime(dslCode, solidityFile) match {
      case Left(result:Seq[Seq[CountRowsComment]]) =>
        // in above:
        //             CountRowsComment   // one CountRowsComment for each mapping inside a find query      
        //         Seq[CountRowsComment]  // one Seq[CountRowsComment] for each find query
        //     Seq[Seq[CountRowsComment]] // one Seq[Seq[CountRowsComment]] for all find queries

        // we can assume that in SDQL, each find query has ONE mapping
        // and that each mapping outputs 3 entries: (startChar, numChars, codeText)
        // thus, we can flatten the data and map directly to bugs
        // (because one bug maps to one find query)

        val resultFlattened = result.flatten
        // sanity check below
        if (resultFlattened.size != getActiveBugPatterns.size) {
          // check for error 
          throw new Exception(s"Error parsing results: resultFlattened.size (${resultFlattened.size}) != bugPatterns.size (${getActiveBugPatterns.size})")
        }

        val foundBugs = getActiveBugPatterns zip resultFlattened collect {
          case (bugPattern, ((count, rows), comment)) if rows.nonEmpty => (bugPattern, count, rows)
        }

        val bugArray = foundBugs.map {
          case (bugPattern, count, rows) => 
            val bugs = rows.map {row =>
              // temp46(190,22,'receiver.send(howMany)').
              val ar = { row.split("\\(", 2)(1).init.init.split(",", 3) }
              val stChar = ar(0)
              val (lineNum, charNum) = getLineCol(stChar.toInt)
              val bug = JSONUtil.createJSONObject(
                Array("startCharacter", "numCharacters", "text", "lineNum", "charNum"),
                Array(stChar, ar(1), ar(2).init.tail, lineNum+1, charNum+1)
              )
              bug
            }

            JSONUtil.createJSONObject(
              Array(
                "bugID", "name", "description", "numBugs", "bugs", "level"
              ), 
              Array(
                bugPattern.id, bugPattern.name, bugPattern.description, count, JSONUtil.encodeJSONSeq(bugs), bugPattern.level
              )
            )
        }
        JSONUtil.createJSONObject(
          Array("numProblems", "problems"), 
          Array(foundBugs.size, JSONUtil.encodeJSONSeq(bugArray))
        )

        /*
    { "numProblems":4,
      "problems":[
             { 
               "bugID": "Bug-01", "name":"multiple sends", "description":"do not use multiple sends", "numBugs":"3",
               "bugs":[
                            {
                              "startCharacter":"120",
                              "numCharacters":"13",
                              "text":"msg.sender.send(111)"
                            },
                            {
                              "startCharacter":"240",
                              "numCharacters":"42",
                              "text":"if (!msg.sender.send(111)) throw"
                            },
                            {
                              "startCharacter":"413",
                              "numCharacters":"14",
                              "text":"msg.sender.send(1111)"
                            }		
                    ]
             },

             { 
               "bugID": "Bug-02", "name":"code after send", "description":"do not have code after send", "numBugs":"2",
               "bugs":[
                            {
                              "startCharacter":"120",
                              "numCharacters":"13",
                              "text":"msg.sender.send(111)"
                            },
                            {
                              "startCharacter":"240",
                              "numCharacters":"42",
                              "text":"if (!msg.sender.send(111)) throw"
                            }		
                    ]
             },

             { 
               "bugID": "Bug-03", "name":"some name", "description":"some description", "numBugs":"1",
               "bugs":[
                            {
                              "startCharacter":"123",
                              "numCharacters":"31",
                              "text":"some text"
                            }		
                    ]
             },
             { 
               "bugID": "Bug-04", "name":"some name", "description":"some description", "numBugs":"1",
               "bugs":[
                            {
                              "startCharacter":"1234",
                              "numCharacters":"32",
                              "text":"some text here too"
                            }		
                    ]
             }

      ]
    }

      def convertResultsToStrings(countsRowsComments:Seq[Seq[((Count, Rows), Comment)]]) = {
        countsRowsComments.flatMap{
          case countRowComment:Seq[((Int, Seq[String]), String)] =>
            countRowComment.flatMap{
              case ((count:Int, rows:Seq[String]), comment:String) =>
                val result = if (count > 10) {
                  rows.take(10) ++ Seq("...", s"[showing top 10 of $count rows]", "") 
                } else rows ++ Seq(s"[$count rows]", "")

                // Seq(comment)++Seq("// Rows returned: "+count)++result++Seq("// ----------------------")
                Seq(comment)++result // ++Seq("// ----------------------")
            }
        }
      }
      */

      case Right(t) =>
        val bugDetails = JSONUtil.createJSONObject(
          Array("startCharacter", "numCharacters", "text", "lineNum", "charNum"),
          Array(0, 0, t.getMessage, 0, 0)
        )

        val bug = JSONUtil.createJSONObject(
          Array(
            "bugID", "name", "description", "numBugs", "bugs", "level"
          ), 
          Array(
            "CompilerError", 
            t.getClass.getSimpleName, 
            t.getMessage, 
            1, 
            JSONUtil.encodeJSONSeq(Seq(bugDetails)), 
            2
          )
        )
        JSONUtil.createJSONObject(
          Array("numProblems", "problems"), 
          Array(1, JSONUtil.encodeJSONSeq(Seq(bug)))
        )
        //  case a:Throwable =>
        //      a.printStackTrace
        //      Seq(Seq(((1, Seq(a.getMessage)), "error")))

    }
  }

  // 
  def doAnalysisRealtime(
    dslCode:String, solidityFile:File
  ):Either[Seq[Seq[CountRowsComment]], Throwable] = try {
    // outer Seq, one element for each bug (one find pattern)
    // inner Seq, one element for each mapping
    // CountRowsComment = tuple contains following:
    //      1. How many rows returned?
    //      2. Actual rows returned?
    //      3. Comment (some info)
    //      
    val xdsl = DSLToXDSL.compileCode(dslCode, "noFile") 
    
    val analysisID = shaSmall(DQLUtil.xmlHash(xdsl) + CryptoUtil.md5(solidityFile))+        
                     org.sh.utils.Util.randomAlphanumericString(10) // added random to redo same analysis multiple times
    val tmpWorkDir = tmpDir + "/" +analysisID    
    trap.file.Util.createDir(tmpWorkDir)
    //    if (new File(tmpWorkDir).isDirectory) throw new Exception("Analysis already exists with ID: "+analysisID+". Please delete analysis first")
    val solidityPath = solidityFile.getAbsolutePath
    val outputDatalogFile = tmpWorkDir + "/"+solidityFile.getName+".facts"
    SolidityToDatalog.generateDatalog(solidityPath, outputDatalogFile)
    Left(DQLUtil.runDatalog(dslCode, Array(outputDatalogFile)))
  } catch {
    case a:Throwable => Right(a)
  }
    
  def doAnalysisBackground(sdqlFile:File, solidityFile:File) = {
    val dslPath = sdqlFile.getAbsolutePath
    val dslCode = readTextFileToString(dslPath)
    
    
    val xdsl = DSLToXDSL.compileCode(dslCode, sdqlFile.getName) //doCompilation(dslPath)
    
    val analysisID = shaSmall(DQLUtil.xmlHash(xdsl) + CryptoUtil.md5(solidityFile))+        
                     org.sh.utils.Util.randomAlphanumericString(10) // added random to redo same analysis multiple times
    val tmpWorkDir = tmpDir + "/" +analysisID    
    if (new File(tmpWorkDir).isDirectory) throw new Exception("Analysis already exists with ID: "+analysisID+". Please delete analysis first")

    val resultFile = tmpWorkDir + "/result.txt"
    val completeFile = tmpWorkDir + "/complete.txt"
    val solidityPath = solidityFile.getAbsolutePath
    val outputDatalogFile = tmpWorkDir + "/"+solidityFile.getName+".facts"
    
    createDir(tmpWorkDir)
    org.sh.utils.Util.doOnceNow{
      try { 
        SolidityToDatalog.generateDatalog(solidityPath, outputDatalogFile)
        DQLUtil.runDatalogWriteResults(dslCode, Array(outputDatalogFile), resultFile  )
        
      } catch{
        case a:Any => 
          writeToTextFile(resultFile, "An error occured: \n "+a.getClass+":"+a.getMessage+"\nCaused by\n"+a.getStackTrace.map(_.toString).reduceLeft(_+"  \n"+_))
      } finally {
        writeToTextFile(completeFile, analysisID)
      }
    }
    "analysisID: "+analysisID    
  }
  
  def uploadNewConfig(basis:File, rules:File) = {
    DQLUtil.loadBasis(basis.getAbsolutePath)
    DQLUtil.loadRules(rules.getAbsolutePath)
    SolidityToDatalog.solidityBootStrapFile = basis.getAbsolutePath
    "Ok"
  }
  def uploadNewMappings(mappings:File) = {
    DQLUtil.loadMappings(mappings.getAbsolutePath)
    "Ok"
  }
}
//import java.io.File
//import trap.datalog.optimizer.RuleFilter
//import trap.datalog.validator.RuleValidator
//import trap.dsl.DSLConfig
//import trap.dsl.DSLToXDSL._
//import trap.dsl.analyzer.DSLAnalysis
//import trap.file.TraitFilePropertyReader
//import trap.file.Util._
//import scala.util.Random._
////import test.DSLAnalyzer.Main
//import trap.datalog.RuleReader
//import trap.Util._
//import trap.xdsl.XDSLBasisUtil
//import org.sh.utils.Util._
//
//object SDQLUtil { //extends TraitFilePropertyReader {
//  //  val propertyFile = "bootstrap.properties"
//  //
//  //  val basisFile = read("defaultBootstrapFile") 
//  //  val ruleFile = read("defaultInitialRuleFile")
//  
//  var (optConfig:Option[DSLConfig], optBasisUtil:Option[XDSLBasisUtil], optRuleFilter:Option[RuleFilter]) = (None, None, None) //loadConfig(basisFile, ruleFile)
//  loadConfig(basisFile, ruleFile)
//  def loadConfig(basisFile:String, ruleFile:String) = {
//    val (loadedConfig, xdslRulesString) = (new DSLConfig(basisFile), new RuleReader(ruleFile).xdslRulesString)
//    println("Loaded basis from: "+basisFile)
//    println("Loaded rules from: "+ruleFile)
//    new RuleValidator(loadedConfig, xdslRulesString, _ => ()).validate // validate rules first
//    val basis = Array(loadedConfig.extendedBasis, loadedConfig.reducedBasis).flatten
//    optConfig = Some(loadedConfig)
//    optBasisUtil = Some(new XDSLBasisUtil(basis))
//    optRuleFilter = Some(new RuleFilter(xdslRulesString))
//    "Config loaded"
//  }
//  def notLoaded(st:String) = throw new Exception("not loaded: "+st)
//  def doCompilation(dslFile:String) = {
//    val xdsl = compile(dslFile)
//    Main.getFindQueries(optConfig.getOrElse(notLoaded("config")), optBasisUtil.getOrElse(notLoaded("basis")),  xdsl) // get find queries in DSL // for validation, not used otherwise, also for dup check
//    xdsl
//  }
//  def xmlHash(x: xml.Node):String = { // strips line etc (for checking if two DQL codes are identical in semantics
//    val a = x.attributes.filterNot{x =>
//      x.key == "line" || x.key == "ver" || x.key == "file"
//    }.foldLeft("")((u, attr) => shaSmall(u + shaSmall(attr.key + "|" + attr.value)))
//    (x \ "_").foldLeft(a+shaSmall(x.text.trim))((u, v) => shaSmall(u + xmlHash(v)))
//  }
//  def runDatalog(dslFile:String, dlFactsDir:String, outFile:String) = {
//    val config = optConfig.getOrElse(notLoaded("config"))
//    val basisUtil = optBasisUtil.getOrElse(notLoaded("basis"))
//    val ruleFilter = optRuleFilter.getOrElse(notLoaded("rule filter"))
//    val (findQueryMap, newRules) = Main.getFindQueries(config, basisUtil, compile(dslFile)) // get find queries in DSL
//    val queries = findQueryMap map(_._2._1)
//    val metaData = findQueryMap map (x => (x._1, x._2._1, x._2._2.reduceLeft(_+","+_)))
//    if (metaData.size > 0) { 
//      val metaDataString = "// ---- queries ----\n// resolve ids: "+config.resolveResults+"\n// ----\n"+metaData.map(x => 
//          "// "+x._1+"\n// results: "+x._2.name+"("+x._3+")\n// ----\n").reduceLeft(_+_) 
//      writeToTextFile(outFile, metaDataString)
//                  // filter rules
//      val filteredInitRules = ruleFilter.getUsedRules(newRules)            
//      val ruleString = if (filteredInitRules.size > 0) filteredInitRules.reduceLeft(_+_) else ""
//      val analyzer = new DSLAnalysis(ruleString)   
//      val elapsed = time{analyzer.getResults(queries, newRules, getAllFiles(dlFactsDir, Array("dl"), false), outFile)}
//      println ("Total time: "+elapsed/1000+"s")
//    } else println ("-- no queries --")    
//  }
//
//}
