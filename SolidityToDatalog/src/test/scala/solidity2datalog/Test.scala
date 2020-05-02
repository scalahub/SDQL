
package solidity2datalog

import sdql._
object TestSolidityCompiler {
  def main(args:Array[String]):Unit = {
    val solFile = "token.sol" // args(0)
    val xml = SolidityToXSolidity.getXML(solFile)
    println(trap.xml.Util.formatNicely(xml))
  }
}



object TestXSolitityLoader extends App { 
  import XSolidityLoader._
  val children = getChildren("token.sol") 
  getCodeAtoms(children) foreach println
  println
  children.foreach(printTree)
}
object TestSolidityParser extends App {
  val xml = SolidityToXSolidity.getXML("token.sol")
  val json = """
{"metadata":"{\"compiler\":{\"version\":\"0.4.9+commit.364da425\"},\"language\":\"Solidity\",\"output\":{\"abi\":[{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"balances\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"total_supply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"receiver\",\"type\":\"address\"}],\"name\":\"testPayable\",\"outputs\":[],\"payable\":true,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"mint\",\"outputs\":[],\"payable\":true,\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"address_to_lookup\",\"type\":\"address\"}],\"name\":\"getBalanceOfAddress\",\"outputs\":[{\"name\":\"balance\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"receiver\",\"type\":\"address\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"transfer\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_id\",\"type\":\"bytes32\"}],\"name\":\"deposit\",\"outputs\":[],\"payable\":true,\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getTotalSupply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"inputs\":[{\"name\":\"_total_supply\",\"type\":\"uint256\"},{\"name\":\"_token_name\",\"type\":\"string\"}],\"payable\":false,\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"_from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"_id\",\"type\":\"bytes32\"},{\"indexed\":false,\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"Deposit\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"sender\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"receiver\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"minter\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Minted\",\"type\":\"event\"}],\"devdoc\":{\"methods\":{}},\"userdoc\":{\"methods\":{}}},\"settings\":{\"compilationTarget\":{\"token.sol\":\"Token\"},\"libraries\":{},\"optimizer\":{\"enabled\":false,\"runs\":200},\"remappings\":[]},\"sources\":{\"token.sol\":{\"keccak256\":\"0x1a783e2c05b7bbb4eaf451274c3ada4e7f14500ffd4a2c1c50828da462e9e00f\",\"urls\":[\"bzzr://2f8a7cdddf02e42efdd9f76ef2afb1cb2bb97b55d5a04188141579374deceaef\"]}},\"version\":1}"}}"""
  val x = trap.xml.Util.formatNicely(xml)
  (xml \ "JSON") foreach println
  println(x)
/*
"""
<JSON>
  <contracts>
    <token.sol:Token>
      <metadata>
        {&quot;compiler&quot;:{&quot;version&quot;:&quot;0.4.9+commit.36x4da425&quot;},&quot;language&quot;:&quot;Solidity&quot;,&quot;output&quot;:{&quot;abi&quot;:[{&quot;constant&quot;:true,&quot;inputs&quot;:[{&quot;name&quot;:&quot;&quot;,&quot;type&quot;:&quot;address&quot;}],&quot;name&quot;:&quot;balances&quot;,&quot;outputs&quot;:[{&quot;name&quot;:&quot;&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:true,&quot;inputs&quot;:[],&quot;name&quot;:&quot;total_supply&quot;,&quot;outputs&quot;:[{&quot;name&quot;:&quot;&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:true,&quot;inputs&quot;:[],&quot;name&quot;:&quot;owner&quot;,&quot;outputs&quot;:[{&quot;name&quot;:&quot;&quot;,&quot;type&quot;:&quot;address&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:false,&quot;inputs&quot;:[{&quot;name&quot;:&quot;receiver&quot;,&quot;type&quot;:&quot;address&quot;}],&quot;name&quot;:&quot;testPayable&quot;,&quot;outputs&quot;:[],&quot;payable&quot;:true,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:false,&quot;inputs&quot;:[{&quot;name&quot;:&quot;amount&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;name&quot;:&quot;mint&quot;,&quot;outputs&quot;:[],&quot;payable&quot;:true,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:true,&quot;inputs&quot;:[{&quot;name&quot;:&quot;address_to_lookup&quot;,&quot;type&quot;:&quot;address&quot;}],&quot;name&quot;:&quot;getBalanceOfAddress&quot;,&quot;outputs&quot;:[{&quot;name&quot;:&quot;balance&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:false,&quot;inputs&quot;:[{&quot;name&quot;:&quot;receiver&quot;,&quot;type&quot;:&quot;address&quot;},{&quot;name&quot;:&quot;amount&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;name&quot;:&quot;transfer&quot;,&quot;outputs&quot;:[],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:false,&quot;inputs&quot;:[{&quot;name&quot;:&quot;_id&quot;,&quot;type&quot;:&quot;bytes32&quot;}],&quot;name&quot;:&quot;deposit&quot;,&quot;outputs&quot;:[],&quot;payable&quot;:true,&quot;type&quot;:&quot;function&quot;},{&quot;constant&quot;:true,&quot;inputs&quot;:[],&quot;name&quot;:&quot;getTotalSupply&quot;,&quot;outputs&quot;:[{&quot;name&quot;:&quot;&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;function&quot;},{&quot;inputs&quot;:[{&quot;name&quot;:&quot;_total_supply&quot;,&quot;type&quot;:&quot;uint256&quot;},{&quot;name&quot;:&quot;_token_name&quot;,&quot;type&quot;:&quot;string&quot;}],&quot;payable&quot;:false,&quot;type&quot;:&quot;constructor&quot;},{&quot;anonymous&quot;:false,&quot;inputs&quot;:[{&quot;indexed&quot;:true,&quot;name&quot;:&quot;_from&quot;,&quot;type&quot;:&quot;address&quot;},{&quot;indexed&quot;:true,&quot;name&quot;:&quot;_id&quot;,&quot;type&quot;:&quot;bytes32&quot;},{&quot;indexed&quot;:false,&quot;name&quot;:&quot;_value&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;name&quot;:&quot;Deposit&quot;,&quot;type&quot;:&quot;event&quot;},{&quot;anonymous&quot;:false,&quot;inputs&quot;:[{&quot;indexed&quot;:false,&quot;name&quot;:&quot;sender&quot;,&quot;type&quot;:&quot;address&quot;},{&quot;indexed&quot;:false,&quot;name&quot;:&quot;receiver&quot;,&quot;type&quot;:&quot;address&quot;},{&quot;indexed&quot;:false,&quot;name&quot;:&quot;amount&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;name&quot;:&quot;Transfer&quot;,&quot;type&quot;:&quot;event&quot;},{&quot;anonymous&quot;:false,&quot;inputs&quot;:[{&quot;indexed&quot;:false,&quot;name&quot;:&quot;minter&quot;,&quot;type&quot;:&quot;address&quot;},{&quot;indexed&quot;:false,&quot;name&quot;:&quot;amount&quot;,&quot;type&quot;:&quot;uint256&quot;}],&quot;name&quot;:&quot;Minted&quot;,&quot;type&quot;:&quot;event&quot;}],&quot;devdoc&quot;:{&quot;methods&quot;:{}},&quot;userdoc&quot;:{&quot;methods&quot;:{}}},&quot;settings&quot;:{&quot;compilationTarget&quot;:{&quot;token.sol&quot;:&quot;Token&quot;},&quot;libraries&quot;:{},&quot;optimizer&quot;:{&quot;enabled&quot;:false,&quot;runs&quot;:200},&quot;remappings&quot;:[]},&quot;sources&quot;:{&quot;token.sol&quot;:{&quot;keccak256&quot;:&quot;0x1a783e2c05b7bbb4eaf451274c3ada4e7f14500ffd4a2c1c50828da462e9e00f&quot;,&quot;urls&quot;:[&quot;bzzr://2f8a7cdddf02e42efdd9f76ef2afb1cb2bb97b55d5a04188141579374deceaef&quot;]}},&quot;version&quot;:1}
      </metadata>
    </token.sol:Token>
  </contracts>
  <version>0.4.9+commit.364da425.Linux.g++</version>
</JSON>
"""
 */
}
