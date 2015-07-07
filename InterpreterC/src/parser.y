%{
	#include "interpreter.h"
	
    struct node* root;
	
	void yyerror(const char *msg)
	{
		fprintf(stderr, "%s: error: %s found in file '%s' in line %d\n", usage_name, msg, active_file, line_cnt);
		error_parse++;
        //exit(2);
	}
	
	int yywarp()
	{
		return 1;
	}
%}

%token NUM
%token IDENT
%token INPUT OUTPUT
%token IF WHILE ASSERT
%token ASSIGN NOT LESS GREATER EQUAL PLUS MINUS
%token SEMIKOLON KOMMA L_BRACE R_BRACE LB_BRACE RB_BRACE

%token LP_BRACE RP_BRACE

%start root

@attributes {long value; unsigned int line;} NUM
@attributes {char* name; unsigned int line;} IDENT
@attributes {unsigned int line;} WHILE
@attributes {struct node* n;} program
@attributes {struct node* n;} declarations
@attributes {struct node* n;} statements 
@attributes {struct node* n;} statement 
@attributes {struct node* n;} paren_expr 
@attributes {struct node* n;} expr 
@attributes {struct node* n;} test 
@attributes {struct node* n;} sum 
@attributes {struct node* n;} term 
@attributes {struct node* n;} input 
@attributes {struct node* n;} output 
@attributes {struct node* n;} ids_in
@attributes {struct node* n;} ids_out

@attributes {struct node* n;} predicates

@traversal @lefttoright @postorder TRAVERSE


%%
/* ----------------------------------------------------------------------- */
root	
: program						
    @{  
        @TRAVERSE root = @program.n@; 
    @}
;

/* ----------------------------------------------------------------------- */
program
: declarations statements
    @{  
        @i @program.n@ = INTERNAL(NODE_PROGRAM,
                                  @declarations.n@,
                                  @statements.n@);
    @}


/* EXTENTION: PREDICATES CHECK AND ASPT FOR SYMBOLIC EXECUTION */
| LP_BRACE LB_BRACE predicates RB_BRACE RP_BRACE
    @{  
        @i @program.n@ = INTERNAL(NODE_PREDICATES,
                                  @predicates.n@,
                                  NULL);
    @}
;

predicates
: expr
    @{  
        @i @predicates.n@ = INTERNAL(NODE_TEST,
                                     @expr.n@,
                                     NULL);
    @}
| expr KOMMA predicates
    @{ 
        @i @predicates.0.n@ = INTERNAL( NODE_PREDICATES,
                                        @expr.n@,
                                        @predicates.1.n@);
    @}
;


/* ----------------------------------------------------------------------- */
declarations
: /* EMPTY */
    @{  
        @i @declarations.n@ = LEAF;
    @}
| input
    @{  
        @i @declarations.n@ = INTERNAL(NODE_DECLARATIONS,
                                       @input.n@,
                                       NULL);
    @}
| input output
    @{  
        @i @declarations.n@ = INTERNAL(NODE_DECLARATIONS,
                                       @input.n@,
                                       @output.n@);
    @}
| output
    @{  
        @i @declarations.n@ = INTERNAL(NODE_DECLARATIONS,
                                       NULL,
                                       @output.n@);
    @}
;

/* ----------------------------------------------------------------------- */
statements
: /* EMPTY */
    @{  
        @i @statements.n@ = LEAF;
    @}
| statement statements
    @{  
        @i @statements.0.n@ = INTERNAL(NODE_STATEMENTS,
                                       @statement.n@,
                                       @statements.1.n@);
    @}
;

/* ----------------------------------------------------------------------- */
statement
: IF paren_expr statement
    @{  
/*        @i @statement.0.n@ = INTERNAL(NODE_IF,
                                      @paren_expr.n@,
                                      @statement.1.n@);
*/                                      
        @i @statement.0.n@ = INTERNAL(NODE_IF,
                                      @paren_expr.n@,
                                      INTERNAL( NODE_STATEMENTS,
                                                @statement.1.n@,
                                                LEAF));
    @}
| WHILE paren_expr statement
    @{  
/*        @i @statement.0.n@ = INTERNAL(NODE_WHILE,
                                        @paren_expr.n@,
                                        @statement.1.n@);
*/
        @i @statement.0.n@ = WHILE_INTR(@paren_expr.n@,
                                        INTERNAL(   NODE_STATEMENTS,
                                                    @statement.1.n@,
                                                    LEAF),
                                        @WHILE.line@);
    @}
| LB_BRACE statements RB_BRACE
    @{  
        @i @statement.n@ = INTERNAL(NODE_STATEMENT,
                                    @statements.n@,
                                    NULL);
    @}
| expr SEMIKOLON
    @{  
        @i @statement.n@ = INTERNAL(NODE_STATEMENT,
                                    @expr.n@,
                                    NULL);
    @}
| ASSERT paren_expr SEMIKOLON
    @{  
        @i @statement.n@ = INTERNAL(NODE_ASSERT,
                                    @paren_expr.n@,
                                    NULL);
    @}
| SEMIKOLON
    @{  
        @i @statement.n@ = LEAF;
    @}
;

/* ----------------------------------------------------------------------- */
paren_expr
: L_BRACE expr R_BRACE
    @{  
        @i @paren_expr.n@ = INTERNAL(NODE_PARENEXPR,
                                     @expr.n@,
                                     NULL);
    @}
;

/* ----------------------------------------------------------------------- */
expr	
: test
    @{  
        @i @expr.n@ = INTERNAL(NODE_TEST,
                               @test.n@,
                               NULL);
    @}
| IDENT ASSIGN expr
    @{  
        @i @expr.0.n@ = INTERNAL(NODE_ASSIGN,
                                 @expr.1.n@,
                                 ID(@IDENT.name@, 
                                    @IDENT.line@));
    @}
    
/* | NOT expr */    /* THIS IS AN ERROR IN THE EXERCISE SHEET */
                    /* lower line is correct!!! */
| NOT paren_expr
    @{  
        @i @expr.n@ = INTERNAL(NODE_PARENEXPR_NOT,
                               @paren_expr.n@,
                               NULL);
    @}
;

/* ----------------------------------------------------------------------- */
test
: sum
    @{  
        @i @test.n@ = INTERNAL(NODE_SUM,
                               @sum.n@,
                               NULL);
    @}
| sum LESS sum
    @{  
        @i @test.n@ = INTERNAL(NODE_LESS,
                               @sum.0.n@,
                               @sum.1.n@);
    @}
| sum EQUAL sum
    @{  
        @i @test.n@ = INTERNAL(NODE_EQUAL,
                               @sum.0.n@,
                               @sum.1.n@);
    @}
/* EXTENSTION */
| sum GREATER sum
    @{  
        @i @test.n@ = INTERNAL(NODE_GREATER,
                               @sum.0.n@,
                               @sum.1.n@);
    @}
;

/* ----------------------------------------------------------------------- */
sum
: term
    @{  
        @i @sum.n@ = INTERNAL(NODE_TERM,
                              @term.n@,
                              NULL);
    @}
| sum PLUS term
    @{  
        @i @sum.0.n@ = INTERNAL(NODE_PLUS,
                                @sum.1.n@,
                                @term.n@);
    @}
| sum MINUS term
    @{  
        @i @sum.0.n@ = INTERNAL(NODE_MINUS,
                                @sum.1.n@,
                                @term.n@);
    @}
;

/* ----------------------------------------------------------------------- */
term
: IDENT
    @{  
        @i @term.n@ = ID(@IDENT.name@, 
                         @IDENT.line@);
    @}
| NUM
    @{  
        @i @term.n@ = INT(@NUM.value@, 
                          @NUM.line@);
    @}
| paren_expr
    @{  
        @i @term.n@ = INTERNAL(NODE_PARENEXPR,
                               @paren_expr.n@,
                               NULL);
    @}
;

/* ----------------------------------------------------------------------- */
input
: INPUT ids_in SEMIKOLON
    @{  
        @i @input.n@ = INTERNAL(NODE_INPUT,
                               @ids_in.n@,
                               NULL);
    @}
;

/* ----------------------------------------------------------------------- */
output
: OUTPUT ids_out SEMIKOLON
    @{  
        @i @output.n@ = INTERNAL(NODE_OUTPUT,
                                @ids_out.n@,
                                NULL);
    @}
;


/* ----------------------------------------------------------------------- */
ids_in
: IDENT
    @{  
        @i @ids_in.n@ = ID_IN(@IDENT.name@, 
                              @IDENT.line@);
    @}
| IDENT KOMMA ids_in
    @{  
        @i @ids_in.0.n@ = INTERNAL(NODE_INPUT,
                                   ID_IN(@IDENT.name@, 
                                         @IDENT.line@),
                                   @ids_in.1.n@);
    @}
;


/* ----------------------------------------------------------------------- */
ids_out
: IDENT
    @{  
        @i @ids_out.n@ = ID_OUT(@IDENT.name@, 
                                @IDENT.line@);
    @}
| IDENT KOMMA ids_out
    @{  
        @i @ids_out.0.n@ = INTERNAL(NODE_OUTPUT,
                                    ID_OUT(@IDENT.name@, 
                                           @IDENT.line@),
                                    @ids_out.1.n@);
    @}
;

