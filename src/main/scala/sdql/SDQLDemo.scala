package sdql

import dql.DQLUtil
import org.sh.easyweb.AutoWeb

// Alternative to manual setup using GenerateWebBoxHTML
object SDQLDemo extends App {
  DQLUtil
  Config // ???
  Compiler // load basis
  val objects = GenerateWebBoxHTML.formObjects.toList
  new AutoWeb(objects, "SDQL Demo")
}
