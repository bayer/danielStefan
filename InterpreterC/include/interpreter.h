
#ifndef INTERPRETER_H
#define INTERPRETER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <string.h>
#include <getopt.h>
#include <limits.h>
#include <errno.h>
#include <termios.h>
#include <assert.h>

#include "parser.h"
#include "table.h"
#include "node.h"
#include "lts.h"
#include "ks.h"

#include "mathsat.h"


#define TRUE    1
#define FALSE   0

#define UNICODE_BRACE_LEFT              "\u27E8"
#define UNICODE_BRACE_RIGHT             "\u27E9"

#define UNICODE_CHECK_MARK              "\u2713"
#define UNICODE_BALLOT                  "\u2717"

#define UNICODE_ARROW_ASSIGN            "\u21A6"
#define UNICODE_ARROW_DOWN              "\u21E9"
#define UNICODE_ARROW_RIGHT             "\u21FE"
#define UNICODE_ARROW_RIGHT_CONC        "\u21D2"

#define UNICODE_ARROW_TRANSITION_START  "\u2198"
#define UNICODE_ARROW_TRANSITION_END    "\u2199"

#define UNICODE_NOT                     "\u00AC"
#define UNICODE_AND                     "\u2227"

#define max(a,b)    ( a>=b ? a : b )

#define DBG(statements) if( DEBUG == 1 ) { fprintf(stderr, "%s: (DEBUG) ", usage_name); statements; fprintf(stderr, "\n"); }


#define NODE_LABEL_SIZE     256
#define TRACE_LABEL_SIZE    256

// INTERPRETER
extern int DEBUG;

extern char* usage_name;
extern char* code_file;

extern int step_flag;
extern long int run_loop_bound;

extern int output_flag;
extern char* output_file;
extern FILE* output_stream;

extern int trace_flag;
extern char* trace_formula;
extern FILE* trace_stream;

extern int bound_flag;

extern int lts_print_flag;
extern int source_code_flag;
extern int ast_flag;

extern char* active_file;
extern struct node* prog_root;

// LEXER
extern int error_lex;
extern int line_cnt;
extern int yylex(void);

// PARSER
extern int error_parse;
extern struct node *root;
extern FILE *yyin;
extern int yyparse(void);
extern void yyinit(void);

// SEMANTICS
extern int error_sem;
extern int warning_sem;

// LABELED TRANSITION SYSTEM
extern struct node ***lts_matrix;
extern void lts_generator(struct node* n);
extern void lts_labeling(struct node* n);

// KRIPKE STRUCTURE
extern struct trace* path;
extern struct loop_buffer* loops_from_tks;
extern msat_config  sym_cfg;
extern msat_env     sym_env;
extern msat_term    sym_formula;
extern msat_result  sym_status;
extern int          sym_res;

#endif /* INTERPRETER_H */


