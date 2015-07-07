
#include "lts.h"

int lts_transition_counter;
int lts_state_vertex;
int lts_highest_to_request;

int lts_last_vertex_to_state = 0;

struct node ***lts_matrix;

struct table_element* idents = NULL;
struct table_element* result;

int execution_numeric_result;

char line_buffer[4096];

struct trace* path_formula = NULL;


void lts_execute_symbolic(struct node* n)
{    
    switch( (int)(n->type) ) 
    {   
        case NODE_VAR:
            n->ident->not_accessed = FALSE;
            
            sprintf(line_buffer, "%s%i", n->ident->name, n->ident->assign_cnt);
                        
            n->formula = msat_make_constant(sym_env, 
                                            msat_declare_function(sym_env, 
                                                                  line_buffer, 
                                                                  msat_get_integer_type(sym_env)));
            
            sprintf(n->label, "%s%i", n->ident->name, n->ident->assign_cnt);
            break;
            
        case NODE_INT:
            sprintf(line_buffer, "%li", n->value);
            
            n->formula = msat_make_number(sym_env, 
                                          line_buffer);
            
            sprintf(n->label, "%li", n->value);
            break;
            

            
        
        case NODE_STATEMENT:
            switch( (int)(n->left->type) )
            {
                case NODE_ASSIGN:
                case NODE_ASSERT:
                case NODE_EXPR:
                    n->formula = n->left->formula;
                
                    sprintf(n->label, "%s", n->left->label);
                    break;
            } 
            break;
            
        case NODE_STATEMENTS: 
            switch( (int)(n->left->type) )
        {
            case NODE_IF:
            case NODE_WHILE:
            case NODE_ASSERT:
                if( n->left->left->left->type != NODE_PARENEXPR_NOT)
                {
                    n->formula = msat_make_not(sym_env,
                                               n->left->left->formula);
                    
                    sprintf(n->label, "%s", n->left->left->label);
                }
                else
                {
                    n->formula = n->left->left->left->left->formula;
                    
                    sprintf(n->label, "%s", n->left->left->left->left->label);
                }
                break;
        }    
            break;
            
            
        case NODE_PARENEXPR_NOT:
            n->formula = msat_make_not(sym_env,
                                       n->left->formula);
            
            switch( (int)(n->left->left->type) )
            {
//                case NODE_TEST:
//                    switch( (int)(n->left->left->type) )
//                    {
//                        case NODE_SUM:
//                            sprintf(n->label, "-(%s)", 
//                                    n->left->left->left->label);
//                            break;
//                            
//                        case NODE_LESS:
//                            sprintf(n->label, "%s >= %s", 
//                                    n->left->left->left->label,
//                                    n->left->left->right->label);
//                            break;
//                            
//                        case NODE_EQUAL:
//                            sprintf(n->label, "%s != %s", 
//                                    n->left->left->left->label,
//                                    n->left->left->right->label);
//                            break;
//                            
//                        case NODE_GREATER:
//                            sprintf(n->label, "%s <= %s", 
//                                    n->left->left->left->label,
//                                    n->left->left->right->label);
//                            break;
//                            
//                    }
//                    break;
                    
                default:
                    sprintf(n->label, "%s(%s)", UNICODE_NOT, n->left->label);
                    break;
            }
            break;
        
        case NODE_PREDICATES:
            if( n->right == NULL )
            {
                n->formula = n->left->formula;
                sprintf(n->label, "%s", n->left->label);
            }
            else
            {
                n->formula = msat_make_and(sym_env,
                                           n->left->formula,
                                           n->right->formula);
                
                sprintf(n->label, "%s %s %s", n->left->label, UNICODE_AND, n->right->label);
            }
            break;    
            
        case NODE_PARENEXPR:  
        case NODE_ASSERT:
        case NODE_IF:               
        case NODE_WHILE:            
        case NODE_EXPR:
        case NODE_SUM:
        case NODE_TEST:
        case NODE_TERM:
            n->formula = n->left->formula;
            sprintf(n->label, "%s", n->left->label);
            break;
            
            
            
        case NODE_PLUS: 
            n->formula = msat_make_plus(sym_env,
                                        n->left->formula,
                                        n->right->formula);
            
            sprintf(n->label, "%s + %s", n->left->label, n->right->label);
            break;
            
        case NODE_MINUS:
            n->formula = msat_make_plus(sym_env,
                                        n->left->formula,
                                        msat_make_times(sym_env,
                                                        msat_make_number(sym_env, "-1"),
                                                        n->right->formula));
            
            sprintf(n->label, "%s - %s", n->left->label, n->right->label);
            break;
            
        case NODE_LESS:
            n->formula = msat_make_leq(sym_env,
                                       n->left->formula, 
                                       msat_make_plus(sym_env,
                                                      n->right->formula,
                                                      msat_make_number(sym_env, "-1")));
            
            sprintf(n->label, "%s < %s", n->left->label, n->right->label);
            break;
            
        case NODE_EQUAL:            
            n->formula = msat_make_equal(sym_env, 
                                         n->left->formula,
                                         n->right->formula);
            
            sprintf(n->label, "%s = %s", n->left->label, n->right->label);
            break;
            
        case NODE_GREATER:
            n->formula = msat_make_not(sym_env,
                                       msat_make_leq(sym_env,
                                                     n->left->formula, 
                                                     n->right->formula));
            
            sprintf(n->label, "%s > %s", n->left->label, n->right->label);
            break;
            
            
         
            
        case NODE_ASSIGN:
            if( n->right->ident->not_accessed == TRUE )
            {
                n->right->ident->not_accessed = FALSE;
            }
            else
            {
                n->right->ident->assign_cnt++;
                
                sprintf(line_buffer, "%s%i", n->right->ident->name, n->right->ident->assign_cnt);
                
                n->right->formula = msat_make_constant(sym_env, 
                                                       msat_declare_function(sym_env, 
                                                                             line_buffer, 
                                                                             msat_get_integer_type(sym_env)));
                
                sprintf(n->right->label, "%s%i", n->right->ident->name, n->right->ident->assign_cnt);
            }
            
            n->formula = msat_make_equal(sym_env, 
                                         n->right->formula,
                                         n->left->formula);
            
            sprintf(n->label, "%s = %s", n->right->label, n->left->label);
            break;
    }
}



void lts_execute_numeric(struct node* n)
{
    switch( (int)( n->type ) )
    {
//        case NODE_IF:
//        case NODE_WHILE:
//        case NODE_ASSERT:
//            n->value = (n->left->value > 0) ? TRUE : FALSE;
//            break;
        
        case NODE_ASSIGN: // write access                    
            n->value = n->left->value;
            
            n->right->ident->value = n->left->value;
//            ident[ n->right->ident_index ].value = n->left->value;
            break;    

        case NODE_STATEMENT:
            n->value = n->left->value;    
            
        case NODE_PARENEXPR:
            n->value = n->left->value;
            break;  
            
        case NODE_PARENEXPR_NOT:
            n->value = (n->left->value > 0) ? FALSE : TRUE;
            break;
            
            
            
        case NODE_TEST:
            n->value = n->left->value;
            break; 
            
        case NODE_LESS:
            n->value = (n->left->value < n->right->value) ? TRUE : FALSE;
            break;
            
        case NODE_EQUAL:
            n->value = (n->left->value == n->right->value) ? TRUE : FALSE;
            break;
            
        // EXTENSION
        case NODE_GREATER:
            n->value = (n->left->value > n->right->value) ? TRUE : FALSE;
            break;    
            
            
        case NODE_SUM:
            n->value = n->left->value;
            break;         
            
        case NODE_PLUS:
            n->value = n->left->value + n->right->value;
            
//            if ( ((si1^si2) | (((si1^(~(si1^si2) & INT_MIN)) + si2)^si2)) >= 0) {
//                /* handle error condition */
//            } else {
//                sum = si1 + si2;
//            }
            
//            if (((si2>0) && (si1 > (INT_MAX-si2)))
//                || ((si2<0) && (si1 < (INT_MIN-si2)))) {
//                /* handle error condition */
//            }
//            else {
//                sum = si1 + si2;
//            }
            break;
            
        case NODE_MINUS:
            n->value = n->left->value - n->right->value;
            
//            if (((si1^si2)
//                 & (((si1 ^ ((si1^si2)
//                             & (1 << (sizeof(int)*CHAR_BIT-1))))-si2)^si2)) < 0) {
//                /* handle error condition */
//            }
//            else {
//                result = si1 - si2;
//            }
            
//            if ((si2 > 0 && si1 < INT_MIN + si2) ||
//                (si2 < 0 && si1 > INT_MAX + si2)) {
//                /* handle error condition */
//            }
//            else {
//                result = si1 - si2;
//            }
            break;
        
            
            
        case NODE_TERM:
            n->value = n->left->value;
            break;
        
        case NODE_VAR: // read access
            n->value = n->ident->value;
//            n->value = ident[ n->ident_index ].value;
            break;
            
        case NODE_INT:
            // trivial state, value was assigned during lexing/parsing/ast-generation phase
            break;
    }
    
}

char trace_state_str[4096];

static void print_current_state(int state, FILE* stream)
{    
    if( step_flag == FALSE )
    {
        return;
    }
    
    trace_state_str[0] = '\0';
    sprintf(trace_state_str, "%s%i, (", UNICODE_BRACE_LEFT, state);
    
    
    result = idents;
    while( result != NULL )
    {
        sprintf(trace_state_str, "%s%s %s %li, ", 
                trace_state_str, 
                result->name, 
                UNICODE_ARROW_ASSIGN, 
                result->value );
        
        result = result->next;
    }
    
    sprintf(trace_state_str, "%s\b\b)%s", trace_state_str, UNICODE_BRACE_RIGHT);
    
    fprintf(stream, "\t%s\n", trace_state_str);
}

static int getch(void)
{
    static int ch = -1, fd = 0;
    struct termios neu, alt;
    
    fd = fileno(stdin);
    tcgetattr(fd, &alt);
    neu = alt;
    neu.c_lflag &= ~(ICANON|ECHO);
    
    tcsetattr(fd, TCSANOW, &neu);
    ch = getchar();
    tcsetattr(fd, TCSANOW, &alt);
    
    return ch;
}

static void print_current_transition(int state, int next_state, char* n, FILE* stream)
{
    int i;
    static int u = -1;
    static int b = -1;
    
    if( step_flag == FALSE )
    {
        return;
    }
    
    if( u < 0 && b < 0 )
    {
        u = strlen(trace_state_str) - 2;
        b = u/3;
    }
    
    fprintf(stream, "\t");
    
    for(i = 0; i < u; i++)
    {
        if( i < b )
        {
            fprintf(stream, " ");
        }
        else if( i == b && state >= 0 && next_state >= 0 )
        {
            fprintf(stream, "%s", UNICODE_ARROW_DOWN);
        }
        else if( i < u*4/5 )
        {
            fprintf(stream, " ");
        }
    }
    
    if( next_state == -1 )
    {
        fprintf(stream, "%i %s END", state, UNICODE_ARROW_RIGHT);
    }
    else if( next_state == -2 )
    {
        fprintf(stream, "%i %s ERROR: %s does not hold %s assert statement is violated!", 
                state, UNICODE_ARROW_RIGHT, n, UNICODE_ARROW_RIGHT_CONC);
    }
    else if( next_state == -3 )
    {
        fprintf(stream, "%i %s ABORT\n",
                state, UNICODE_ARROW_RIGHT);
        exit(-1);
    }
    else
    {
        fprintf(stream, "%i %s %i: %s ", state, UNICODE_ARROW_RIGHT, next_state, n);
        
        if( execution_numeric_result == TRUE )
        {
            fprintf(stream, "%s", UNICODE_CHECK_MARK);
        }
        else
        {
            fprintf(stream, "%s", UNICODE_BALLOT);
        }
    }
    
    fprintf(stream, "\n");
    
    if( step_flag == TRUE )
    {        
        for(;;)
        {
            switch( getch() )
            {
                case 'n':
                    return;
                    
                case 'a':
                    print_current_transition(state, -3, n, stream);
                    return;
            }
        }
    }
    
    
}



static void print_current_state_symbolic(struct trace* p, FILE* stream)
{
    if( step_flag == FALSE )
    {
        return;
    }
    
    if( p->ks_state == -1 && p->lts_state == -1 )
    {
        fprintf(stream, "\t%sEND, {", UNICODE_BRACE_LEFT);
    }
    else
    {
        fprintf(stream, "\t%s%i | %i, {", UNICODE_BRACE_LEFT, p->ks_state, p->lts_state);
    }
        
    sprintf(line_buffer, " %s ", UNICODE_AND);
    trace_print_labels_with_format(path_formula, stream, "", line_buffer, "");
    
    fprintf(stream, "}%s ", UNICODE_BRACE_RIGHT);
}

static struct trace* print_current_transition_symbolic(struct trace* p, FILE* stream, int flag)
{
    struct trace* back;
    
    struct trace* next;
    
    if( step_flag == FALSE )
    {
        if( p->loop != NULL && p->loop->loop_cnt > 0 )
        {
            p->loop->loop_cnt--;
            return p->loop->next;
        }
        else
        {
            return p->next;
        }
    }
    
    
    fprintf(stream, "\t\t%s\n", UNICODE_ARROW_DOWN);
    
    
    if( flag >= 0 )
    {
        next = p->next;
        
        fprintf(stream, "(n)\t\t%s\t", UNICODE_ARROW_DOWN);
        
        if( next->ks_state == -1 && next->lts_state == -1 )
        {
            fprintf(stream, "(%i | %i/%s) %s END", 
                    p->ks_state, 
                    p->lts_state, 
                    p->label,
                    UNICODE_ARROW_RIGHT);
        }
        else
        {
            fprintf(stream, "(%i | %i/%s) %s (%i | %i/%s)  :  %s",
                    p->ks_state, 
                    p->lts_state, 
                    p->label,
                    UNICODE_ARROW_RIGHT,
                    next->ks_state, 
                    next->lts_state, 
                    next->label,
                    lts_matrix[p->lts_state][next->lts_state]->label);
        }
        
        next = p->loop;
        
        if( next != NULL )
        {
            next = next->next;
            
            fprintf(stream, "\n(l)\t\t%s\t", UNICODE_ARROW_DOWN);
            
            fprintf(stream, "(%i | %i/%s) %s (%i | %i/%s)  :  %s",
                    p->ks_state, 
                    p->lts_state, 
                    p->label,
                    UNICODE_ARROW_RIGHT,
                    next->ks_state, 
                    next->lts_state, 
                    next->label,
                    lts_matrix[p->lts_state][next->lts_state]->label);
        }
    }
    else
    {
        fprintf(stream, "\t\t%s\t(%i | %i/%s) %s ABORT\n\n", 
                UNICODE_ARROW_DOWN,
                p->ks_state, 
                p->lts_state, 
                p->label,
                UNICODE_ARROW_RIGHT);
        exit(-1);
    }
    
    
    fprintf(stream, "\n");
    
    char g;
    
    for(;;)
    {
        g = getch();
        
        if( g == 'n' )
        {
            back = p->next;
            break;
        }
        if( g == 'l' && p->loop != NULL )
        {
            back = p->loop->next;
            break;
        }
        else if( g == 'a' )
        {
            print_current_transition_symbolic(p, stream, -1);
        }
    }
    
    fprintf(stream, "\t\t%s\n", UNICODE_ARROW_DOWN);
    
    return back;
}


void lts_interpreting(void)
{
    int state = 0;
    int next_state = 0;
    char* last_transition;
    
    
    if( output_flag == TRUE )
    {
        DBG( fprintf(stderr, "writing LTS to output file %s", output_file) );
        lts_printer(output_stream, "", TRUE);
        exit(0);
    }
    
    char c;
    
    // write source code file to console
    if( source_code_flag == TRUE )
    {
        if( (yyin = fopen(code_file, "r")) == NULL )
        {
            fprintf(stderr, "%s: error: could not open the source code file %s\n", usage_name, code_file);
            exit(-1);
        }
        
        fseek(yyin, 0, 0);
        
        fprintf(stdout, "Source code:\n");
        
        fprintf(stdout, "\t");
        while( ( c = fgetc(yyin) ) != EOF )
        {
            fprintf(stdout, "%c", c);
            
            if( c == '\n' )
            {
                fprintf(stdout, "\t");
            }
        }
        fprintf(stdout, "\n");
    }
    
    if( ast_flag == TRUE )
    {
        fprintf(stdout, "Labeled abstract syntax parse-tree:\n");
        print_ascii_tree(root, stdout);
        fprintf(stdout, "\n");
    }
    
    if( lts_print_flag == TRUE )
    {
        fprintf(stdout, "Labeled transition system:\n");
        lts_printer(stdout, "\t", FALSE);
        fprintf(stdout, "\n");
    }
    
    int check = EOF;
    int none = TRUE;
    
    
    
    
    switch( (int) trace_flag )
    {
        // perform symbolic execution
        case TRUE:
        {
            //char path_formula[4096]; //TODO: old
            struct trace* ptr = path;
            msat_term f;
            
            char* last_predicate_label = NULL;
            
            
            DBG( fprintf(stderr, "starting symbolic execution") );
            fprintf(stdout, "Abstract trace:\n\tkripke\tLTS\ttinyC\n\tstates\tstates\tpredicate(s)\n");
            
            while( ptr->next != NULL )
            {
                fprintf(stdout, "\t");
                
                fprintf(stderr, "\t%i\t%i\t%s\n", 
                        ptr->ks_state,
                        ptr->lts_state,
                        ptr->label);
                
                ptr = ptr->next;
            }
            
            fprintf(stdout, "\n");
            
            
            

            // abstract trace loop detection
            struct trace *iter = path;
            struct trace *loop = NULL;
            int loop_detection_flag = FALSE;
            
            while( iter != NULL )
            {
                loop = trace_search_loop(iter);
                
                
                if( loop != NULL )
                {
                    if( step_flag == FALSE )
                    {
                        if( bound_flag == FALSE )
                        {
                            if( loop_detection_flag == FALSE )
                            {
                                loop_detection_flag = TRUE;
                                
                                fprintf(stdout, "Upper bound for kripke state loop(s):\n");
                            }
                            
                            fprintf(stdout, "\t[... %s ", UNICODE_ARROW_RIGHT);
                            
                            ptr = iter->loop;
                            
                            for(;;)
                            {
                                fprintf(stdout, "%i %s ", ptr->ks_state, UNICODE_ARROW_RIGHT);
                                
                                if( ptr->next == iter )
                                {
                                    fprintf(stdout, "%i %s ...]: ", ptr->next->ks_state,UNICODE_ARROW_RIGHT);
                                    break;
                                }
                                
                                ptr = ptr->next;
                            }
                            
                            fflush(stdin);
                            check = fscanf(stdin, "%i", &iter->loop->loop_cnt);
                            
                            
                            if( check == 0 )
                            {
                                fprintf(stderr, "%s: error: not a valid upper loop bound\n", usage_name);
                                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                                exit(-1);
                            }
                            
                            if( iter->loop->loop_cnt < 0 )
                            {
                                fprintf(stderr, "%s: error: negative upper loop bound is not allowed\n", 
                                        usage_name);
                                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                                exit(-1);
                            }
                        }
                        else
                        {
                            DBG( fprintf(stderr, "use upper loop bounds from tks or fill up with zeros:") );
                            if( loops_from_tks != NULL )
                            {                               
                                iter->loop->loop_cnt = loops_from_tks->bound;
                                loops_from_tks = loops_from_tks->next;
                            }
                            
                            DBG( fprintf(stderr, "for loop node %i = %i", iter->ks_state, iter->loop->loop_cnt) );
                        }
                    }
                }
                
                iter = iter->next;
            }
            
            fprintf(stdout, "\n");
            
            
            
            ptr = path;
            
            if( ptr == NULL )
            {
                fprintf(stderr, "%s: error: no abstract path was found\n", usage_name);
                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                exit(-1);
            }
            
            
            
            ptr = path;
            
            fprintf(stdout, "Symbolic execution trace:\n");
            
            if( step_flag == TRUE )
            {
                fprintf(stdout, "(press 'n' for next transition, 'l' to perform a loop, 'a' to abort)\n\n");
            }
            
            last_predicate_label = ptr->label;
            
            DBG( fprintf(stderr, "calculating formula of '%s'", 
                         ptr->label) );
            // calc pre-condition
            traverse(ptr->ast, 
                     TRAVERSE_POSTORDER, 
                     TRAVERSE_LEFT_TO_RIGHT, 
                     lts_execute_symbolic);
            
            f = ptr->ast->formula;
            
            DBG( fprintf(stderr, "f = %s (%s)", msat_term_repr(ptr->ast->formula), ptr->ast->label) );
            
            DBG( fprintf(stderr, "\n"); 
                print_ascii_tree(ptr->ast, stderr);
                fprintf(stderr, "\n") );
            //sprintf(path_formula, "\t%s", ptr->ast->label);
            path_formula = trace_new_state(ptr->ast->label, 0, 0);
            
//            print_current_state_symbolic(ptr, stdout);
            
            if( MSAT_ERROR_TERM(f) )
            {
                fprintf(stderr, "%s: error: symbolic path formula does not conform to a MathSAT term\n", 
                        usage_name);
                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                exit(-1);
            }
            
            
            msat_reset_env(sym_env);
            sym_res = msat_assert_formula(sym_env, f);
            
            if( sym_res != 0 )
            {
                fprintf(stderr, "%s: error: SAT solver does not accept the symbolic path formula\n", 
                        usage_name);
                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                exit(-1);
            }
            
            
            sym_status = msat_solve(sym_env);
            
//            print_current_transition_symbolic(ptr, stdout, 0);
//            ptr = ptr->next;
            print_current_state_symbolic(ptr, stdout);
            
            if( sym_status == MSAT_SAT )
            {
                if( step_flag == TRUE )
                {
                    fprintf(stdout, "%s SAT\n", UNICODE_ARROW_RIGHT_CONC);                        
                }
            }
            else if( sym_status == MSAT_UNSAT )
            {
                if( step_flag == TRUE )
                {
                    fprintf(stdout, "%s UNSAT\n\n", UNICODE_ARROW_RIGHT_CONC);
                    break;
                }
            }
            else
            {
                fprintf(stderr, "%s: error: SAT solver could not interpret the path formula\n", 
                        usage_name);
                fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                exit(-1);
            }
            
            
            struct trace* pre  = NULL;
            struct trace* post = NULL;
            
            int ks_trace_counter = -1;
            
            
            
            
            
            
            while( ptr->next != NULL )
            {
                pre = ptr;
                
                
                
                ptr = print_current_transition_symbolic(ptr, stdout, 0);
                //ptr = ptr->next;
                
                post = ptr;
                
                
                //-------------
                // transition
                if( pre->lts_state < post->lts_state && post->lts_state >= 0 )
                {
                    switch( (int)(lts_matrix[ pre->lts_state ][ post->lts_state ]->type) )
                    {
                        case NODE_WHILE:
                        case NODE_IF:
                        case NODE_ASSERT:
                        case NODE_STATEMENT:
                            DBG( fprintf(stderr, "calculating formula of '%s'", 
                                         lts_matrix[ pre->lts_state ][ post->lts_state ]->label) );
                            
                            traverse(lts_matrix[ pre->lts_state ][ post->lts_state ]->left, 
                                     TRAVERSE_POSTORDER, 
                                     TRAVERSE_LEFT_TO_RIGHT, 
                                     lts_execute_symbolic);
                            
                            f = msat_make_and(sym_env,
                                              f,
                                              lts_matrix[ pre->lts_state ][ post->lts_state ]->left->formula);
                            
                            DBG( fprintf(stderr, "f = %s (%s)", 
                                         msat_term_repr(lts_matrix[ pre->lts_state ][ post->lts_state ]->left->formula),
                                         lts_matrix[ pre->lts_state ][ post->lts_state ]->left->label));
                            
                            DBG( fprintf(stderr, "TRANSITION\n"); 
                                print_ascii_tree(lts_matrix[ pre->lts_state ][ post->lts_state ]->left, 
                                                 stderr);
                                fprintf(stderr, "\n") );
                            

                            if( trace_search_label(path_formula, 
                                    lts_matrix[ pre->lts_state ][ post->lts_state ]->left->label) == NULL )
                            {
                                path_formula = trace_add_state(path_formula, 
                                                               trace_new_state(lts_matrix[ pre->lts_state ][ post->lts_state ]->left->label, 0, 0));
                            }
                            
                            break;
                            
                        case NODE_STATEMENTS:
                            if( strlen(lts_matrix[ pre->lts_state ][ post->lts_state ]->label) > 0 )
                            {
                                DBG( fprintf(stderr, "calculating formula of '%s'", 
                                             lts_matrix[ pre->lts_state ][ post->lts_state ]->label) );
                                
                                traverse(lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left, 
                                         TRAVERSE_POSTORDER, 
                                         TRAVERSE_LEFT_TO_RIGHT, 
                                         lts_execute_symbolic);
                                
                                f = msat_make_and(sym_env,
                                                  f,
                                                  msat_make_not(sym_env,
                                                                lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left->formula));
                                
                                char buffer[2048];
                                
                                sprintf(buffer, "%s(%s)", 
                                        UNICODE_NOT, 
                                        lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left->label);
                                
                                
                                DBG( fprintf(stderr, "f = %s", msat_term_repr(lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left->formula)));
                                
                                DBG( fprintf(stderr, "\n"); 
                                    print_ascii_tree(lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left, stderr);
                                    fprintf(stderr, "\n") );
                                
                                if( trace_search_label(path_formula, 
                                                       lts_matrix[ pre->lts_state ][ post->lts_state ]->left->left->label) == NULL )
                                {
                                    path_formula = trace_add_state(path_formula, 
                                                                   trace_new_state(buffer, 0, 0));
                                }
                            }
                            break;
                    }     
                }
                
                if( step_flag == FALSE )
                {
                    if( ks_trace_counter == -1 )
                    {
                        ks_trace_counter = 0;
                        
                        fprintf(stdout, "\t");
                    }
                    
                    fprintf(stdout, "%i %s ", pre->ks_state, UNICODE_ARROW_RIGHT);
                    
                    ks_trace_counter++;
                    
                    if((ks_trace_counter % 10) == 0 && ks_trace_counter > 0)
                    {
                        ks_trace_counter = 0;
                        fprintf(stdout, "\n\t");
                    }
                }
                
                
                
                // post-predicate
                if( (post->next != NULL && post->next->ks_state != -1) || (post->next == NULL) )
                {
                    if(post->next == NULL)
                    {
                        post = pre;
                    }
                    
                    DBG( fprintf(stderr, "calculating formula of '%s'", post->label) );
                    
                    // calc post-condition
                    traverse(post->ast, 
                             TRAVERSE_POSTORDER, 
                             TRAVERSE_LEFT_TO_RIGHT, 
                             lts_execute_symbolic);
                    
                    f = msat_make_and(sym_env,
                                      f,
                                      post->ast->formula);
                    
                    DBG( fprintf(stderr, "f = %s (%s)", msat_term_repr(post->ast->formula), post->ast->label) );
                    
                    DBG( fprintf(stderr, "\n"); 
                        print_ascii_tree(post->ast, stderr);
                        fprintf(stderr, "\n") );

                    if( trace_search_label(path_formula, 
                                           post->ast->label) == NULL )
                    {
                        path_formula = trace_add_state(path_formula, 
                                                       trace_new_state(post->ast->label, 0, 0));
                    }
                    
                    
                    
                    if( MSAT_ERROR_TERM(f) )
                    {
                        fprintf(stderr, "%s: error: symbolic path formula does not conform to a MathSAT term\n", 
                                usage_name);
                        fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                        exit(-1);
                    }
                    
                    
                    msat_reset_env(sym_env);
                    sym_res = msat_assert_formula(sym_env, f);
                    
                    if( sym_res != 0 )
                    {
                        fprintf(stderr, "%s: error: SAT solver does not accept the symbolic path formula\n", 
                                usage_name);
                        fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                        exit(-1);
                    }
                    
                    
                    sym_status = msat_solve(sym_env);
                }
                                
                
                print_current_state_symbolic(post, stdout);
                
                
                if( sym_status == MSAT_SAT )
                {
                    if( step_flag == TRUE )
                    {
                        fprintf(stdout, "%s SAT\n", UNICODE_ARROW_RIGHT_CONC);                        
                    }
                }
                else if( sym_status == MSAT_UNSAT )
                {
                    if( step_flag == TRUE )
                    {
                        fprintf(stdout, "%s UNSAT\n\n", UNICODE_ARROW_RIGHT_CONC);
                        break;
                    }
                }
                else
                {
                    fprintf(stderr, "%s: error: SAT solver could not interpret the path formula\n", 
                            usage_name);
                    fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                    exit(-1);
                } 
            }
            
            
            
            
            
            if( step_flag == TRUE )
            {
                fprintf(stdout, "\n");
            }
            else
            {
                fprintf(stdout, "END\n\n");
            }
            
            
            fprintf(stdout, "Output value:\n");
            
            switch( (int) sym_status )
            {
                case MSAT_SAT:
                    fprintf(stdout, "\tSATISFIABLE\n");
                    break;
                    
                case MSAT_UNSAT:
                    fprintf(stdout, "\tNOT SATISFIABLE\n\nCounter example:\n");
                    
                    sprintf(line_buffer, " %s ", UNICODE_AND);
                    trace_print_labels_with_format(path_formula, stdout, "\t", line_buffer, "\n");
                    break;
                    
                default:
                    fprintf(stderr, "%s: error: SAT solver could not interpret the path formula\n", 
                            usage_name);
                    fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                    exit(-1);
                    break;
            }
            
            fprintf(stdout, "\n");
            
            break;
        }
        
            
            
            
            
            
            
            
            
            
            
            
            
        // perform numeric execution
        case FALSE:
        {
            fprintf(stdout, "Input value(s):\n");
            
            result = idents;
            while( result != NULL )
            {
                if( result->type  == TABLE_ELEMENT_TYPE_INPUT     ||
                   result->type  == TABLE_ELEMENT_TYPE_INOUTPUT  )
                {
                    none = FALSE;
                    
                    fprintf(stdout, "\t%s = ", result->name);
                    
                    check = fscanf(stdin, "%li", &result->value);
                    fflush(stdin);
                    
                    if( check == 0 )
                    {
                        fprintf(stderr, "%s: error: not a valid input value for variable '%s'\n", 
                                usage_name, 
                                result->name);
                        fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
                        exit(-1);
                    }
                }
                
                result = result->next;
            }
            
            if( none == TRUE )
            {
                fprintf(stdout, "\tno input variables declared\n");
            }
            
            fprintf(stdout, "\n");
            
            if( step_flag == TRUE )
            {
                fprintf(stdout, "Numeric execution trace:\n(press 'n' for next transition, 'a' to abort)\n\n");
            }
            
            print_current_state(state, stdout);
            
            for(;;)
            {
                execution_numeric_result = FALSE;
                
                for(next_state = 0; next_state <= lts_highest_to_request; next_state++)
                {
                    if( lts_matrix[state][next_state] != NULL )
                    {
                        if( state > next_state )
                        {
                            // loop goto condition ^= assume( true )
                            execution_numeric_result = TRUE;
                        }
                        else
                        {
                            if( lts_matrix[state][next_state]->type != NODE_STATEMENTS )
                            {
                                traverse(lts_matrix[state][next_state]->left, 
                                         TRAVERSE_POSTORDER, 
                                         TRAVERSE_LEFT_TO_RIGHT, 
                                         lts_execute_numeric);
                                
                                lts_matrix[state][next_state]->value = lts_matrix[state][next_state]->left->value;
                            }
                            
                            switch( (int)( lts_matrix[state][next_state]->type ) )
                            {
                                case NODE_IF:
                                case NODE_WHILE:
                                    if( lts_matrix[state][next_state]->value != 0 )
                                    {
                                        execution_numeric_result = TRUE;
                                        
                                        lts_matrix[state][next_state]->loop_counter++;
                                        
                                        if( lts_matrix[state][next_state]->loop_counter >= run_loop_bound &&
                                           step_flag == FALSE)
                                        {
                                            fprintf(stdout, "\tBOUND: infinite while loop with condition '%s' found in line %i\n\n", 
                                                    lts_matrix[state][next_state]->label, lts_matrix[state][next_state]->line);
                                            
                                            exit(-1);
                                        }
                                    }
                                    else
                                    {
                                        execution_numeric_result = FALSE;
                                        
                                        lts_matrix[state][next_state]->loop_counter = -1;
                                    }
                                    break;
                                    
                                case NODE_ASSERT:
                                    if( lts_matrix[state][next_state]->left->value != 0 )
                                    {
                                        execution_numeric_result = TRUE;
                                    }
                                    else
                                    {
                                        execution_numeric_result = FALSE;
                                    }
                                    break;
                                    
                                case NODE_STATEMENTS:
                                    if( strlen( lts_matrix[state][next_state]->label ) > 0 )
                                    {
                                        lts_matrix[state][next_state]->value = 
                                        (lts_matrix[state][next_state]->left->value == TRUE) ? FALSE : TRUE;
                                    }
                                    else
                                    {
                                        continue;
                                    }
                                    
                                default:
                                    execution_numeric_result = TRUE;
                                    break;
                            }
                        }
                        
                        last_transition = lts_matrix[state][next_state]->label;
                        
                        DBG( fprintf(stderr, "(%i, %i) %i --> %i  [ %li ] : %s", 
                                     lts_highest_to_request,
                                     execution_numeric_result,
                                     state, 
                                     next_state,
                                     lts_matrix[state][next_state]->value,
                                     lts_matrix[state][next_state]->label) );
                        
                        DBG( fprintf(stderr, "\n"); print_ascii_tree(lts_matrix[state][next_state]->left, stderr) );
                        
                        print_current_transition(state, 
                                                 next_state, 
                                                 lts_matrix[state][next_state]->label,
                                                 stdout);
                        
                        if( execution_numeric_result == TRUE )
                        {
                            state = next_state;
                            
                            print_current_state(state, 
                                                stdout);
                            
                            break;
                        }
                    }
                }
                
                if( execution_numeric_result == FALSE && state == lts_highest_to_request )
                {
                    DBG( fprintf(stderr, "end state was reached :)") );
                    
                    print_current_transition(state, 
                                             -1, 
                                             NULL,
                                             stdout);
                    
                    if( step_flag == TRUE )
                    {
                        fprintf(stdout, "\n");
                    }
                    
                    fprintf(stdout, "Output value(s):\n");
                    
                    none = TRUE;
                    
                    
                    result = idents;
                    while( result != NULL )
                    {
                        if( result->type  == TABLE_ELEMENT_TYPE_OUTPUT    ||
                           result->type  == TABLE_ELEMENT_TYPE_INOUTPUT  )
                        {
                            none = FALSE;
                            fprintf(stdout, "\t%s = %li\n", result->name, result->value);
                        }
                        
                        result = result->next;
                    }
                    
                    
                    if( none == TRUE )
                    {
                        fprintf(stdout, "\tno output variables declared\n");
                    }
                    none = FALSE;
                    
                    fprintf(stdout, "\n");
                    break;
                }
                
                if( execution_numeric_result == FALSE && state != lts_highest_to_request )
                {
                    DBG( fprintf(stderr, "error state was reached :(") );
                    
                    print_current_transition(state, 
                                             -2, 
                                             last_transition,
                                             stdout);
                    
                    if( step_flag == FALSE )
                    {
                        fprintf(stdout, "\tERROR: %s IS INVALID\n", last_transition);
                    }
                    
                    fprintf(stdout, "\n");
                    
                    exit(-1);
                }
            }
            
            break;
        }
    }
}


void lts_printer(FILE* stream, char line_prefix[], int print_full)
{
    int x;
    int y;
    
    if( print_full == TRUE )
    {
        fprintf(stream, "%s%i\n%s--\n%s", line_prefix, lts_highest_to_request + 1, line_prefix, line_prefix);
        
        for(y = 0; y <= lts_highest_to_request; y++)
        {
            for(x = 0; x <= lts_highest_to_request; x++)
            {
                if( lts_matrix[y][x] == NULL )
                {
                    fprintf(stream, "0 ");
                }
                else
                {
                    fprintf(stream, "1 ");
                }
            }
            fprintf(stream, "\n%s", line_prefix);
        }
        fprintf(stream, "--%s\n", line_prefix);
    }
    
    for(y = 0; y <= lts_highest_to_request; y++)
    {
        for(x = 0; x <= lts_highest_to_request; x++)
        {
            if( lts_matrix[y][x] != NULL )
            {
                fprintf(stream, "%s%i %i -> %s\n", line_prefix, y, x, lts_matrix[y][x]->label);
            }
        }
    }
}

void lts_generator(struct node* n)
{
    int x;
    int y;
    
    switch( (int)(n->type) ) 
    {
        case NODE_PROGRAM:
        {
            DBG( fprintf(stderr, "LTS edge list:") );
            
            lts_state_vertex = 0;
            lts_highest_to_request = 0;
            lts_last_vertex_to_state = lts_state_vertex;
            
            
            //lts_matrix = malloc( sizeof(struct node*) * lts_transition_counter*2*2 );
                        
            lts_matrix = (struct node ***)malloc(sizeof(struct node *) * lts_transition_counter*2);
            for (x = 0; x < lts_transition_counter*2; x++) 
            {
                lts_matrix[x] = (struct node **)malloc(sizeof(struct node*) * lts_transition_counter*2);
            }
            
            
            for(y = 0; y < lts_transition_counter*2; y++)
            {
                for(x = 0; x < lts_transition_counter*2; x++)
                {
                    lts_matrix[x][y] = NULL;
                }
            }
            
            break;
        }
        
        // fall through
        case NODE_ASSERT:
        {
            n->state = lts_state_vertex;
            
            if( strlen(n->label) > 0 )
            {
//                if(lts_state_vertex > 0)
//                {
//                    x = lts_state_vertex-1;
//                    y = lts_state_vertex;
//                }
//                else
//                {
//                    x = lts_state_vertex++;
//                    y = lts_state_vertex;
//                }
                
                lts_state_vertex = lts_last_vertex_to_state;
                x = lts_state_vertex++;
                y = lts_state_vertex;
                lts_last_vertex_to_state = lts_state_vertex;
                
                lts_highest_to_request = max(lts_highest_to_request, y);
                
                lts_matrix[x][y] = n;
                
                DBG( fprintf(stderr, "%i %i -> %s", x, y, n->label) );
            }
            break;
        }
        case NODE_IF:
        case NODE_WHILE:
        {
            n->state = lts_state_vertex;
            
            if( strlen(n->label) > 0 )
            {
                x = lts_state_vertex++;
                y = lts_state_vertex;
                lts_last_vertex_to_state = lts_state_vertex;
                
                lts_highest_to_request = max(lts_highest_to_request, y);
                
                lts_matrix[x][y] = n;
                
                DBG( fprintf(stderr, "%i %i -> %s", x, y, n->label) );
            }
            break;
        }
        
        case NODE_STATEMENT:
        {
            if( strlen(n->label) > 0 )
            {
                x = lts_state_vertex++;
                y = lts_state_vertex;
                lts_last_vertex_to_state = lts_state_vertex;
                
                lts_highest_to_request = max(lts_highest_to_request, y);
                
                lts_matrix[x][y] = n;
                
                DBG( fprintf(stderr, "%i %i -> %s", x, y, n->label) );
            }
            break;
        }
        
        case NODE_STATEMENTS:
        {
            if( strlen(n->label) > 0 )
            {
                switch( (int)(n->left->type) )
                {
                    case NODE_ASSERT:
                    {
                        return;
                        break;
                    }
                        
                    case NODE_WHILE:
                    {
                        x = lts_state_vertex++;
                        y = n->left->state;
                        
                        lts_highest_to_request = max(lts_highest_to_request, y);
                        
                        lts_matrix[x][y] = new_node(NODE_LEAF,NULL,NULL,0,NULL,0);
                        lts_matrix[x][y]->label = "assume(true)";
                        
                        DBG( fprintf(stderr, "%i %i -> %s", x, y, 
                                     lts_matrix[x][y]->label) ); 
                        
                        
                        x = n->left->state;
                        y = lts_state_vertex;
                        lts_last_vertex_to_state = lts_state_vertex;
                
                        break;
                    }
                    
                    case NODE_IF:
                    {
                        x = n->left->state;
                        y = lts_state_vertex;
                        lts_last_vertex_to_state = lts_state_vertex;
                        
                        break;
                    }    
                    
                    case NODE_STATEMENT:
                    {
                        if( strlen(n->left->label) > 0 )
                        {
                            x = n->left->state;
                            y = lts_state_vertex;
                            lts_last_vertex_to_state = lts_state_vertex;
                        }
                        break;
                    }     
                        
                    default:
                    {
                        break;
                    }
                }
                
                lts_highest_to_request = max(lts_highest_to_request, y);
                
                lts_matrix[x][y] = n;
                
                DBG( fprintf(stderr, "%i %i -> %s", x, y, n->label) );
            }
            break;
        }                       
    }
}

void lts_labeling(struct node* n)
{
    switch( (int)(n->type) ) 
    {
        case NODE_PROGRAM:
            DBG( fprintf(stderr, "LTS transition counter = %i", lts_transition_counter) );
            break;
        
        case NODE_DECLARATIONS:
            if( n->left == NULL && n->right != NULL)
            {
                sprintf(n->label, "out: %s", n->right->label); 
            }
            else if( n->left != NULL && n->right == NULL)
            {
                sprintf(n->label, "in: %s", n->left->label); 
            }
            else if( n->left != NULL && n->right != NULL)
            {
                sprintf(n->label, "in: %s, out: %s", n->left->label, n->right->label); 
            }
            break;
            
        case NODE_INPUT:
        case NODE_OUTPUT:
            if( n->right != NULL )
            {
                sprintf(n->label, "%s, %s", n->left->label, n->right->label); 
            }
            else
            {
                sprintf(n->label, "%s", n->left->label); 
            }
            break;
            
        case NODE_INPUT_VAR:
            n->label = n->name;
            
            result = table_search_element(idents, n->name);
            
            if( result == NULL )
            {
                struct table_element* e = table_new_element(TABLE_ELEMENT_TYPE_INPUT, n->name, 0);
                
                n->ident = e;
                
                idents = table_add_element(idents, e);
            }
            else
            {
                n->ident = result;
                
                fprintf(stderr, "%s: error: input variable %s was already declared as input\n", 
                        usage_name, 
                        n->name);
                error_sem++;
            }
            break;
            
        case NODE_OUTPUT_VAR:
            n->label = n->name;
            
            result = table_search_element(idents, n->name);
            
            if( result == NULL )
            {
                struct table_element* e = table_new_element(TABLE_ELEMENT_TYPE_OUTPUT, n->name, 0);
                
                n->ident = e;
                
                idents = table_add_element(idents, e);
            }
            else
            {
                n->ident = result;
                
                if( result->type == TABLE_ELEMENT_TYPE_INPUT )
                {
                    result->type = TABLE_ELEMENT_TYPE_INOUTPUT;
                    
                    fprintf(stderr, "%s: warning: output variable '%s' is also an input variable\n", 
                            usage_name, 
                            n->name);
                    warning_sem++;
                }
                else if( result->type == TABLE_ELEMENT_TYPE_OUTPUT )
                {
                    fprintf(stderr, "%s: error: output variable '%s' was already declared as output\n", 
                            usage_name, 
                            n->name);
                    error_sem++;
                }
            }
            break;
        
            
            
        
        // ----------------------------------------------------------------
        
        case NODE_VAR:
            result = table_search_element(idents, n->name);
            
            if( result == NULL )
            {
                struct table_element* e = table_new_element(TABLE_ELEMENT_TYPE_VAR, n->name, 0);
                
                n->ident = e;
                
                idents = table_add_element(idents, e);
                
                n->ident->assign_cnt = 0;
            }
            else
            {
                n->ident = result;
            }
            break;
            
            
        // dummy cases: fall through just to take over lower label
        case NODE_EXPR:
        case NODE_SUM:
        case NODE_TEST:
        case NODE_TERM:
            sprintf(n->label, "%s", n->left->label);
            break;
            
        // operations
        case NODE_PLUS: 
            sprintf(n->label, "%s + %s", n->left->label, n->right->label);
            break;
            
        case NODE_MINUS:
            sprintf(n->label, "%s - %s", n->left->label, n->right->label);
            break;
            
        case NODE_LESS:
            sprintf(n->label, "%s < %s", n->left->label, n->right->label);
            break;
            
        case NODE_EQUAL:            
            sprintf(n->label, "%s == %s", n->left->label, n->right->label);
            break;
        
        // EXTENSION
        case NODE_GREATER:
            sprintf(n->label, "%s > %s", n->left->label, n->right->label);
            break;
            
            
        // paren expressions
        case NODE_PARENEXPR_NOT:
            sprintf(n->label, "!(%s)", n->left->label);
            break;
            
        case NODE_PARENEXPR:
            sprintf(n->label, "%s", n->left->label);
            break;    
            
        case NODE_ASSIGN:
            sprintf(n->label, "%s = %s", n->right->label, n->left->label);
            break;
            
        // LTS action elements
        case NODE_ASSERT:
            lts_transition_counter++;
            sprintf(n->label, "assume(%s)", n->left->label);
            break;
            
        case NODE_IF:               
            lts_transition_counter++;
            sprintf(n->label, "assume(%s)", n->left->label);
            break;
            
        case NODE_WHILE:            
            lts_transition_counter++;
            sprintf(n->label, "assume(%s)", n->left->label);
            break;
        
        case NODE_STATEMENT:
            switch( (int)(n->left->type) )
            {
                // dummy cases: fall through just to take over lower label
                case NODE_ASSIGN:
                case NODE_ASSERT:
                case NODE_EXPR:
                    lts_transition_counter++;
                    sprintf(n->label, "%s", n->left->label);
                    break;
            } 
            break;
        
        case NODE_STATEMENTS: 
            switch( (int)(n->left->type) )
            {
                // fall through, negate the lower formula
                case NODE_IF:
                case NODE_WHILE:
                case NODE_ASSERT: // TODO: if the error state is not present, erase line!!!
                    lts_transition_counter++;
                    if( n->left->left->left->type != NODE_PARENEXPR_NOT)
                    {
                        sprintf(n->label, "assume(!(%s))", 
                                n->left->left->label);
                    }
                    else
                    {
                        sprintf(n->label, "assume(%s)", 
                                n->left->left->left->left->label);
                    }
                    break;
            }    
            break;
    }
}
