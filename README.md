# SDQL
[![Build Status](https://travis-ci.org/scalahub/SDQL.svg?branch=master)](https://travis-ci.org/scalahub/SDQL)

Solidity DQL (SDQL) is a static analysis tool for the Solidity programming language.
 
It is built on top of the [DQL static analysis framework](https://github.com/scalahub/DQL). 

DQL is short for *Datalog Query Language*, and details can be found on the above link.

In order to use DQL for Solidity, we need to do the following:

1. Define the DQL basis (i.e., schema of the underlying fact tables). This is defined [here](https://github.com/scalahub/SDQL/blob/master/basis.txt).
2. Define the DQL rules for generating extended basis from reduces basis, as defined [here](https://github.com/scalahub/SDQL/blob/master/rules.txt) 
3. Populate the (reduced-basis) tables by converting a Solidity program to Datalog facts. This involves two steps
    * Convert the Solidity to an intermediate XML based language called XSolidity (done [here](https://github.com/scalahub/SDQL/blob/master/SolidityToDatalog/src/main/scala/solidity2datalog/sdql/SolidityToXSolidity.scala))
    * Convert the XSolidity to Datalog facts (done [here](https://github.com/scalahub/SDQL/blob/master/SolidityToDatalog/src/main/scala/solidity2datalog/sdql/XSolidityToDatalog.scala))
    
The Solidity to XSolidity compiler uses the AST generated by the solidity compiler of Ethereumj.

Currently this is the only documentation of SDQL, and the reader is referred to the source code for further details.  


 

