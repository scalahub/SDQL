pragma solidity ^0.4.8;
contract RPS {
    address public house;
    uint public numTokens;
    uint public houseCommission;
    uint public commission = 50;
    mapping(address => uint) public tokenBalance;
    function getContractBalance() returns (uint) {
        return (this.balance);
    }
    function RPS(uint startTokens) {
        if (startTokens < 1000 ether) throw;
        if (playAmount < commission) throw;
        house = msg.sender;
        tokenBalance[house] = startTokens;
        numTokens = startTokens;
    }
    modifier notHouse() {
        if (msg.sender == house) throw;
        _;
    }
    modifier onlyHouse() {
        if (msg.sender != house) throw;
        _;
    }
    
    modifier noOverflow(address who, uint toAdd) {
        if (tokenBalance[who] + toAdd < tokenBalance[who]) throw;
        _;
    }
    event NewTokenPurchase(address buyer, uint amount);
    function getBalance(address user) returns (uint) {
        return tokenBalance[user];
    }
    function getHouseBal() returns(uint) {
        return (tokenBalance[house]);
    }
    function buyTokens() payable notHouse {
        if (msg.value == 0) throw;
        if (msg.value > tokenBalance[house]) throw;
        tokenBalance[msg.sender] += msg.value;
        tokenBalance[house] -= msg.value;
        NewTokenPurchase(msg.sender, msg.value);
    }
    function createTokens(uint howMany) onlyHouse noOverflow (msg.sender, howMany) {
        if (numTokens + howMany < numTokens) throw; // overflow
        tokenBalance[msg.sender] += howMany;
        numTokens += howMany;
    }
    function transferTokens(uint howMany, address receiver) notHouse {
        if (howMany == 0) throw;
        if (tokenBalance[msg.sender] < howMany) throw;
        tokenBalance[msg.sender] -= howMany;
        tokenBalance[receiver] += howMany;
    }    
    function withdrawEther(uint howMany, address receiver) notHouse {
        if (howMany == 0) throw;
        if (tokenBalance[msg.sender] < howMany) throw;
        tokenBalance[msg.sender] -= howMany;
        if (!receiver.send(howMany)) throw; // throw should automaticaly revert changes
    }
    function withdrawCommission(uint howMany, address receiver) onlyHouse {
        if (howMany == 0) throw;
        if (houseCommission < howMany) throw;
        houseCommission -= howMany;
        if (!receiver.send(howMany)) throw; // throw should automaticaly revert changes
    }
    enum GameStates { Open, FirstPlayerPlayed, SecondPlayerPlayed }
    enum PlayerChoices { Rock, Paper, Scissors }
    enum Outcomes {Player1Win, Player2Win, Draw }
    
    GameStates public currentState = GameStates.Open;
    uint public playAmount = 1000;
    bytes32 public firstPlayerEncryptedChoice;
    PlayerChoices public secondPlayerChoice;
    address public firstPlayer;
    address public secondPlayer;
    uint public secondPlayerTime;
    event GameOver(address player1, address player2, Outcomes outcome);
    event FirstPlayerPlayed(address firstPlayer, bytes32 encChoice);
    event SecondPlayerPlayed(address secondPlayer, PlayerChoices choice);
    event FirstPlayerOpened(address firstPlayer, PlayerChoices choice);
    
    function playAsFirstPlayer(bytes32 encChoice) notHouse {
        if (currentState == GameStates.SecondPlayerPlayed) forceSettle();
        if (currentState != GameStates.Open) throw;
        if (tokenBalance[msg.sender] < playAmount) throw;
        tokenBalance[msg.sender] -= playAmount;
        tokenBalance[house] += playAmount;
        firstPlayerEncryptedChoice = encChoice;
        firstPlayer = msg.sender;
        currentState = GameStates.FirstPlayerPlayed;
        
        FirstPlayerPlayed(msg.sender, encChoice);

    }
    
    function playAsSecondPlayer(PlayerChoices choice) notHouse {
        if (currentState != GameStates.FirstPlayerPlayed) throw;
        if (msg.sender == firstPlayer) throw;
        if (tokenBalance[msg.sender] < playAmount) throw;
        tokenBalance[msg.sender] -= playAmount;
        tokenBalance[house] += playAmount;
        secondPlayerChoice = choice;
        secondPlayer = msg.sender;
        currentState = GameStates.SecondPlayerPlayed;
        secondPlayerTime = now;

        SecondPlayerPlayed(msg.sender, choice);
        
    }
    string text;
    function revealFirstPlayerChoice(PlayerChoices choice, string randomness) {
        if (currentState != GameStates.SecondPlayerPlayed) throw;
        if (msg.sender != firstPlayer) throw;
        if (choice == PlayerChoices.Rock) { text = "rock"; }
        if (choice == PlayerChoices.Paper) { text = "paper"; }
        if (choice == PlayerChoices.Scissors) { text = "scissors"; }
        bytes32 computed = sha3(text, randomness);
        if (firstPlayerEncryptedChoice != computed) throw;
        currentState = GameStates.Open;
        FirstPlayerOpened(msg.sender, choice);
        settleGame(choice, secondPlayerChoice);
    }
    function forceSettle() {
        if (currentState != GameStates.SecondPlayerPlayed) throw;
        if (secondPlayerTime + 10 minutes > now) throw;
        currentState = GameStates.Open;
        settle(secondPlayer, playAmount * 2);
        GameOver(firstPlayer, secondPlayer, Outcomes.Player2Win);

    }
    function settle(address player, uint amount) internal {
        tokenBalance[player] += (amount - commission);
        tokenBalance[house] -= (amount - commission);
        houseCommission +- commission;
    }
    function settleGame(PlayerChoices first, PlayerChoices second) internal {
        if (first == second) {
            settle(firstPlayer, playAmount);
            settle(secondPlayer, playAmount);
            GameOver(firstPlayer, secondPlayer, Outcomes.Draw);
           // draw
        } else {
            if (
                (first == PlayerChoices.Rock && second == PlayerChoices.Scissors) ||
                (first == PlayerChoices.Scissors && second == PlayerChoices.Paper) ||
                (first == PlayerChoices.Paper && second == PlayerChoices.Rock) 
            ) {
                // player 1 wins
                settle(firstPlayer, playAmount * 2);
                GameOver(firstPlayer, secondPlayer, Outcomes.Player1Win);

            } else {
                // player 2 wins
                settle(secondPlayer, playAmount * 2);
                GameOver(firstPlayer, secondPlayer, Outcomes.Player2Win);
            }
        }
    }
    
    
}