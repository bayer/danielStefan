grammar TinyC;
INT          	: [0-9]+ ;
WHITESPACE   	: ('\t'|' '|'\r'|'\n'|'\u000C')+    -> channel(HIDDEN) ;
ID		      	: [a-z] ;
COMMENT      	: '/*' .*? '*/' -> skip ;
LINE_COMMENT 	: '//' ~('\n'|'\r')* '\r'? '\n' -> skip ;

id				: ID ;
prog        	: statement*
            	| input statement*
            	| input output statement*
            	| output statement*
            	;
statement   	: ifStatement
            	| whileStatement
            	| '{' statement+ '}'
				| programExpr ';'
				| assertStatement
				| ';'
				;
programExpr		: expr ;
ifStatement 	: 'if' '(' expr ')' statement ;
whileStatement 	: 'while' '(' expr ')' statement ;
assertStatement : 'assert' '(' expr ')' ';' ;
expr        	: sum
				| test
            	| asgn
            	| notTest
            	;
notTest			: '!' test ;
asgn			: id '=' expr ; 
test			: sum
                | sum '<' sum
                | sum '==' sum
                ;
sum				: term
				| sum '+' term
				| sum '-' term
				;
integer			: INT ;
term			: id
			    | integer
			    | '(' expr ')'
			   	;
inputId			: id ;			   	
listOfInputIds	: inputId
				| inputId ',' listOfInputIds
				;
listOfIds		: id
				| id ',' listOfIds
				;
input			: 'input' listOfInputIds ';' ;
output			: 'output' listOfIds ';' ;