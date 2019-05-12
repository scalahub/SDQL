
package sdql

import org.sh.reflect.web._

object GenerateWebBoxHTML  {  
  val formObjects = Array(
    Analyzer,
    AnalyzerMultiTenant
//    ,
//    Config,
//    Compiler
  )
  val htmlFilePrefix = "" // this will be prepended to html file name
  def main(args:Array[String]) { 
    println("Auto-Generating file")

    val formInfo = """<h2>Smart Scan</h2>Smart Scan is a static analysis tool for Solidty smart contracts. It detects bug patterns encoded in a DSL (domain-specific language) called Solidity Data Query Language (SDQL). 
It is highly configurable and has a user-friendly syntax. 

	<h3>HOW TO USE THIS SITE</h3>
(a) Create/obtain a SDQL file containing your analysis. Test that it compiles successfully using the method <a href='#sdql.Compiler.compileSDQLCode'>sdql.Compiler.compileSDQLCode</a>
(b) Paste the contents of the SDQL file + Solidity file and start the analysis using the method <a href='#sdql.Analyzer.doAnalysis'>sdql.Analyzer.doAnalysis</a>."""
    lazy val cg = new HTMLClientCodeGenerator(formObjects.toList.sortWith{(l, r) => l.getClass.getCanonicalName < r.getClass.getCanonicalName}, "web", formInfo, None, false, false)
    cg.pageTitle = "Smart Scan"
    try {
      cg.autoGenerateToFile(htmlFilePrefix, "src/main/webapp", "")    	
    } catch { case t:Throwable => t.printStackTrace } 
    finally System.exit(1)
  }
}




