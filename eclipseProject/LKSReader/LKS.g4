grammar LKS; 

@header {
package parser;
}

kripke  : section1
SECSEP NL
section2 
SECSEP NL 
section3
SECSEP NL
section4;
    
section1 : 
	  vertex_amount NL;
	  
section2 :
	  (adjacencyRow NL)+;
	  
section3 :
	  (predicateAssignmentRow NL)+;
	  
section4 :
	  (vertexMapping NL)+;
	
vertex_amount :
	  NUMBER;
	   
adjacencyRow :
	  NUMBER+;

predicateAssignmentRow :
	  NUMBER ASSIGNMENT listOfPredicates;

vertexMapping :
	  NUMBER ASSIGNMENT NUMBER
	  ;

listOfPredicates :
	  (LB predicate RB)+
	  ;
	  
predicate:
	  '!' LP predicate RP
	| BOOLVAL
	| VAR RELOP (VAR|NUMBER)
	| NUMBER RELOP VAR  
	;	

RELOP   :'<'|'>'|'==';
SPACE	: ' ' ->skip;
MATHOP	: ('+'|'-');
VAR		: [a-z];
SECSEP  : '--';
ASSIGNMENT: '->';
LP 		: '(';
RP 		: ')';
LB		: '{';
RB		: '}';
NL		: [\r\n];
BOOLVAL    : 'true'|'false';
NUMBER  : DIGIT+;
DIGIT 	: [0-9];
WS 		: [ \t]+ -> skip;
COMMENT: '//.*' -> skip;