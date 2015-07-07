%{
    #include "interpreter.h"

	#define GET_IDENT_ATTRIBUTE() strcpy((char *)malloc(yyleng), yytext)
	#define GET_NUM_ATTRIBUTE(BASE) strtol(yytext, 0, BASE)
    
	int com_line_start = 0;
%}

%option noyywrap
DELETE				[ \t]+
NEWLINE				\n

DEC					[0-9]+
IDS					[a-z]

ERR					.

COM_START			"/*"
COM_END				"*/"
COM_LINE			"//"

%x COM_FLAG
%x COM_FLAG_LINE

%%

{DELETE}			/* */
{NEWLINE}			line_cnt++;

{DEC}				return NUM; 	@{ 
										@NUM.value@ = GET_NUM_ATTRIBUTE(10);
										@NUM.line@ = line_cnt;
									@}

{IDS}				return IDENT;	@{ 
										@IDENT.name@ = GET_IDENT_ATTRIBUTE();
										@IDENT.line@ = line_cnt;
									@}

input				return INPUT;
output				return OUTPUT;

if					return IF;
while				return WHILE;   @{ 
										@WHILE.line@ = line_cnt;
									@}
                                    
assert				return ASSERT;

"<"					return LESS;
">"					return GREATER;

"="					return ASSIGN;
"=="				return EQUAL;
"!"					return NOT;
"+"					return PLUS;
"-"					return MINUS;

";"					return SEMIKOLON;
","					return KOMMA;

"("					return L_BRACE;
")"					return R_BRACE;
"{"					return LB_BRACE;
"}"					return RB_BRACE;

"["					return LP_BRACE;
"]"					return RP_BRACE;


{COM_START}			{
						BEGIN(COM_FLAG);
						com_line_start = line_cnt;
					}

{COM_LINE}			{
						BEGIN(COM_FLAG_LINE);
					}

<COM_FLAG_LINE>{NEWLINE}	{
                                BEGIN(INITIAL);
                                line_cnt++;
                            }

<COM_FLAG>{COM_END}	BEGIN(INITIAL);
<COM_FLAG>{NEWLINE}	line_cnt++;
<COM_FLAG>{ERR}		/* */
<COM_FLAG><<EOF>>	{
						fprintf(stderr, "%s: error: unclosed comment in file '%s' from line %d to line %d\n", 
							usage_name, active_file, com_line_start, line_cnt);
                        error_lex++;
						//exit(1);
					}

{ERR}				{
						fprintf(stderr, "%s: error: lexical error found in file '%s' in line %d: unknown character '%c'\n", 
							usage_name, active_file, line_cnt,  yytext[0]);
						error_lex++;
                        //exit(1);
					}


