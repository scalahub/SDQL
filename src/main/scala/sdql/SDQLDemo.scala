package sdql

import dql.DQLUtil
import org.sh.easyweb.AutoWeb

object SDQLDemo extends App {

  DQLUtil
  Config // ???
  Compiler // load basis

  val formObjects = List(
    Analyzer,
    AnalyzerMultiTenant,
    Config,
    Compiler
  )
  val formInfo = """<h2>SDQL</h2>SDQL is a static analysis tool for Solidty smart contracts. It detects bug patterns encoded in a DSL (domain-specific language) called Solidity Data Query Language (SDQL).
It is highly configurable and has a user-friendly syntax.

	<h3>HOW TO USE THIS SITE</h3>
(a) Create/obtain a SDQL file containing your analysis. Test that it compiles successfully using the method <a href='#sdql.Compiler.compileSDQLCode'>sdql.Compiler.compileSDQLCode</a>
(b) Paste the contents of the SDQL file + Solidity file and start the analysis using the method <a href='#sdql.Analyzer.doAnalysis'>sdql.Analyzer.doAnalysis</a>."""

  new AutoWeb(formObjects, formInfo)
  //    EasyProxy.addProcessor("", SDQLAdmin, DefaultTypeHandler, true)
  //    EasyProxy.addProcessor("", SolidityCompiler, DefaultTypeHandler, true)
}


