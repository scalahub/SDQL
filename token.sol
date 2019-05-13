pragma solidity ^0.4.8;
contract Foo {}
contract Bar {}
library Search {
    function indexOf(uint[] storage self, uint value) view public returns (uint) {
        for (uint i = 0; i < self.length; i++)
            if (self[i] == value) return i;
        return uint(-1);
    }
}
contract Token is Foo, Bar {
    using Search for uint[];
    uint foozzz = 43;
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
    function deposit(bytes32 _id) public payable {
	// Any call to this function (even deeply nested) can
        // be detected from the JavaScript API by filtering
        // for `Deposit` to be called.
        emit Deposit(msg.sender, _id, msg.value);
    }
    
    address public owner;
    uint public total_supply;
    mapping (address => uint) public balances;
    string token_name;
    
    constructor(uint _total_supply, string _token_name) public {
        owner = msg.sender;
        total_supply = _total_supply;
        token_name = _token_name;
        balances[msg.sender] = total_supply;
    }
    modifier hasBalance(uint amount) {
        if (balances[msg.sender] < amount) revert();
        _;
    }
    //uint foo;
    modifier noOverflow(address receiver, uint amount) {
        // check for overflow
        if (balances[receiver] + amount <= balances[receiver]) revert();
        _;
    }
    
    modifier onlyOwner() {
        if (msg.sender != owner) revert();
        _;
    }
    event Transfer(address sender, address receiver, uint amount);
    
    function transfer(address receiver, uint amount)  
        hasBalance(amount) 
        noOverflow(receiver, amount) public
    {
        balances[msg.sender] -= amount;
	
	{
		balances[0x444] = 34;
	}
	//uint foo;
        balances[receiver] += amount;
        emit Transfer(msg.sender, receiver, amount);
    } 
    
    modifier noTotalSupplyOverflow(uint amount) {
        if (total_supply+amount <= total_supply) revert();
        _;
    }
    event Minted(address minter, uint amount);
    function mint(uint amount) 
        onlyOwner 
        noOverflow(msg.sender, amount) 
        noTotalSupplyOverflow(amount)
        public payable
    {
        balances[msg.sender] += amount;
        total_supply += amount;
        emit Minted(msg.sender, amount);
	for (uint same3 = 0; same3 < 1; same3++) {
		// loop test
		total_supply += 304;
		if (total_supply < 4005) {
			total_supply += 59595;
		} else {
			//uint foo;
			total_supply -= 595;
			while(total_supply > 49944) {
				//uint foo;
				total_supply -= 595;
				if (msg.sender == owner) break;
				if (msg.sender != 0x12345) continue;
				total_supply -= 595;				
				do {
					total_supply -= 5344;				
					if (tx.origin != 0x49595696) revert();
					//uint foo = 44;
				} while(total_supply != 4444);
			}
		}
        }

    }
    function getBalanceOfAddress(address address_to_lookup) public constant returns(uint balance) {
        balance = balances[address_to_lookup];
    }
    
    function getTotalSupply() public constant returns(uint) {return total_supply;}
    function testPayable(address receiver) public payable {
        uint amount = msg.value;
        if (!receiver.send(amount)) revert();
    }
}