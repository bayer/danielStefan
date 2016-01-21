grammar LTL;            // Define a grammar called Hello

/***************************************************
****************************************************

//Grammar:
//	ltl ::= opd | ltl | ltl binop ltl | unop ltl
//
//Operands (opd):
//	true, false, user-defined names starting with a lower-case letter.
//
//Unary Operators (unop):
//	! 	the boolean operator for negation
//	X	The temporal operator 'next'
//
//Binary Operators (binop):
//	U 	the temporal operator 'until'
//	W	the temporal operator weak until
//	V 	the dual of U: (p V q) means !(!p U !q)
//	&&	the boolean operator for logical and
//	||	the boolean operator for logical or
//	/\	alternative form of &&
//	\/	alternative form of ||
//	->	the boolean operator for logical implication
//	==	the boolean operator for logical equivalence
* 
****************************************************
****************************************************/

@header {
package parser;
}


//The unary connectives (consisting of ¬ and the temporal
//connectives X, F and G) bind most tightly. Next in the order come U, R
//and W; then come ∧ and ∨; and after that comes →.

formula  : ALWAYS phi;  //(phi '\r'?'\n')*;       
phi : 
	  parens							#parenthesis
	//unary operators bind most tightly
	| unary								#unaryOp
	| phi (UNTIL|RELEASE|AND|OR|IMPLIES) phi 	#binaryOp //order: U,R,W,∧,∨,→
	| prop 								#proposition
	;
	
unary :
	  NOT phi
	| NEXT phi
	| FUTURE phi
	| GLOBAL phi
	;

parens : 
	LP phi RP //-> skip
	;
	  
prop : 	 
		(TRUE|FALSE)			#boolProp
		| INT 					#intProp
		| VAR					#varProp
		;

ALWAYS	: 'A'; 
TRUE 	: 'true';
FALSE 	: 'false';
NOT 	: '!';
AND 	: '&';
OR 		: '|';
IMPLIES : '->';
NEXT 	: 'X';
FUTURE 	: 'F';
GLOBAL 	: 'G';
UNTIL 	: 'U';
RELEASE : 'R';
LT 		: '<';
GT 		: '>';
EQ 		: '==';
VAR		: [a-z];
//VAR 	: [a-z][a-zA-Z0-9]*;
INT 	: [0-9]+;


LP 		: '(';
RP 		: ')';
CHAR 	: [a-zA-Z];
ALNUM 	: [a-zA-Z0-9];
DIGIT 	: [0-9];
WS 		: [ \t\r\n]+ -> skip;
