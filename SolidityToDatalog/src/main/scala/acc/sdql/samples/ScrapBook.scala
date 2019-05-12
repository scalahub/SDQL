package acc.sdql.samples

object ScrapBook {

  // contains samples for writing parser
/*
{"name":"UsingForDirective"}
{"name":"Return"}
{"name":"FunctionDefinition","attributes":"constant,payable,visibility,name"}
{"name":"IfStatement"}
{"name":"UserDefinedTypeName","attributes":"name"}
{"name":"FunctionCall","attributes":"type_conversion,type"}
{"name":"IndexAccess","attributes":"type"}
{"name":"Literal","attributes":"hexvalue,subdenomination,token,type,value"}
{"name":"UnaryOperation","attributes":"prefix,type,operator"}
{"name":"Mapping"}
{"name":"WhileStatement"}
{"name":"StructDefinition","attributes":"name"}
{"name":"ParameterList"}
{"name":"Break"}
{"name":"PragmaDirective","attributes":"literals"}
{"name":"VariableDeclaration","attributes":"name,type"}
{"name":"ElementaryTypeNameExpression","attributes":"type,value"}
{"name":"ElementaryTypeName","attributes":"name"}
{"name":"ContractDefinition","attributes":"fullyImplemented,linearizedBaseContracts,isLibrary,name"}
{"name":"PlaceholderStatement"}
{"name":"ForStatement"}
{"name":"ArrayTypeName"}
{"name":"InheritanceSpecifier"}
{"name":"Identifier","attributes":"type,value"}
{"name":"ModifierDefinition","attributes":"name"}
{"name":"Assignment","attributes":"type,operator"}
{"name":"DoWhileStatement"}
{"name":"ModifierInvocation"}
{"name":"Block"}
{"name":"EventDefinition","attributes":"name"}
{"name":"BinaryOperation","attributes":"type,operator"}
{"name":"Continue"}
{"name":"Throw"}
{"name":"ExpressionStatement"}
{"name":"MemberAccess","attributes":"member_name,type"}
{"name":"VariableDeclarationStatement"}
****************************************
pragma solidity ^0.4.9;
contract Foo {}
contract Bar {}
library Search {
    function indexOf(uint[] storage self, uint value) returns (uint) {
        for (uint i = 0; i < self.length; i++)
            if (self[i] == value) return i;
        return uint(-1);
    }
}
contract Token is Foo, Bar {
    using Search for uint[];
    
    struct Test {
        uint amount;
        address test_address;
    }
    
    //Test test = Test({amount: 399, test_address:0x2344});
    
    mapping (address => Test) structs;
    
    Test test = structs[0x12345];
    
  // test.amount;
    event Deposit(
        address indexed _from,
        bytes32 indexed _id,
        uint _value
    );
    function deposit(bytes32 _id) payable {
	// Any call to this function (even deeply nested) can
        // be detected from the JavaScript API by filtering
        // for `Deposit` to be called.
        Deposit(msg.sender, _id, msg.value);
    }
    
    address public owner;
    uint public total_supply;
    mapping (address => uint) public balances;
    string token_name;
    
    function Token(uint _total_supply, string _token_name) {
        owner = msg.sender;
        total_supply = _total_supply;
        token_name = _token_name;
        balances[msg.sender] = total_supply;
    }
    modifier hasBalance(uint amount) {
        if (balances[msg.sender] < amount) throw;
        _;
    }
    uint foo;
    modifier noOverflow(address receiver, uint amount) {
        // check for overflow
        if (balances[receiver] + amount <= balances[receiver]) throw;
        _;
    }
    
    modifier onlyOwner() {
        if (msg.sender != owner) throw;
        _;
    }
    event Transfer(address sender, address receiver, uint amount);
    
    function transfer(address receiver, uint amount) 
        hasBalance(amount) 
        noOverflow(receiver, amount)
    {
        balances[msg.sender] -= amount;
	
	{
		balances[0x444] = 34;
	}
	uint foo;
        balances[receiver] += amount;
        Transfer(msg.sender, receiver, amount);
    } 
    
    modifier noTotalSupplyOverflow(uint amount) {
        if (total_supply+amount <= total_supply) throw;
        _;
    }
    event Minted(address minter, uint amount);
    function mint(uint amount) 
        onlyOwner 
        noOverflow(msg.sender, amount) 
        noTotalSupplyOverflow(amount)
        payable
    {
        balances[msg.sender] += amount;
        total_supply += amount;
        Minted(msg.sender, amount);
	for (uint same3 = 0; same3 < 1; same3++) {
		// loop test
		total_supply += 304;
		if (total_supply < 4005) {
			total_supply += 59595;
		} else {
			uint foo;
			total_supply -= 595;
			while(total_supply > 49944) {
				//uint foo;
				total_supply -= 595;
				if (msg.sender == owner) break;
				if (msg.sender != 0x12345) continue;
				total_supply -= 595;				
				do {
					total_supply -= 5344;				
					if (tx.origin != 0x49595696) throw;
					//uint foo = 44;
				} while(total_supply != 4444);
			}
		}
        }

    }
    function getBalanceOfAddress(address address_to_lookup) constant returns(uint balance) {
        balance = balances[address_to_lookup];
    }
    
    function getTotalSupply() constant returns(uint) {return total_supply;}
    function testPayable(address receiver) payable {
        uint amount = msg.value;
        if (!receiver.send(amount)) throw;
    }
}
******************************************
- {"literals":".9","id":1}
- {"fullyImplemented":"true","linearizedBaseContracts":"2","isLibrary":"false","name":"Foo","id":2}
- {"fullyImplemented":"true","linearizedBaseContracts":"3","isLibrary":"false","name":"Bar","id":3}
- {"fullyImplemented":"true","linearizedBaseContracts":"40","isLibrary":"true","name":"Search","id":40}
-- {"constant":"false","payable":"false","visibility":"public","name":"indexOf","id":39}
--- {"id":9}
---- {"name":"self","id":6,"type":"uint256[] storage pointer"}
----- {"id":5}
------ {"name":"uint","id":4}
---- {"name":"value","id":8,"type":"uint256"}
----- {"name":"uint","id":7}
--- {"id":12}
---- {"name":"","id":11,"type":"uint256"}
----- {"name":"uint","id":10}
--- {"id":38}
---- {"id":32}
----- {"id":16}
------ {"name":"i","id":14,"type":"uint256"}
------- {"name":"uint","id":13}
------ {"subdenomination":"null","hexvalue":"30","id":15,"type":"int_const 0","value":"0","token":"null"}
----- {"id":20,"type":"bool","operator":"<"}
------ {"id":17,"type":"uint256","value":"i"}
------ {"id":19,"member_name":"length","type":"uint256"}
------- {"id":18,"type":"uint256[] storage pointer","value":"self"}
----- {"id":23}
------ {"prefix":"false","id":22,"type":"uint256","operator":"++"}
------- {"id":21,"type":"uint256","value":"i"}
----- {"id":31}
------ {"id":28,"type":"bool","operator":"=="}
------- {"id":26,"type":"uint256"}
-------- {"id":24,"type":"uint256[] storage pointer","value":"self"}
-------- {"id":25,"type":"uint256","value":"i"}
------- {"id":27,"type":"uint256","value":"value"}
------ {"id":30}
------- {"id":29,"type":"uint256","value":"i"}
---- {"id":37}
----- {"type_conversion":"true","id":36,"type":"uint256"}
------ {"id":33,"type":"type(uint256)","value":"uint"}
------ {"prefix":"true","id":35,"type":"int_const -1","operator":"-"}
------- {"subdenomination":"null","hexvalue":"31","id":34,"type":"int_const 1","value":"1","token":"null"}
- {"fullyImplemented":"true","linearizedBaseContracts":"2","isLibrary":"false","name":"Token","id":386}
-- {"id":42}
--- {"name":"Foo","id":41}
-- {"id":44}
--- {"name":"Bar","id":43}
-- {"id":48}
--- {"name":"Search","id":45}
--- {"id":47}
---- {"name":"uint","id":46}
-- {"name":"Test","id":53}
--- {"name":"amount","id":50,"type":"uint256"}
---- {"name":"uint","id":49}
--- {"name":"test_address","id":52,"type":"address"}
---- {"name":"address","id":51}
-- {"name":"structs","id":57,"type":"mapping(address => struct Token.Test storage ref)"}
--- {"id":56}
---- {"name":"address","id":54}
---- {"name":"Test","id":55}
-- {"name":"test","id":62,"type":"struct Token.Test storage ref"}
--- {"name":"Test","id":58}
--- {"id":61,"type":"struct Token.Test storage ref"}
---- {"id":59,"type":"mapping(address => struct Token.Test storage ref)","value":"structs"}
---- {"subdenomination":"null","hexvalue":"30783132333435","id":60,"type":"int_const 74565","value":"0x12345","token":"null"}
-- {"name":"Deposit","id":70}
--- {"id":69}
---- {"name":"_from","id":64,"type":"address"}
----- {"name":"address","id":63}
---- {"name":"_id","id":66,"type":"bytes32"}
----- {"name":"bytes32","id":65}
---- {"name":"_value","id":68,"type":"uint256"}
----- {"name":"uint","id":67}
-- {"constant":"false","payable":"true","visibility":"public","name":"deposit","id":84}
--- {"id":73}
---- {"name":"_id","id":72,"type":"bytes32"}
----- {"name":"bytes32","id":71}
--- {"id":74}
--- {"id":83}
---- {"id":82}
----- {"type_conversion":"false","id":81,"type":"tuple()"}
------ {"id":75,"type":"function (address,bytes32,uint256) constant","value":"Deposit"}
------ {"id":77,"member_name":"sender","type":"address"}
------- {"id":76,"type":"msg","value":"msg"}
------ {"id":78,"type":"bytes32","value":"_id"}
------ {"id":80,"member_name":"value","type":"uint256"}
------- {"id":79,"type":"msg","value":"msg"}
-- {"name":"owner","id":86,"type":"address"}
--- {"name":"address","id":85}
-- {"name":"total_supply","id":88,"type":"uint256"}
--- {"name":"uint","id":87}
-- {"name":"balances","id":92,"type":"mapping(address => uint256)"}
--- {"id":91}
---- {"name":"address","id":89}
---- {"name":"uint","id":90}
-- {"name":"token_name","id":94,"type":"string storage ref"}
--- {"name":"string","id":93}
-- {"constant":"false","payable":"false","visibility":"public","name":"Token","id":122}
--- {"id":99}
---- {"name":"_total_supply","id":96,"type":"uint256"}
----- {"name":"uint","id":95}
---- {"name":"_token_name","id":98,"type":"string memory"}
----- {"name":"string","id":97}
--- {"id":100}
--- {"id":121}
---- {"id":105}
----- {"id":104,"type":"address","operator":"="}
------ {"id":101,"type":"address","value":"owner"}
------ {"id":103,"member_name":"sender","type":"address"}
------- {"id":102,"type":"msg","value":"msg"}
---- {"id":109}
----- {"id":108,"type":"uint256","operator":"="}
------ {"id":106,"type":"uint256","value":"total_supply"}
------ {"id":107,"type":"uint256","value":"_total_supply"}
---- {"id":113}
----- {"id":112,"type":"string storage ref","operator":"="}
------ {"id":110,"type":"string storage ref","value":"token_name"}
------ {"id":111,"type":"string memory","value":"_token_name"}
---- {"id":120}
----- {"id":119,"type":"uint256","operator":"="}
------ {"id":117,"type":"uint256"}
------- {"id":114,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":116,"member_name":"sender","type":"address"}
-------- {"id":115,"type":"msg","value":"msg"}
------ {"id":118,"type":"uint256","value":"total_supply"}
-- {"name":"hasBalance","id":136}
--- {"id":125}
---- {"name":"amount","id":124,"type":"uint256"}
----- {"name":"uint","id":123}
--- {"id":135}
---- {"id":133}
----- {"id":131,"type":"bool","operator":"<"}
------ {"id":129,"type":"uint256"}
------- {"id":126,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":128,"member_name":"sender","type":"address"}
-------- {"id":127,"type":"msg","value":"msg"}
------ {"id":130,"type":"uint256","value":"amount"}
----- {"id":132}
---- {"id":134}
-- {"name":"foo","id":138,"type":"uint256"}
--- {"name":"uint","id":137}
-- {"name":"noOverflow","id":157}
--- {"id":143}
---- {"name":"receiver","id":140,"type":"address"}
----- {"name":"address","id":139}
---- {"name":"amount","id":142,"type":"uint256"}
----- {"name":"uint","id":141}
--- {"id":156}
---- {"id":154}
----- {"id":152,"type":"bool","operator":"<="}
------ {"id":148,"type":"uint256","operator":"+"}
------- {"id":146,"type":"uint256"}
-------- {"id":144,"type":"mapping(address => uint256)","value":"balances"}
-------- {"id":145,"type":"address","value":"receiver"}
------- {"id":147,"type":"uint256","value":"amount"}
------ {"id":151,"type":"uint256"}
------- {"id":149,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":150,"type":"address","value":"receiver"}
----- {"id":153}
---- {"id":155}
-- {"name":"onlyOwner","id":167}
--- {"id":158}
--- {"id":166}
---- {"id":164}
----- {"id":162,"type":"bool","operator":"!="}
------ {"id":160,"member_name":"sender","type":"address"}
------- {"id":159,"type":"msg","value":"msg"}
------ {"id":161,"type":"address","value":"owner"}
----- {"id":163}
---- {"id":165}
-- {"name":"Transfer","id":175}
--- {"id":174}
---- {"name":"sender","id":169,"type":"address"}
----- {"name":"address","id":168}
---- {"name":"receiver","id":171,"type":"address"}
----- {"name":"address","id":170}
---- {"name":"amount","id":173,"type":"uint256"}
----- {"name":"uint","id":172}
-- {"constant":"false","payable":"false","visibility":"public","name":"transfer","id":220}
--- {"id":180}
---- {"name":"receiver","id":177,"type":"address"}
----- {"name":"address","id":176}
---- {"name":"amount","id":179,"type":"uint256"}
----- {"name":"uint","id":178}
--- {"id":188}
--- {"id":183}
---- {"id":181,"type":"modifier (uint256)","value":"hasBalance"}
---- {"id":182,"type":"uint256","value":"amount"}
--- {"id":187}
---- {"id":184,"type":"modifier (address,uint256)","value":"noOverflow"}
---- {"id":185,"type":"address","value":"receiver"}
---- {"id":186,"type":"uint256","value":"amount"}
--- {"id":219}
---- {"id":195}
----- {"id":194,"type":"uint256","operator":"-="}
------ {"id":192,"type":"uint256"}
------- {"id":189,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":191,"member_name":"sender","type":"address"}
-------- {"id":190,"type":"msg","value":"msg"}
------ {"id":193,"type":"uint256","value":"amount"}
---- {"id":202}
----- {"id":201}
------ {"id":200,"type":"uint256","operator":"="}
------- {"id":198,"type":"uint256"}
-------- {"id":196,"type":"mapping(address => uint256)","value":"balances"}
-------- {"subdenomination":"null","hexvalue":"3078343434","id":197,"type":"int_const 1092","value":"0x444","token":"null"}
------- {"subdenomination":"null","hexvalue":"3334","id":199,"type":"int_const 34","value":"34","token":"null"}
---- {"id":205}
----- {"name":"foo","id":204,"type":"uint256"}
------ {"name":"uint","id":203}
---- {"id":211}
----- {"id":210,"type":"uint256","operator":"+="}
------ {"id":208,"type":"uint256"}
------- {"id":206,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":207,"type":"address","value":"receiver"}
------ {"id":209,"type":"uint256","value":"amount"}
---- {"id":218}
----- {"type_conversion":"false","id":217,"type":"tuple()"}
------ {"id":212,"type":"function (address,address,uint256) constant","value":"Transfer"}
------ {"id":214,"member_name":"sender","type":"address"}
------- {"id":213,"type":"msg","value":"msg"}
------ {"id":215,"type":"address","value":"receiver"}
------ {"id":216,"type":"uint256","value":"amount"}
-- {"name":"noTotalSupplyOverflow","id":233}
--- {"id":223}
---- {"name":"amount","id":222,"type":"uint256"}
----- {"name":"uint","id":221}
--- {"id":232}
---- {"id":230}
----- {"id":228,"type":"bool","operator":"<="}
------ {"id":226,"type":"uint256","operator":"+"}
------- {"id":224,"type":"uint256","value":"total_supply"}
------- {"id":225,"type":"uint256","value":"amount"}
------ {"id":227,"type":"uint256","value":"total_supply"}
----- {"id":229}
---- {"id":231}
-- {"name":"Minted","id":239}
--- {"id":238}
---- {"name":"minter","id":235,"type":"address"}
----- {"name":"address","id":234}
---- {"name":"amount","id":237,"type":"uint256"}
----- {"name":"uint","id":236}
-- {"constant":"false","payable":"true","visibility":"public","name":"mint","id":345}
--- {"id":242}
---- {"name":"amount","id":241,"type":"uint256"}
----- {"name":"uint","id":240}
--- {"id":253}
--- {"id":244}
---- {"id":243,"type":"modifier ()","value":"onlyOwner"}
--- {"id":249}
---- {"id":245,"type":"modifier (address,uint256)","value":"noOverflow"}
---- {"id":247,"member_name":"sender","type":"address"}
----- {"id":246,"type":"msg","value":"msg"}
---- {"id":248,"type":"uint256","value":"amount"}
--- {"id":252}
---- {"id":250,"type":"modifier (uint256)","value":"noTotalSupplyOverflow"}
---- {"id":251,"type":"uint256","value":"amount"}
--- {"id":344}
---- {"id":260}
----- {"id":259,"type":"uint256","operator":"+="}
------ {"id":257,"type":"uint256"}
------- {"id":254,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":256,"member_name":"sender","type":"address"}
-------- {"id":255,"type":"msg","value":"msg"}
------ {"id":258,"type":"uint256","value":"amount"}
---- {"id":264}
----- {"id":263,"type":"uint256","operator":"+="}
------ {"id":261,"type":"uint256","value":"total_supply"}
------ {"id":262,"type":"uint256","value":"amount"}
---- {"id":270}
----- {"type_conversion":"false","id":269,"type":"tuple()"}
------ {"id":265,"type":"function (address,uint256) constant","value":"Minted"}
------ {"id":267,"member_name":"sender","type":"address"}
------- {"id":266,"type":"msg","value":"msg"}
------ {"id":268,"type":"uint256","value":"amount"}
---- {"id":343}
----- {"id":274}
------ {"name":"same3","id":272,"type":"uint256"}
------- {"name":"uint","id":271}
------ {"subdenomination":"null","hexvalue":"30","id":273,"type":"int_const 0","value":"0","token":"null"}
----- {"id":277,"type":"bool","operator":"<"}
------ {"id":275,"type":"uint256","value":"same3"}
------ {"subdenomination":"null","hexvalue":"31","id":276,"type":"int_const 1","value":"1","token":"null"}
----- {"id":280}
------ {"prefix":"false","id":279,"type":"uint256","operator":"++"}
------- {"id":278,"type":"uint256","value":"same3"}
----- {"id":342}
------ {"id":284}
------- {"id":283,"type":"uint256","operator":"+="}
-------- {"id":281,"type":"uint256","value":"total_supply"}
-------- {"subdenomination":"null","hexvalue":"333034","id":282,"type":"int_const 304","value":"304","token":"null"}
------ {"id":341}
------- {"id":287,"type":"bool","operator":"<"}
-------- {"id":285,"type":"uint256","value":"total_supply"}
-------- {"subdenomination":"null","hexvalue":"34303035","id":286,"type":"int_const 4005","value":"4005","token":"null"}
------- {"id":292}
-------- {"id":291}
--------- {"id":290,"type":"uint256","operator":"+="}
---------- {"id":288,"type":"uint256","value":"total_supply"}
---------- {"subdenomination":"null","hexvalue":"3539353935","id":289,"type":"int_const 59595","value":"59595","token":"null"}
------- {"id":340}
-------- {"id":295}
--------- {"name":"foo","id":294,"type":"uint256"}
---------- {"name":"uint","id":293}
-------- {"id":299}
--------- {"id":298,"type":"uint256","operator":"-="}
---------- {"id":296,"type":"uint256","value":"total_supply"}
---------- {"subdenomination":"null","hexvalue":"353935","id":297,"type":"int_const 595","value":"595","token":"null"}
-------- {"id":339}
--------- {"id":302,"type":"bool","operator":">"}
---------- {"id":300,"type":"uint256","value":"total_supply"}
---------- {"subdenomination":"null","hexvalue":"3439393434","id":301,"type":"int_const 49944","value":"49944","token":"null"}
--------- {"id":338}
---------- {"id":306}
----------- {"id":305,"type":"uint256","operator":"-="}
------------ {"id":303,"type":"uint256","value":"total_supply"}
------------ {"subdenomination":"null","hexvalue":"353935","id":304,"type":"int_const 595","value":"595","token":"null"}
---------- {"id":312}
----------- {"id":310,"type":"bool","operator":"=="}
------------ {"id":308,"member_name":"sender","type":"address"}
------------- {"id":307,"type":"msg","value":"msg"}
------------ {"id":309,"type":"address","value":"owner"}
----------- {"id":311}
---------- {"id":318}
----------- {"id":316,"type":"bool","operator":"!="}
------------ {"id":314,"member_name":"sender","type":"address"}
------------- {"id":313,"type":"msg","value":"msg"}
------------ {"subdenomination":"null","hexvalue":"30783132333435","id":315,"type":"int_const 74565","value":"0x12345","token":"null"}
----------- {"id":317}
---------- {"id":322}
----------- {"id":321,"type":"uint256","operator":"-="}
------------ {"id":319,"type":"uint256","value":"total_supply"}
------------ {"subdenomination":"null","hexvalue":"353935","id":320,"type":"int_const 595","value":"595","token":"null"}
---------- {"id":337}
----------- {"id":336,"type":"bool","operator":"!="}
------------ {"id":334,"type":"uint256","value":"total_supply"}
------------ {"subdenomination":"null","hexvalue":"34343434","id":335,"type":"int_const 4444","value":"4444","token":"null"}
----------- {"id":333}
------------ {"id":326}
------------- {"id":325,"type":"uint256","operator":"-="}
-------------- {"id":323,"type":"uint256","value":"total_supply"}
-------------- {"subdenomination":"null","hexvalue":"35333434","id":324,"type":"int_const 5344","value":"5344","token":"null"}
------------ {"id":332}
------------- {"id":330,"type":"bool","operator":"!="}
-------------- {"id":328,"member_name":"origin","type":"address"}
--------------- {"id":327,"type":"tx","value":"tx"}
-------------- {"subdenomination":"null","hexvalue":"30783439353935363936","id":329,"type":"int_const 1230591638","value":"0x49595696","token":"null"}
------------- {"id":331}
-- {"constant":"true","payable":"false","visibility":"public","name":"getBalanceOfAddress","id":359}
--- {"id":348}
---- {"name":"address_to_lookup","id":347,"type":"address"}
----- {"name":"address","id":346}
--- {"id":351}
---- {"name":"balance","id":350,"type":"uint256"}
----- {"name":"uint","id":349}
--- {"id":358}
---- {"id":357}
----- {"id":356,"type":"uint256","operator":"="}
------ {"id":352,"type":"uint256","value":"balance"}
------ {"id":355,"type":"uint256"}
------- {"id":353,"type":"mapping(address => uint256)","value":"balances"}
------- {"id":354,"type":"address","value":"address_to_lookup"}
-- {"constant":"true","payable":"false","visibility":"public","name":"getTotalSupply","id":367}
--- {"id":360}
--- {"id":363}
---- {"name":"","id":362,"type":"uint256"}
----- {"name":"uint","id":361}
--- {"id":366}
---- {"id":365}
----- {"id":364,"type":"uint256","value":"total_supply"}
-- {"constant":"false","payable":"true","visibility":"public","name":"testPayable","id":385}
--- {"id":370}
---- {"name":"receiver","id":369,"type":"address"}
----- {"name":"address","id":368}
--- {"id":371}
--- {"id":384}
---- {"id":376}
----- {"name":"amount","id":373,"type":"uint256"}
------ {"name":"uint","id":372}
----- {"id":375,"member_name":"value","type":"uint256"}
------ {"id":374,"type":"msg","value":"msg"}
---- {"id":383}
----- {"prefix":"true","id":381,"type":"bool","operator":"!"}
------ {"type_conversion":"false","id":380,"type":"bool"}
------- {"id":378,"member_name":"send","type":"function (uint256) returns (bool)"}
-------- {"id":377,"type":"address","value":"receiver"}
------- {"id":379,"type":"uint256","value":"amount"}
----- {"id":382}
*/

/*
//object Sample{
//  // sample xml
//  def xml = 
//    <JSON>
//      <children>
//        <src>0:23:-1</src>
//        <name>PragmaDirective</name>
//        <attributes>
//          <literals>solidity</literals>
//          <literals>^</literals>
//          <literals>0.4</literals>
//          <literals>.9</literals>
//        </attributes>
//        <id>1</id>
//      </children>
//      <children>
//        <src>24:39:-1</src>
//        <name>ContractDefinition</name>
//        <attributes>
//          <fullyImplemented>true</fullyImplemented>
//          <linearizedBaseContracts>2</linearizedBaseContracts>
//          <isLibrary>false</isLibrary>
//          <name>mortal</name>
//        </attributes>
//        <id>2</id>
//      </children>
//      <name>SourceUnit</name>
//    </JSON>
//  
////- {"srcChars":23,"name":"PragmaDirective","ATTR_literals":".9","id":1,"srcSt":0}
////- {"ATTR_name":"mortal","ATTR_fullyImplemented":"true","srcChars":58,"ATTR_isLibrary":"false","name":"ContractDefinition","ATTR_linearizedBaseContracts":"8","id":8,"srcSt":24}
////-- {"ATTR_name":"owner","ATTR_type":"address","srcChars":13,"name":"VariableDeclaration","id":3,"srcSt":44}
////--- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":2,"srcSt":44}
////-- {"ATTR_name":"foo","ATTR_payable":"false","ATTR_visibility":"public","srcChars":19,"name":"FunctionDefinition","id":7,"srcSt":61,"ATTR_constant":"false"}
////--- {"srcChars":2,"name":"ParameterList","id":4,"srcSt":73}
////--- {"srcChars":0,"name":"ParameterList","id":5,"srcSt":76}
////--- {"srcChars":4,"name":"Block","id":6,"srcSt":76}

////{"name":"FunctionDefinition","attributes":"constant,payable,visibility,name"}
////{"name":"IfStatement","attributes":"-----"}
////{"name":"UserDefinedTypeName","attributes":"name"}
////{"name":"FunctionCall","attributes":"type_conversion,type"}
////{"name":"IndexAccess","attributes":"type"}
////{"name":"Literal","attributes":"hexvalue,subdenomination,token,type,value"}
////{"name":"UnaryOperation","attributes":"prefix,type,operator"}
////{"name":"Mapping","attributes":"-----"}
////{"name":"WhileStatement","attributes":"-----"}
////{"name":"StructDefinition","attributes":"name"}
////{"name":"ParameterList","attributes":"-----"}
////{"name":"PragmaDirective","attributes":"literals"}
////{"name":"VariableDeclaration","attributes":"name,type"}
////{"name":"ElementaryTypeNameExpression","attributes":"type,value"}
////{"name":"ElementaryTypeName","attributes":"name"}
////{"name":"ContractDefinition","attributes":"fullyImplemented,linearizedBaseContracts,isLibrary,name"}
////{"name":"ForStatement","attributes":"-----"}
////{"name":"ArrayTypeName","attributes":"-----"}
////{"name":"Identifier","attributes":"type,value"}
////{"name":"Assignment","attributes":"type,operator"}
////{"name":"Block","attributes":"-----"}
////{"name":"BinaryOperation","attributes":"type,operator"}
////{"name":"Throw","attributes":"-----"}
////{"name":"ExpressionStatement","attributes":"-----"}
////{"name":"MemberAccess","attributes":"member_name,type"}
////{"name":"VariableDeclarationStatement","attributes":"-----"}
////
//   StructDefinition:name->"Voter"
//   ElementaryTypeName:name->"uint"
//   VariableDeclaration:name->"weight",type->"uint256"
//   ContractDefinition:name->"Ballot",fullyImplemented->true,isLibrary->false,linearizedBaseContracts->274
//   VariableDeclaration:name->"voters",type->"mapping(address => struct Ballot.Voter storage ref)"
//   VariableDeclaration:name->"proposals",type->"struct Ballot.Proposal storage ref[] storage ref"
//   
//- {"srcChars":23,"name":"PragmaDirective","ATTR_literals":".0","id":1,"srcSt":0}
//- {"ATTR_name":"Ballot","ATTR_fullyImplemented":"true","srcChars":4791,"ATTR_isLibrary":"false","name":"ContractDefinition","ATTR_linearizedBaseContracts":"274","id":274,"srcSt":60}
//-- {"ATTR_name":"Voter","srcChars":240,"name":"StructDefinition","id":10,"srcSt":210}
//--- {"ATTR_name":"weight","ATTR_type":"uint256","srcChars":11,"name":"VariableDeclaration","id":3,"srcSt":233}
//---- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":2,"srcSt":233}
//--- {"ATTR_name":"voted","ATTR_type":"bool","srcChars":10,"name":"VariableDeclaration","id":5,"srcSt":293}
//---- {"ATTR_name":"bool","srcChars":4,"name":"ElementaryTypeName","id":4,"srcSt":293}
//--- {"ATTR_name":"delegate","ATTR_type":"address","srcChars":16,"name":"VariableDeclaration","id":7,"srcSt":352}
//---- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":6,"srcSt":352}
//--- {"ATTR_name":"vote","ATTR_type":"uint256","srcChars":9,"name":"VariableDeclaration","id":9,"srcSt":401}
//---- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":8,"srcSt":401}
//-- {"ATTR_name":"Proposal","srcChars":137,"name":"StructDefinition","id":15,"srcSt":501}
//--- {"ATTR_name":"name","ATTR_type":"bytes32","srcChars":12,"name":"VariableDeclaration","id":12,"srcSt":531}
//---- {"ATTR_name":"bytes32","srcChars":7,"name":"ElementaryTypeName","id":11,"srcSt":531}
//--- {"ATTR_name":"voteCount","ATTR_type":"uint256","srcChars":14,"name":"VariableDeclaration","id":14,"srcSt":586}
//---- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":13,"srcSt":586}
//-- {"ATTR_name":"chairperson","ATTR_type":"address","srcChars":26,"name":"VariableDeclaration","id":17,"srcSt":644}
//--- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":16,"srcSt":644}
//
//-- {"ATTR_name":"voters","ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":39,"name":"VariableDeclaration","id":21,"srcSt":778}
//--- {"srcChars":25,"name":"Mapping","id":20,"srcSt":778}
//---- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":18,"srcSt":786}
//---- {"ATTR_name":"Voter","srcChars":5,"name":"UserDefinedTypeName","id":19,"srcSt":797}
////    mapping(address => Voter) public voters;
//
//
//-- {"ATTR_name":"proposals","ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":27,"name":"VariableDeclaration","id":24,"srcSt":880}
//--- {"srcChars":10,"name":"ArrayTypeName","id":23,"srcSt":880}
//---- {"ATTR_name":"Proposal","srcChars":8,"name":"UserDefinedTypeName","id":22,"srcSt":880}
////    Proposal[] public proposals;
//  
//-- {"ATTR_name":"Ballot","ATTR_payable":"false","ATTR_visibility":"public","srcChars":617,"name":"FunctionDefinition","id":67,"srcSt":976,"ATTR_constant":"false"}
//--- {"srcChars":25,"name":"ParameterList","id":28,"srcSt":991}
//---- {"ATTR_name":"proposalNames","ATTR_type":"bytes32[] memory","srcChars":23,"name":"VariableDeclaration","id":27,"srcSt":992}
//----- {"srcChars":9,"name":"ArrayTypeName","id":26,"srcSt":992}
//------ {"ATTR_name":"bytes32","srcChars":7,"name":"ElementaryTypeName","id":25,"srcSt":992}
//--- {"srcChars":0,"name":"ParameterList","id":29,"srcSt":1017}
//--- {"srcChars":576,"name":"Block","id":66,"srcSt":1017}
//---- {"srcChars":24,"name":"ExpressionStatement","id":34,"srcSt":1027}
//----- {"ATTR_type":"address","srcChars":24,"name":"Assignment","id":33,"srcSt":1027,"ATTR_operator":"="}
//------ {"ATTR_type":"address","srcChars":11,"ATTR_value":"chairperson","name":"Identifier","id":30,"srcSt":1027}
//------ {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":32,"srcSt":1041,"ATTR_member_name":"sender"}
//------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":31,"srcSt":1041}
//---- {"srcChars":30,"name":"ExpressionStatement","id":41,"srcSt":1061}
//----- {"ATTR_type":"uint256","srcChars":30,"name":"Assignment","id":40,"srcSt":1061,"ATTR_operator":"="}
//------ {"ATTR_type":"uint256","srcChars":26,"name":"MemberAccess","id":38,"srcSt":1061,"ATTR_member_name":"weight"}
//------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":19,"name":"IndexAccess","id":37,"srcSt":1061}
//-------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":35,"srcSt":1061}
//-------- {"ATTR_type":"address","srcChars":11,"ATTR_value":"chairperson","name":"Identifier","id":36,"srcSt":1068}
//------ {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 1","srcChars":1,"ATTR_value":"1","name":"Literal","id":39,"srcSt":1090,"ATTR_subdenomination":"31"}
//---- {"srcChars":346,"name":"ForStatement","id":65,"srcSt":1241}
//----- {"srcChars":10,"name":"VariableDeclarationStatement","id":45,"srcSt":1246}
//------ {"ATTR_name":"i","ATTR_type":"uint256","srcChars":6,"name":"VariableDeclaration","id":43,"srcSt":1246}
//------- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":42,"srcSt":1246}
//------ {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 0","srcChars":1,"ATTR_value":"0","name":"Literal","id":44,"srcSt":1255,"ATTR_subdenomination":"30"}
//----- {"ATTR_type":"bool","srcChars":24,"name":"BinaryOperation","id":49,"srcSt":1258,"ATTR_operator":"<"}
//------ {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"i","name":"Identifier","id":46,"srcSt":1258}
//------ {"ATTR_type":"uint256","srcChars":20,"name":"MemberAccess","id":48,"srcSt":1262,"ATTR_member_name":"length"}
//------- {"ATTR_type":"bytes32[] memory","srcChars":13,"ATTR_value":"proposalNames","name":"Identifier","id":47,"srcSt":1262}
//----- {"srcChars":3,"name":"ExpressionStatement","id":52,"srcSt":1284}
//------ {"ATTR_prefix":"false","ATTR_type":"uint256","srcChars":3,"name":"UnaryOperation","id":51,"srcSt":1284,"ATTR_operator":"++"}
//------- {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"i","name":"Identifier","id":50,"srcSt":1284}
//----- {"srcChars":298,"name":"Block","id":64,"srcSt":1289}
//------ {"srcChars":110,"name":"ExpressionStatement","id":63,"srcSt":1466}
//------- {"ATTR_type_conversion":"false","ATTR_type":"uint256","srcChars":110,"name":"FunctionCall","id":62,"srcSt":1466}
//-------- {"ATTR_type":"function (struct Ballot.Proposal storage ref) returns (uint256)","srcChars":14,"name":"MemberAccess","id":55,"srcSt":1466,"ATTR_member_name":"push"}
//--------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":53,"srcSt":1466}
//-------- {"ATTR_type_conversion":"false","ATTR_type":"struct Ballot.Proposal memory","srcChars":94,"name":"FunctionCall","id":61,"srcSt":1481}
//--------- {"ATTR_type":"type(struct Ballot.Proposal storage pointer)","srcChars":8,"ATTR_value":"Proposal","name":"Identifier","id":56,"srcSt":1481}
//--------- {"ATTR_type":"bytes32","srcChars":16,"name":"IndexAccess","id":59,"srcSt":1514}
//---------- {"ATTR_type":"bytes32[] memory","srcChars":13,"ATTR_value":"proposalNames","name":"Identifier","id":57,"srcSt":1514}
//---------- {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"i","name":"Identifier","id":58,"srcSt":1528}
//--------- {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 0","srcChars":1,"ATTR_value":"0","name":"Literal","id":60,"srcSt":1559,"ATTR_subdenomination":"30"}
//-- {"ATTR_name":"giveRightToVote","ATTR_payable":"false","ATTR_visibility":"public","srcChars":457,"name":"FunctionDefinition","id":92,"srcSt":1697,"ATTR_constant":"false"}
//--- {"srcChars":15,"name":"ParameterList","id":70,"srcSt":1721}
//---- {"ATTR_name":"voter","ATTR_type":"address","srcChars":13,"name":"VariableDeclaration","id":69,"srcSt":1722}
//----- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":68,"srcSt":1722}
//--- {"srcChars":0,"name":"ParameterList","id":71,"srcSt":1737}
//--- {"srcChars":417,"name":"Block","id":91,"srcSt":1737}
//---- {"srcChars":367,"name":"IfStatement","id":83,"srcSt":1747}
//----- {"ATTR_type":"bool","srcChars":48,"name":"BinaryOperation","id":80,"srcSt":1751,"ATTR_operator":"||"}
//------ {"ATTR_type":"bool","srcChars":25,"name":"BinaryOperation","id":75,"srcSt":1751,"ATTR_operator":"!="}
//------- {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":73,"srcSt":1751,"ATTR_member_name":"sender"}
//-------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":72,"srcSt":1751}
//------- {"ATTR_type":"address","srcChars":11,"ATTR_value":"chairperson","name":"Identifier","id":74,"srcSt":1765}
//------ {"ATTR_type":"bool","srcChars":19,"name":"MemberAccess","id":79,"srcSt":1780,"ATTR_member_name":"voted"}
//------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":13,"name":"IndexAccess","id":78,"srcSt":1780}
//-------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":76,"srcSt":1780}
//-------- {"ATTR_type":"address","srcChars":5,"ATTR_value":"voter","name":"Identifier","id":77,"srcSt":1787}
//----- {"srcChars":313,"name":"Block","id":82,"srcSt":1801}
//------ {"srcChars":5,"name":"Throw","id":81,"srcSt":2098}
//---- {"srcChars":24,"name":"ExpressionStatement","id":90,"srcSt":2123}
//----- {"ATTR_type":"uint256","srcChars":24,"name":"Assignment","id":89,"srcSt":2123,"ATTR_operator":"="}
//------ {"ATTR_type":"uint256","srcChars":20,"name":"MemberAccess","id":87,"srcSt":2123,"ATTR_member_name":"weight"}
//------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":13,"name":"IndexAccess","id":86,"srcSt":2123}
//-------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":84,"srcSt":2123}
//-------- {"ATTR_type":"address","srcChars":5,"ATTR_value":"voter","name":"Identifier","id":85,"srcSt":2130}
//------ {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 1","srcChars":1,"ATTR_value":"1","name":"Literal","id":88,"srcSt":2146,"ATTR_subdenomination":"31"}
//-- {"ATTR_name":"delegate","ATTR_payable":"false","ATTR_visibility":"public","srcChars":1394,"name":"FunctionDefinition","id":180,"srcSt":2206,"ATTR_constant":"false"}
//--- {"srcChars":12,"name":"ParameterList","id":95,"srcSt":2223}
//---- {"ATTR_name":"to","ATTR_type":"address","srcChars":10,"name":"VariableDeclaration","id":94,"srcSt":2224}
//----- {"ATTR_name":"address","srcChars":7,"name":"ElementaryTypeName","id":93,"srcSt":2224}
//--- {"srcChars":0,"name":"ParameterList","id":96,"srcSt":2236}
//--- {"srcChars":1364,"name":"Block","id":179,"srcSt":2236}
//---- {"srcChars":33,"name":"VariableDeclarationStatement","id":103,"srcSt":2275}
//----- {"ATTR_name":"sender","ATTR_type":"struct Ballot.Voter storage pointer","srcChars":12,"name":"VariableDeclaration","id":98,"srcSt":2275}
//------ {"ATTR_name":"Voter","srcChars":5,"name":"UserDefinedTypeName","id":97,"srcSt":2275}
//----- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":18,"name":"IndexAccess","id":102,"srcSt":2290}
//------ {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":99,"srcSt":2290}
//------ {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":101,"srcSt":2297,"ATTR_member_name":"sender"}
//------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":100,"srcSt":2297}
//---- {"srcChars":35,"name":"IfStatement","id":107,"srcSt":2318}
//----- {"ATTR_type":"bool","srcChars":12,"name":"MemberAccess","id":105,"srcSt":2322,"ATTR_member_name":"voted"}
//------ {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":104,"srcSt":2322}
//----- {"srcChars":5,"name":"Throw","id":106,"srcSt":2348}
//---- {"srcChars":162,"name":"WhileStatement","id":132,"srcSt":2772}
//----- {"ATTR_type":"bool","srcChars":82,"name":"BinaryOperation","id":123,"srcSt":2792,"ATTR_operator":"&&"}
//------ {"ATTR_type":"bool","srcChars":33,"name":"BinaryOperation","id":115,"srcSt":2792,"ATTR_operator":"!="}
//------- {"ATTR_type":"address","srcChars":19,"name":"MemberAccess","id":111,"srcSt":2792,"ATTR_member_name":"delegate"}
//-------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":10,"name":"IndexAccess","id":110,"srcSt":2792}
//--------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":108,"srcSt":2792}
//--------- {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":109,"srcSt":2799}
//------- {"ATTR_type_conversion":"true","ATTR_type":"address","srcChars":10,"name":"FunctionCall","id":114,"srcSt":2815}
//-------- {"ATTR_type":"type(address)","srcChars":7,"ATTR_value":"address","name":"ElementaryTypeNameExpression","id":112,"srcSt":2815}
//-------- {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 0","srcChars":1,"ATTR_value":"0","name":"Literal","id":113,"srcSt":2823,"ATTR_subdenomination":"30"}
//------ {"ATTR_type":"bool","srcChars":33,"name":"BinaryOperation","id":122,"srcSt":2841,"ATTR_operator":"!="}
//------- {"ATTR_type":"address","srcChars":19,"name":"MemberAccess","id":119,"srcSt":2841,"ATTR_member_name":"delegate"}
//-------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":10,"name":"IndexAccess","id":118,"srcSt":2841}
//--------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":116,"srcSt":2841}
//--------- {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":117,"srcSt":2848}
//------- {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":121,"srcSt":2864,"ATTR_member_name":"sender"}
//-------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":120,"srcSt":2864}
//----- {"srcChars":49,"name":"Block","id":131,"srcSt":2885}
//------ {"srcChars":24,"name":"ExpressionStatement","id":130,"srcSt":2899}
//------- {"ATTR_type":"address","srcChars":24,"name":"Assignment","id":129,"srcSt":2899,"ATTR_operator":"="}
//-------- {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":124,"srcSt":2899}
//-------- {"ATTR_type":"address","srcChars":19,"name":"MemberAccess","id":128,"srcSt":2904,"ATTR_member_name":"delegate"}
//--------- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":10,"name":"IndexAccess","id":127,"srcSt":2904}
//---------- {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":125,"srcSt":2904}
//---------- {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":126,"srcSt":2911}
//---- {"srcChars":52,"name":"IfStatement","id":139,"srcSt":3003}
//----- {"ATTR_type":"bool","srcChars":16,"name":"BinaryOperation","id":136,"srcSt":3007,"ATTR_operator":"=="}
//------ {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":133,"srcSt":3007}
//------ {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":135,"srcSt":3013,"ATTR_member_name":"sender"}
//------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":134,"srcSt":3013}
//----- {"srcChars":30,"name":"Block","id":138,"srcSt":3025}
//------ {"srcChars":5,"name":"Throw","id":137,"srcSt":3039}
//---- {"srcChars":19,"name":"ExpressionStatement","id":145,"srcSt":3159}
//----- {"ATTR_type":"bool","srcChars":19,"name":"Assignment","id":144,"srcSt":3159,"ATTR_operator":"="}
//------ {"ATTR_type":"bool","srcChars":12,"name":"MemberAccess","id":142,"srcSt":3159,"ATTR_member_name":"voted"}
//------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":140,"srcSt":3159}
//------ {"ATTR_hexvalue":"true","ATTR_type":"null","ATTR_token":"bool","srcChars":4,"ATTR_value":"true","name":"Literal","id":143,"srcSt":3174,"ATTR_subdenomination":"74727565"}
//---- {"srcChars":20,"name":"ExpressionStatement","id":151,"srcSt":3188}
//----- {"ATTR_type":"address","srcChars":20,"name":"Assignment","id":150,"srcSt":3188,"ATTR_operator":"="}
//------ {"ATTR_type":"address","srcChars":15,"name":"MemberAccess","id":148,"srcSt":3188,"ATTR_member_name":"delegate"}
//------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":146,"srcSt":3188}
//------ {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":149,"srcSt":3206}
//---- {"srcChars":27,"name":"VariableDeclarationStatement","id":157,"srcSt":3218}
//----- {"ATTR_name":"delegate","ATTR_type":"struct Ballot.Voter storage pointer","srcChars":14,"name":"VariableDeclaration","id":153,"srcSt":3218}
//------ {"ATTR_name":"Voter","srcChars":5,"name":"UserDefinedTypeName","id":152,"srcSt":3218}
//----- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":10,"name":"IndexAccess","id":156,"srcSt":3235}
//------ {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":154,"srcSt":3235}
//------ {"ATTR_type":"address","srcChars":2,"ATTR_value":"to","name":"Identifier","id":155,"srcSt":3242}
//---- {"srcChars":339,"name":"IfStatement","id":178,"srcSt":3255}
//----- {"ATTR_type":"bool","srcChars":14,"name":"MemberAccess","id":159,"srcSt":3259,"ATTR_member_name":"voted"}
//------ {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":8,"ATTR_value":"delegate","name":"Identifier","id":158,"srcSt":3259}
//----- {"srcChars":173,"name":"Block","id":169,"srcSt":3275}
//------ {"srcChars":51,"name":"ExpressionStatement","id":168,"srcSt":3386}
//------- {"ATTR_type":"uint256","srcChars":51,"name":"Assignment","id":167,"srcSt":3386,"ATTR_operator":"+="}
//-------- {"ATTR_type":"uint256","srcChars":34,"name":"MemberAccess","id":164,"srcSt":3386,"ATTR_member_name":"voteCount"}
//--------- {"ATTR_type":"struct Ballot.Proposal storage ref","srcChars":24,"name":"IndexAccess","id":163,"srcSt":3386}
//---------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":160,"srcSt":3386}
//---------- {"ATTR_type":"uint256","srcChars":13,"name":"MemberAccess","id":162,"srcSt":3396,"ATTR_member_name":"vote"}
//----------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":8,"ATTR_value":"delegate","name":"Identifier","id":161,"srcSt":3396}
//-------- {"ATTR_type":"uint256","srcChars":13,"name":"MemberAccess","id":166,"srcSt":3424,"ATTR_member_name":"weight"}
//--------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":165,"srcSt":3424}
//----- {"srcChars":140,"name":"Block","id":177,"srcSt":3454}
//------ {"srcChars":32,"name":"ExpressionStatement","id":176,"srcSt":3551}
//------- {"ATTR_type":"uint256","srcChars":32,"name":"Assignment","id":175,"srcSt":3551,"ATTR_operator":"+="}
//-------- {"ATTR_type":"uint256","srcChars":15,"name":"MemberAccess","id":172,"srcSt":3551,"ATTR_member_name":"weight"}
//--------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":8,"ATTR_value":"delegate","name":"Identifier","id":170,"srcSt":3551}
//-------- {"ATTR_type":"uint256","srcChars":13,"name":"MemberAccess","id":174,"srcSt":3570,"ATTR_member_name":"weight"}
//--------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":173,"srcSt":3570}
//-- {"ATTR_name":"vote","ATTR_payable":"false","ATTR_visibility":"public","srcChars":377,"name":"FunctionDefinition","id":217,"srcSt":3712,"ATTR_constant":"false"}
//--- {"srcChars":15,"name":"ParameterList","id":183,"srcSt":3725}
//---- {"ATTR_name":"proposal","ATTR_type":"uint256","srcChars":13,"name":"VariableDeclaration","id":182,"srcSt":3726}
//----- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":181,"srcSt":3726}
//--- {"srcChars":0,"name":"ParameterList","id":184,"srcSt":3741}
//--- {"srcChars":348,"name":"Block","id":216,"srcSt":3741}
//---- {"srcChars":33,"name":"VariableDeclarationStatement","id":191,"srcSt":3751}
//----- {"ATTR_name":"sender","ATTR_type":"struct Ballot.Voter storage pointer","srcChars":12,"name":"VariableDeclaration","id":186,"srcSt":3751}
//------ {"ATTR_name":"Voter","srcChars":5,"name":"UserDefinedTypeName","id":185,"srcSt":3751}
//----- {"ATTR_type":"struct Ballot.Voter storage ref","srcChars":18,"name":"IndexAccess","id":190,"srcSt":3766}
//------ {"ATTR_type":"mapping(address => struct Ballot.Voter storage ref)","srcChars":6,"ATTR_value":"voters","name":"Identifier","id":187,"srcSt":3766}
//------ {"ATTR_type":"address","srcChars":10,"name":"MemberAccess","id":189,"srcSt":3773,"ATTR_member_name":"sender"}
//------- {"ATTR_type":"msg","srcChars":3,"ATTR_value":"msg","name":"Identifier","id":188,"srcSt":3773}
//---- {"srcChars":35,"name":"IfStatement","id":195,"srcSt":3794}
//----- {"ATTR_type":"bool","srcChars":12,"name":"MemberAccess","id":193,"srcSt":3798,"ATTR_member_name":"voted"}
//------ {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":192,"srcSt":3798}
//----- {"srcChars":5,"name":"Throw","id":194,"srcSt":3824}
//---- {"srcChars":19,"name":"ExpressionStatement","id":201,"srcSt":3839}
//----- {"ATTR_type":"bool","srcChars":19,"name":"Assignment","id":200,"srcSt":3839,"ATTR_operator":"="}
//------ {"ATTR_type":"bool","srcChars":12,"name":"MemberAccess","id":198,"srcSt":3839,"ATTR_member_name":"voted"}
//------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":196,"srcSt":3839}
//------ {"ATTR_hexvalue":"true","ATTR_type":"null","ATTR_token":"bool","srcChars":4,"ATTR_value":"true","name":"Literal","id":199,"srcSt":3854,"ATTR_subdenomination":"74727565"}
//---- {"srcChars":22,"name":"ExpressionStatement","id":207,"srcSt":3868}
//----- {"ATTR_type":"uint256","srcChars":22,"name":"Assignment","id":206,"srcSt":3868,"ATTR_operator":"="}
//------ {"ATTR_type":"uint256","srcChars":11,"name":"MemberAccess","id":204,"srcSt":3868,"ATTR_member_name":"vote"}
//------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":202,"srcSt":3868}
//------ {"ATTR_type":"uint256","srcChars":8,"ATTR_value":"proposal","name":"Identifier","id":205,"srcSt":3882}
//---- {"srcChars":46,"name":"ExpressionStatement","id":215,"srcSt":4036}
//----- {"ATTR_type":"uint256","srcChars":46,"name":"Assignment","id":214,"srcSt":4036,"ATTR_operator":"+="}
//------ {"ATTR_type":"uint256","srcChars":29,"name":"MemberAccess","id":211,"srcSt":4036,"ATTR_member_name":"voteCount"}
//------- {"ATTR_type":"struct Ballot.Proposal storage ref","srcChars":19,"name":"IndexAccess","id":210,"srcSt":4036}
//-------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":208,"srcSt":4036}
//-------- {"ATTR_type":"uint256","srcChars":8,"ATTR_value":"proposal","name":"Identifier","id":209,"srcSt":4046}
//------ {"ATTR_type":"uint256","srcChars":13,"name":"MemberAccess","id":213,"srcSt":4069,"ATTR_member_name":"weight"}
//------- {"ATTR_type":"struct Ballot.Voter storage pointer","srcChars":6,"ATTR_value":"sender","name":"Identifier","id":212,"srcSt":4069}
//-- {"ATTR_name":"winningProposal","ATTR_payable":"false","ATTR_visibility":"public","srcChars":360,"name":"FunctionDefinition","id":259,"srcSt":4186,"ATTR_constant":"true"}
//--- {"srcChars":2,"name":"ParameterList","id":218,"srcSt":4210}
//--- {"srcChars":22,"name":"ParameterList","id":221,"srcSt":4242}
//---- {"ATTR_name":"winningProposal","ATTR_type":"uint256","srcChars":20,"name":"VariableDeclaration","id":220,"srcSt":4243}
//----- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":219,"srcSt":4243}
//--- {"srcChars":277,"name":"Block","id":258,"srcSt":4269}
//---- {"srcChars":25,"name":"VariableDeclarationStatement","id":225,"srcSt":4279}
//----- {"ATTR_name":"winningVoteCount","ATTR_type":"uint256","srcChars":21,"name":"VariableDeclaration","id":223,"srcSt":4279}
//------ {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":222,"srcSt":4279}
//----- {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 0","srcChars":1,"ATTR_value":"0","name":"Literal","id":224,"srcSt":4303,"ATTR_subdenomination":"30"}
//---- {"srcChars":226,"name":"ForStatement","id":257,"srcSt":4314}
//----- {"srcChars":10,"name":"VariableDeclarationStatement","id":229,"srcSt":4319}
//------ {"ATTR_name":"p","ATTR_type":"uint256","srcChars":6,"name":"VariableDeclaration","id":227,"srcSt":4319}
//------- {"ATTR_name":"uint","srcChars":4,"name":"ElementaryTypeName","id":226,"srcSt":4319}
//------ {"ATTR_hexvalue":"null","ATTR_type":"null","ATTR_token":"int_const 0","srcChars":1,"ATTR_value":"0","name":"Literal","id":228,"srcSt":4328,"ATTR_subdenomination":"30"}
//----- {"ATTR_type":"bool","srcChars":20,"name":"BinaryOperation","id":233,"srcSt":4331,"ATTR_operator":"<"}
//------ {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"p","name":"Identifier","id":230,"srcSt":4331}
//------ {"ATTR_type":"uint256","srcChars":16,"name":"MemberAccess","id":232,"srcSt":4335,"ATTR_member_name":"length"}
//------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":231,"srcSt":4335}
//----- {"srcChars":3,"name":"ExpressionStatement","id":236,"srcSt":4353}
//------ {"ATTR_prefix":"false","ATTR_type":"uint256","srcChars":3,"name":"UnaryOperation","id":235,"srcSt":4353,"ATTR_operator":"++"}
//------- {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"p","name":"Identifier","id":234,"srcSt":4353}
//----- {"srcChars":182,"name":"Block","id":256,"srcSt":4358}
//------ {"srcChars":158,"name":"IfStatement","id":255,"srcSt":4372}
//------- {"ATTR_type":"bool","srcChars":41,"name":"BinaryOperation","id":242,"srcSt":4376,"ATTR_operator":">"}
//-------- {"ATTR_type":"uint256","srcChars":22,"name":"MemberAccess","id":240,"srcSt":4376,"ATTR_member_name":"voteCount"}
//--------- {"ATTR_type":"struct Ballot.Proposal storage ref","srcChars":12,"name":"IndexAccess","id":239,"srcSt":4376}
//---------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":237,"srcSt":4376}
//---------- {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"p","name":"Identifier","id":238,"srcSt":4386}
//-------- {"ATTR_type":"uint256","srcChars":16,"ATTR_value":"winningVoteCount","name":"Identifier","id":241,"srcSt":4401}
//------- {"srcChars":111,"name":"Block","id":254,"srcSt":4419}
//-------- {"srcChars":41,"name":"ExpressionStatement","id":249,"srcSt":4437}
//--------- {"ATTR_type":"uint256","srcChars":41,"name":"Assignment","id":248,"srcSt":4437,"ATTR_operator":"="}
//---------- {"ATTR_type":"uint256","srcChars":16,"ATTR_value":"winningVoteCount","name":"Identifier","id":243,"srcSt":4437}
//---------- {"ATTR_type":"uint256","srcChars":22,"name":"MemberAccess","id":247,"srcSt":4456,"ATTR_member_name":"voteCount"}
//----------- {"ATTR_type":"struct Ballot.Proposal storage ref","srcChars":12,"name":"IndexAccess","id":246,"srcSt":4456}
//------------ {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":244,"srcSt":4456}
//------------ {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"p","name":"Identifier","id":245,"srcSt":4466}
//-------- {"srcChars":19,"name":"ExpressionStatement","id":253,"srcSt":4496}
//--------- {"ATTR_type":"uint256","srcChars":19,"name":"Assignment","id":252,"srcSt":4496,"ATTR_operator":"="}
//---------- {"ATTR_type":"uint256","srcChars":15,"ATTR_value":"winningProposal","name":"Identifier","id":250,"srcSt":4496}
//---------- {"ATTR_type":"uint256","srcChars":1,"ATTR_value":"p","name":"Identifier","id":251,"srcSt":4514}
//-- {"ATTR_name":"winnerName","ATTR_payable":"false","ATTR_visibility":"public","srcChars":139,"name":"FunctionDefinition","id":273,"srcSt":4710,"ATTR_constant":"true"}
//--- {"srcChars":2,"name":"ParameterList","id":260,"srcSt":4729}
//--- {"srcChars":20,"name":"ParameterList","id":263,"srcSt":4761}
//---- {"ATTR_name":"winnerName","ATTR_type":"bytes32","srcChars":18,"name":"VariableDeclaration","id":262,"srcSt":4762}
//----- {"ATTR_name":"bytes32","srcChars":7,"name":"ElementaryTypeName","id":261,"srcSt":4762}
//--- {"srcChars":63,"name":"Block","id":272,"srcSt":4786}
//---- {"srcChars":46,"name":"ExpressionStatement","id":271,"srcSt":4796}
//----- {"ATTR_type":"bytes32","srcChars":46,"name":"Assignment","id":270,"srcSt":4796,"ATTR_operator":"="}
//------ {"ATTR_type":"bytes32","srcChars":10,"ATTR_value":"winnerName","name":"Identifier","id":264,"srcSt":4796}
//------ {"ATTR_type":"bytes32","srcChars":33,"name":"MemberAccess","id":269,"srcSt":4809,"ATTR_member_name":"name"}
//------- {"ATTR_type":"struct Ballot.Proposal storage ref","srcChars":28,"name":"IndexAccess","id":268,"srcSt":4809}
//-------- {"ATTR_type":"struct Ballot.Proposal storage ref[] storage ref","srcChars":9,"ATTR_value":"proposals","name":"Identifier","id":265,"srcSt":4809}
//-------- {"ATTR_type_conversion":"false","ATTR_type":"uint256","srcChars":17,"name":"FunctionCall","id":267,"srcSt":4819}
//--------- {"ATTR_type":"function () constant returns (uint256)","srcChars":15,"ATTR_value":"winningProposal","name":"Identifier","id":266,"srcSt":4819}
//  
//  def xml1 = 
//    <JSON>
//      <children>
//        <src>0:23:-1</src>
//        <name>PragmaDirective</name>
//        <attributes>
//          <literals>solidity</literals>
//          <literals>^</literals>
//          <literals>0.4</literals>
//          <literals>.9</literals>
//        </attributes>
//        <id>1</id>
//      </children>
//      <children>
//        <children>
//          <children>
//            <src>42:7:-1</src>
//            <name>ElementaryTypeName</name>
//            <attributes>
//              <name>address</name>
//            </attributes>
//            <id>2</id>
//          </children>
//          <src>42:13:-1</src>
//          <name>VariableDeclaration</name>
//          <attributes>
//            <name>owner</name>
//            <type>address</type>
//          </attributes>
//          <id>3</id>
//        </children>
//        <children>
//          <children>
//            <src>72:2:-1</src>
//            <name>ParameterList</name>
//            <id>4</id>
//          </children>
//          <children>
//            <src>75:0:-1</src>
//            <name>ParameterList</name>
//            <id>5</id>
//          </children>
//          <children>
//            <children>
//              <children>
//                <children>
//                  <src>77:5:-1</src>
//                  <name>Identifier</name>
//                  <attributes>
//                    <type>address</type>
//                    <value>owner</value>
//                  </attributes>
//                  <id>6</id>
//                </children>
//                <children>
//                  <children>
//                    <src>85:3:-1</src>
//                    <name>Identifier</name>
//                    <attributes>
//                      <type>msg</type>
//                      <value>msg</value>
//                    </attributes>
//                    <id>7</id>
//                  </children>
//                  <src>85:10:-1</src>
//                  <name>MemberAccess</name>
//                  <attributes>
//                    <member_name>sender</member_name>
//                    <type>address</type>
//                  </attributes>
//                  <id>8</id>
//                </children>
//                <src>77:18:-1</src>
//                <name>Assignment</name>
//                <attributes>
//                  <type>address</type>
//                  <operator>=</operator>
//                </attributes>
//                <id>9</id>
//              </children>
//              <src>77:18:-1</src>
//              <name>ExpressionStatement</name>
//              <id>10</id>
//            </children>
//            <src>75:23:-1</src>
//            <name>Block</name>
//            <id>11</id>
//          </children>
//          <src>57:41:-1</src>
//          <name>FunctionDefinition</name>
//          <attributes>
//            <constant>false</constant>
//            <payable>false</payable>
//            <visibility>public</visibility>
//            <name>mortal</name>
//          </attributes>
//          <id>12</id>
//        </children>
//        <children>
//          <children>
//            <src>112:2:-1</src>
//            <name>ParameterList</name>
//            <id>13</id>
//          </children>
//          <children>
//            <src>115:0:-1</src>
//            <name>ParameterList</name>
//            <id>14</id>
//          </children>
//          <children>
//            <children>
//              <children>
//                <children>
//                  <children>
//                    <src>121:3:-1</src>
//                    <name>Identifier</name>
//                    <attributes>
//                      <type>msg</type>
//                      <value>msg</value>
//                    </attributes>
//                    <id>15</id>
//                  </children>
//                  <src>121:10:-1</src>
//                  <name>MemberAccess</name>
//                  <attributes>
//                    <member_name>sender</member_name>
//                    <type>address</type>
//                  </attributes>
//                  <id>16</id>
//                </children>
//                <children>
//                  <src>135:5:-1</src>
//                  <name>Identifier</name>
//                  <attributes>
//                    <type>address</type>
//                    <value>owner</value>
//                  </attributes>
//                  <id>17</id>
//                </children>
//                <src>121:19:-1</src>
//                <name>BinaryOperation</name>
//                <attributes>
//                  <type>bool</type>
//                  <operator>==</operator>
//                </attributes>
//                <id>18</id>
//              </children>
//              <children>
//                <children>
//                  <children>
//                    <src>142:7:-1</src>
//                    <name>Identifier</name>
//                    <attributes>
//                      <type>function (address)</type>
//                      <value>suicide</value>
//                    </attributes>
//                    <id>19</id>
//                  </children>
//                  <children>
//                    <src>150:5:-1</src>
//                    <name>Identifier</name>
//                    <attributes>
//                      <type>address</type>
//                      <value>owner</value>
//                    </attributes>
//                    <id>20</id>
//                  </children>
//                  <src>142:14:-1</src>
//                  <name>FunctionCall</name>
//                  <attributes>
//                    <type_conversion>false</type_conversion>
//                    <type>tuple()</type>
//                  </attributes>
//                  <id>21</id>
//                </children>
//                <src>142:14:-1</src>
//                <name>ExpressionStatement</name>
//                <id>22</id>
//              </children>
//              <src>117:39:-1</src>
//              <name>IfStatement</name>
//              <id>23</id>
//            </children>
//            <src>115:44:-1</src>
//            <name>Block</name>
//            <id>24</id>
//          </children>
//          <src>99:60:-1</src>
//          <name>FunctionDefinition</name>
//          <attributes>
//            <constant>false</constant>
//            <payable>false</payable>
//            <visibility>public</visibility>
//            <name>kill</name>
//          </attributes>
//          <id>25</id>
//        </children>
//        <src>24:137:-1</src>
//        <name>ContractDefinition</name>
//        <attributes>
//          <fullyImplemented>true</fullyImplemented>
//          <linearizedBaseContracts>26</linearizedBaseContracts>
//          <isLibrary>false</isLibrary>
//          <name>mortal</name>
//        </attributes>
//        <id>26</id>
//      </children>
//      <children>
//        <children>
//          <children>
//            <src>182:6:-1</src>
//            <name>UserDefinedTypeName</name>
//            <attributes>
//              <name>mortal</name>
//            </attributes>
//            <id>27</id>
//          </children>
//          <src>182:6:-1</src>
//          <name>InheritanceSpecifier</name>
//          <id>28</id>
//        </children>
//        <children>
//          <children>
//            <src>191:6:-1</src>
//            <name>ElementaryTypeName</name>
//            <attributes>
//              <name>string</name>
//            </attributes>
//            <id>29</id>
//          </children>
//          <src>191:15:-1</src>
//          <name>VariableDeclaration</name>
//          <attributes>
//            <name>greeting</name>
//            <type>string storage ref</type>
//          </attributes>
//          <id>30</id>
//        </children>
//        <children>
//          <children>
//            <children>
//              <children>
//                <src>225:6:-1</src>
//                <name>ElementaryTypeName</name>
//                <attributes>
//                  <name>string</name>
//                </attributes>
//                <id>31</id>
//              </children>
//              <src>225:16:-1</src>
//              <name>VariableDeclaration</name>
//              <attributes>
//                <name>_greeting</name>
//                <type>string memory</type>
//              </attributes>
//              <id>32</id>
//            </children>
//            <src>224:18:-1</src>
//            <name>ParameterList</name>
//            <id>33</id>
//          </children>
//          <children>
//            <src>250:0:-1</src>
//            <name>ParameterList</name>
//            <id>34</id>
//          </children>
//          <children>
//            <children>
//              <children>
//                <children>
//                  <src>252:8:-1</src>
//                  <name>Identifier</name>
//                  <attributes>
//                    <type>string storage ref</type>
//                    <value>greeting</value>
//                  </attributes>
//                  <id>35</id>
//                </children>
//                <children>
//                  <src>263:9:-1</src>
//                  <name>Identifier</name>
//                  <attributes>
//                    <type>string memory</type>
//                    <value>_greeting</value>
//                  </attributes>
//                  <id>36</id>
//                </children>
//                <src>252:20:-1</src>
//                <name>Assignment</name>
//                <attributes>
//                  <type>string storage ref</type>
//                  <operator>=</operator>
//                </attributes>
//                <id>37</id>
//              </children>
//              <src>252:20:-1</src>
//              <name>ExpressionStatement</name>
//              <id>38</id>
//            </children>
//            <src>250:25:-1</src>
//            <name>Block</name>
//            <id>39</id>
//          </children>
//          <src>208:67:-1</src>
//          <name>FunctionDefinition</name>
//          <attributes>
//            <constant>false</constant>
//            <payable>false</payable>
//            <visibility>public</visibility>
//            <name>greeter</name>
//          </attributes>
//          <id>40</id>
//        </children>
//        <children>
//          <children>
//            <src>290:2:-1</src>
//            <name>ParameterList</name>
//            <id>41</id>
//          </children>
//          <children>
//            <children>
//              <children>
//                <src>311:6:-1</src>
//                <name>ElementaryTypeName</name>
//                <attributes>
//                  <name>string</name>
//                </attributes>
//                <id>42</id>
//              </children>
//              <src>311:6:-1</src>
//              <name>VariableDeclaration</name>
//              <attributes>
//                <name/>
//                <type>string memory</type>
//              </attributes>
//              <id>43</id>
//            </children>
//            <src>310:8:-1</src>
//            <name>ParameterList</name>
//            <id>44</id>
//          </children>
//          <children>
//            <children>
//              <children>
//                <src>328:8:-1</src>
//                <name>Identifier</name>
//                <attributes>
//                  <type>string storage ref</type>
//                  <value>greeting</value>
//                </attributes>
//                <id>45</id>
//              </children>
//              <src>321:15:-1</src>
//              <name>Return</name>
//              <id>46</id>
//            </children>
//            <src>319:20:-1</src>
//            <name>Block</name>
//            <id>47</id>
//          </children>
//          <src>276:63:-1</src>
//          <name>FunctionDefinition</name>
//          <attributes>
//            <constant>true</constant>
//            <payable>false</payable>
//            <visibility>public</visibility>
//            <name>greet</name>
//          </attributes>
//          <id>48</id>
//        </children>
//        <src>162:179:-1</src>
//        <name>ContractDefinition</name>
//        <attributes>
//          <fullyImplemented>true</fullyImplemented>
//          <linearizedBaseContracts>49</linearizedBaseContracts>
//          <linearizedBaseContracts>26</linearizedBaseContracts>
//          <isLibrary>false</isLibrary>
//          <name>greeter</name>
//        </attributes>
//        <id>49</id>
//      </children>
//      <name>SourceUnit</name>
//    </JSON>
//
//}
*/
}
