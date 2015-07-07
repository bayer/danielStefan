
#include "interpreter.h"

char* active_file       = NULL;
int line_cnt            = 0;

int error_lex           = 0;
int error_parse         = 0;
int error_sem           = 0;
int warning_sem         = 0;

int DEBUG               = FALSE;

char* usage_name        = NULL;
char* code_file         = NULL;

int step_flag           = FALSE;

long int run_loop_bound = 0;

int output_flag         = FALSE;
char* output_file       = NULL;
FILE* output_stream     = NULL;

int trace_flag          = FALSE;
char* trace_formula     = NULL;
FILE* trace_stream      = NULL;

int bound_flag          = FALSE;

int ast_flag            = FALSE;
int lts_print_flag      = FALSE;
int source_code_flag    = FALSE;

struct node* prog_root  = NULL;

struct option long_options[] =
{
    {"out",     required_argument,  0,  'o' },
    {"step",    no_argument,        0,  's' },
    {"trace",   required_argument,  0,  't' },
    {"bound",   no_argument,        0,  'b' },
    
    {"code",    no_argument,        0,  'c' },
    {"tree",    no_argument,        0,  'a' },
    {"lts",     no_argument,        0,  'l' },
    {"grammar", no_argument,        0,  'g' },
    
    {"debug",   no_argument,        0,  'd' },
    {"help",    no_argument,        0,  'h' },
    
    {0,0,0,0}
};

int long_option_length = 10;

const char* long_option_desc[10][2] =
{
    {"<file>",  "write labeled transition system to <file>"},
    {NULL,      "activate \"step through\"- or debugger-mode"},
    {"<file>",  "perform symbolic execution with trace <file>"},
    {NULL,      "use loop bounds from trace file"},
    
    {NULL,      "show the source code file"},
    {NULL,      "show the labeled abstract syntax parse-tree"},
    {NULL,      "show the labeled transition system"},
    {NULL,      "print the interpreters tinyC grammar in EBNF"},
    
    {NULL,      "print debug messages from the interpreter"},
    {NULL,      "print this help message"}
};


void synthesis_check(void);
void analysis_check(void);
void usage(void);
void intro(void);
void authors(void);
void tools(void);
void grammar(void);


int main(int argc, char *argv[]) 
{ 
    usage_name = argv[0];
	
    // argument check
	if(argc < 2)
	{
		usage();
        exit(-1);
	}
    
    code_file = argv[1];
    
    if(argc >= 2)
	{
        for(;;)
        {
            int option_index = 0;
            int c = getopt_long (argc, argv, "o:st:balcgdh", long_options, &option_index);
            
            
            if(c == -1)
            {
                break;
            }
            
            switch(c) 
            {
                case 'o':
                    output_flag = TRUE;
                    output_file = optarg;
                    break;
                    
                case 's':
                    step_flag = TRUE;
                    break;
                    
                case 't':
                    trace_flag = TRUE;
                    trace_formula = optarg;
                    break;
                
                case 'b':
                    bound_flag = TRUE;
                    break;    
                    
                    
                case 'c':
                    source_code_flag = TRUE;
                    break;
                
                case 'a':
                    ast_flag = TRUE;
                    break;   
                    
                case 'l':
                    lts_print_flag = TRUE;
                    break;
                    
                case 'g':
                    grammar();
                    exit(0);
                    break;    
                
                    
                case 'd':
                    DEBUG = 1;
                    break;
                    
                case 'h':
                    usage();
                    exit(0);
                    break;
                    
                default:
                    usage();
                    exit(-1);
                    break;
            }
        }
    }
 
    
    
    //TODO: move run_loop_bound to propper position
    run_loop_bound = 1234567;
    
    
    
	
    DBG( fprintf(stderr, "opening source code file %s", code_file) ); 

	if( (yyin = fopen(code_file, "r")) == NULL )
	{
		fprintf(stderr, "%s: error: could not open the source code file %s\n", usage_name, code_file);
        exit(-1);
	}
    
    active_file = code_file;
    line_cnt = 1;
    
    // try to open output file
    if( output_flag == TRUE )
    {
        DBG( fprintf(stderr, "opening output file %s", output_file) ); 
        
        if( (output_stream = fopen(output_file, "w")) == NULL )
        {
            fprintf(stderr, "%s: error: could not open/create the output file %s\n", usage_name, output_file);
            exit(-1);
        }
    }
    else
    {
        output_stream = NULL;
    }
    
    // try to open output file
    if( trace_flag == TRUE )
    {
        DBG( fprintf(stderr, "opening trace file %s", trace_formula) ); 
        
        if( (trace_stream = fopen(trace_formula, "r")) == NULL )
        {
            fprintf(stderr, "%s: error: could not open the trace file %s\n", usage_name, trace_formula);
            exit(-1);
        }
    }
    
    
    DBG( fprintf(stderr, "starting lexer and parser to generate abstract syntax parse-tree (AST)") ); 
    yyparse();
    fclose(yyin);
        
    if( ( error_lex + error_parse + error_sem ) == 0)
    {
        if( trace_flag == TRUE )
        {
            ks_init_symbolic_execution();
        }
        
        
        DBG( fprintf(stderr, "labelling the AST for LTS transformation") );
        traverse(root, 
                 TRAVERSE_POSTORDER, 
                 TRAVERSE_LEFT_TO_RIGHT, 
                 lts_labeling);
    }
    
    analysis_check();
    synthesis_check();
    
    fprintf(stdout, "\n");
    
    DBG( fprintf(stderr, "generating LTS from labeled AST") );
    traverse(root, 
             TRAVERSE_MIDORDER, 
             TRAVERSE_LEFT_TO_RIGHT, 
             lts_generator);
    
    DBG( fprintf(stderr, "ascii encoding of the LTS") );
    DBG( fprintf(stderr, "\n"); lts_printer(stderr, "", TRUE)   );
    
    
    prog_root = root;
    
    if( trace_flag == TRUE )
    {
        ks_generate();
        ks_sanity_check();
        ks_predicate_parse_and_ast_generation();
    }
    
    root = prog_root;
    
    lts_interpreting();
    
    free_node(root);
	return 0;
} 


void synthesis_check(void)
{
    if( root->left != NULL && root->left->type == NODE_LEAF )
    {
        DBG( fprintf(stderr, "no declarations found") );
    }
    
    if( root->right != NULL && root->right->type == NODE_LEAF  )
    {
        DBG( fprintf(stderr, "no statements found") );
    }
    
    if( root->left  != NULL && root->left->type     == NODE_LEAF && 
        root->right != NULL && root->right->type    == NODE_LEAF )
    {
        DBG( fprintf(stderr, "synthesis failed") );
        
        fprintf(stderr, "%s: there is no program to interpret in file %s\n\n", usage_name, code_file);
        exit(-1);
    }
    
    if( root->left  != NULL && root->left->type     == NODE_LEAF && 
       root->right != NULL && root->right->type    == NODE_LEAF )
    {
        DBG( fprintf(stderr, "synthesis failed") );
        
        fprintf(stderr, "%s: there is no program to interpret in file %s\n\n", usage_name, code_file);
        exit(-1);
    }
}


void analysis_check(void)
{
    int errors      = error_lex + error_parse + error_sem;
    int warnings    = warning_sem;
    
    if( warnings > 0 )
    {
        fprintf(stderr, "%s: %d total warnings(s) found", usage_name, warnings);
        
        if( warning_sem > 0 ) fprintf(stderr, ", %d semantic warning(s)", warning_sem);
        
        fprintf(stderr, "\n");
    }
    
    if( errors   > 0 )	
    {
        DBG( fprintf(stderr, "analysis failed") );
        
        fprintf(stderr, "%s: %d total error(s) found", usage_name, errors);
        
        if( error_lex   > 0 ) fprintf(stderr, ", %d lexical error(s)", 
                                     error_lex);
        if( error_parse > 0 ) fprintf(stderr, ", %d syntax error(s)", 
                                     error_parse);
        if( error_sem 	> 0 ) fprintf(stderr, ", %d semantic error(s)", error_sem);
        
        fprintf(stderr, "\n\n");
        exit(-1);
    }
    
    
    
    DBG( fprintf(stderr, "analysis succeeded") );
}


void usage(void)
{
    intro();
    
    fprintf(stderr, "USAGE:\n\tinterpreter <FILE> [OPTIONS]\n");
    fprintf(stderr, "\n");
    fprintf(stderr, "FILE:\n\tTinyC grammer source code file\n");
    fprintf(stderr, "\n");
    fprintf(stderr, "OPTIONS:\n");
    
    int i;
    
    for(i = 0; i < long_option_length; i++)
    {
        if( i > 0 && i % 4 == 0)
        {
            fprintf(stderr, "\t\n");
        }
        
        if( long_option_desc[i][0] != NULL )
        {
            fprintf(stderr, "\t-%c %s\t--%s %s\t   %s\n", 
                    long_options[i].val,
                    long_option_desc[i][0],
                    long_options[i].name,
                    long_option_desc[i][0],
                    long_option_desc[i][1]);
        }
        else
        {
            fprintf(stderr, "\t-%c\t\t--%-16s %s\n", 
                    long_options[i].val,
                    long_options[i].name,
                    long_option_desc[i][1]);
        }
    }
    
    fprintf(stderr, "\n");
    
//    tools();
    
    authors();
}

void intro(void)
{
    fprintf(stderr, "\n");   
    fprintf(stderr, "\t  _______           _____  ____     __                       __         \n");
    fprintf(stderr, "\t /_  __(_)__  __ __/ ___/ /  _/__  / /____ _______  _______ / /____ ____\n");
    fprintf(stderr, "\t  / / / / _ \\/ // / /__  _/ // _ \\/ __/ -_) __/ _ \\/ __/ -_) __/ -_) __/\n");
    fprintf(stderr, "\t /_/ /_/_//_/\\_, /\\___/ /___/_//_/\\__/\\__/_/ / .__/_/  \\__/\\__/\\__/_/\n");
    fprintf(stderr, "\t            /___/                           /_/                         \n");
    fprintf(stderr, "\n");
}

void authors(void)
{
    fprintf(stderr, "AUTHORS:\n");
    fprintf(stderr, "\tJürgen Maier         <e0825749@student.tuwien.ac.at>\n");
    fprintf(stderr, "\tPhilipp Paulweber    <e0727937@student.tuwien.ac.at>\n");
    fprintf(stderr, "\tStefan Mödlhamer     <e0825895@student.tuwien.ac.at>\n");
    fprintf(stderr, "\n");
}

//void tools(void)
//{
//  
//    fprintf(stderr, "TOOLS:\n");
//    fprintf(stderr, "\t%s\n", CC_VERSION_STRING);
//    fprintf(stderr, "\t%s, ", FLEX_VERSION_STRING);
//    fprintf(stderr, "%s, ", YACC_VERSION_STRING);
//    fprintf(stderr, "%s\n", OX_VERSION_STRING);
//    fprintf(stderr, "\t%s\n", msat_get_version());
//    fprintf(stderr, "\n");
//}

void grammar(void)
{
    intro();
    
    fprintf(stderr, "GRAMMAR:\n");
    
    fprintf(stderr, "\t<program> ::= <statement>\n");
    fprintf(stderr, "\t            | <input> <statement>\n");
    fprintf(stderr, "\t            | <output> <statement>\n");
    fprintf(stderr, "\t            | <input> <output> <statement>\n\n");
    
    fprintf(stderr, "\t<statement> ::= \"if\" <paren_expr> <statement>\n");
    fprintf(stderr, "\t              | \"while\" <paren_expr> <statement>\n");
    fprintf(stderr, "\t              | \"{\" { <statement> } \"}\"\n");
    fprintf(stderr, "\t              | <expr> \";\"\n");
    fprintf(stderr, "\t              | \"assert\" <paren_expr> \";\"\n");
    fprintf(stderr, "\t              | \";\"\n\n");
    
    fprintf(stderr, "\t<paren_expr> ::= \"(\" <expr> \")\"\n\n");
    
    fprintf(stderr, "\t<expr> ::= <test>\n");
    fprintf(stderr, "\t         | <id> \"=\" <expr>\n");
    fprintf(stderr, "\t         | \"!\" <paren_expr>\n\n");
    
    fprintf(stderr, "\t<test> ::= <sum>\n");
    fprintf(stderr, "\t         | <sum> \"==\" <sum>\n");
    fprintf(stderr, "\t         | <sum> \"<\" <sum>\n");
    fprintf(stderr, "\t         | <sum> \">\" <sum>  // TinyC extension\n\n");
    
    fprintf(stderr, "\t<sum> ::= <term>\n");
    fprintf(stderr, "\t        | <sum> \"+\" <term>\n");
    fprintf(stderr, "\t        | <sum> \"-\" <term>\n\n");
//    fprintf(stderr, "\t        | <sum> \"*\" <term>\n\n");
    
    fprintf(stderr, "\t<term> ::= <id>\n");
    fprintf(stderr, "\t         | <int>\n");
    fprintf(stderr, "\t         | <paren_expr>\n\n");
    
    fprintf(stderr, "\t<int> ::= <num><int>\n");
    fprintf(stderr, "\t        | <num>\n\n");
    
    fprintf(stderr, "\t<num> ::= \"0\" | \"1\" | \"2\" | ... | \"9\"\n\n");
    
    fprintf(stderr, "\t<id> ::= \"a\" | \"b\" | \"c\" | ... | \"z\"\n\n");
    
    fprintf(stderr, "\t<list_of_ids> ::= <id>\n");
    fprintf(stderr, "\t                | <id> \",\" <list_of_ids>\n\n");
    
    fprintf(stderr, "\t<input> ::= \"input\" <list_of_ids> \";\"\n\n");
    
    fprintf(stderr, "\t<output> ::= \"output\" <list_of_ids> \";\"\n\n");
}



