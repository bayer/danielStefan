
#include "ks.h"

static FILE *my_fmemopen (void *buf, size_t size, const char *opentype)
{
    FILE *f;
    
    f = tmpfile();
    fwrite(buf, 1, size, f);
    rewind(f);
    
    return f;
}


struct trace* path = NULL;

struct loop_buffer* loops_from_tks = NULL;

char line_buffer[4096];
int calc_line;
int **ks_matrix;


msat_config  sym_cfg;
msat_env     sym_env;
msat_term    sym_formula;
msat_result  sym_status;
int          sym_res;


void ks_init_symbolic_execution(void)
{
    sym_cfg = msat_create_config();
    msat_set_option(sym_cfg, "model_generation", "true");
    sym_env = msat_create_env(sym_cfg);
}


void ks_predicate_parse_and_ast_generation(void)
{
    DBG( fprintf(stderr, "starting predicates AST generation") );
    
    struct trace* ptr = path;
    
    active_file = trace_formula;
    
    while( ptr != NULL && ptr->ks_state != -1 && ptr->lts_state != -1 )
    {
        DBG( fprintf(stderr, "predicate '%s' AST generation", ptr->label) );
        
        error_lex = 0;
        error_parse = 0;
        
        sprintf(line_buffer, "[ %s ]", ptr->label);
        
        yyin = my_fmemopen(line_buffer, strlen(line_buffer), "r");
        
        line_cnt = calc_line + ptr->ks_state + 1;
        
        yyparse();
        
        fclose(yyin);
        
        ptr->ast = root;
        
        traverse(ptr->ast, 
                 TRAVERSE_POSTORDER, 
                 TRAVERSE_LEFT_TO_RIGHT, 
                 lts_labeling);
                
        if( (error_lex + error_parse) != 0 )
        {
            fprintf(stderr, "%s: '%s' is not a valid tinyC predicate\n", usage_name, ptr->label);
            fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
            exit(-1);
        }
        
        ptr = ptr->next;
    }
}


void ks_sanity_check(void)
{
    DBG( fprintf(stderr, "starting sanity check if trace path is on kripke structure and on the LTS") );
    
    struct trace* ptr = path;
    
    while( ptr->next != NULL && ptr->next->ks_state != -1 && ptr->next->lts_state != -1 )
    {
        DBG( fprintf(stderr, "sanity check of mapping (%i | %i/%s) %s (%i | %i/%s)", 
                     ptr->ks_state,
                     ptr->lts_state,
                     ptr->label,
                     UNICODE_ARROW_RIGHT,
                     ptr->next->ks_state,
                     ptr->next->lts_state,
                     ptr->next->label) );
        
        if( ks_matrix[ ptr->ks_state][ ptr->next->ks_state ] != 1 ||
            lts_matrix[ ptr->lts_state][ ptr->next->lts_state ] == NULL )
        {
            DBG( fprintf(stderr, "ks ? %i, lts ? %i",
                         ks_matrix[ ptr->ks_state][ ptr->next->ks_state ] != 1,
                         lts_matrix[ ptr->lts_state][ ptr->next->lts_state ] == NULL) );
            
            fprintf(stderr, "%s: sanity check failed with mapping (%i | %i/%s) %s (%i | %i/%s)\n", 
                    usage_name,
                    ptr->ks_state,
                    ptr->lts_state,
                    ptr->label,
                    UNICODE_ARROW_RIGHT,
                    ptr->next->ks_state,
                    ptr->next->lts_state,
                    ptr->next->label);
            
            fprintf(stderr, "%s: interpretation aborted\n\n", usage_name);
            exit(-1);
        }
        
        ptr = ptr->next;
    }
    
    DBG( fprintf(stderr, "starting sanity check was successful") );
}


void ks_generate(void)
{
    int num = 0;
    char ctrl = '0';
    
    DBG( fprintf(stderr, "generate kripke structure from trace file") );
    
    calc_line = 0;
    
    // read in the trace
    for(;;)
    {
        fscanf(trace_stream, "%i%c", &num, &ctrl);
        
        if( ctrl == ',' )
        {
            path = trace_add_state(path, trace_new_state("", num, -1));
        }
        else if( ctrl == '\n' || ctrl == ':' )
        {
            path = trace_add_state(path, trace_new_state("", num, -1));
            
            path = trace_add_state(path, trace_new_state("END", -1, -1));
            break;
        }
        else
        {
            fprintf(stderr, "%s: 1, %s is not a vaild trace file\n\n", usage_name, trace_formula);
            exit(-1);
        }
    }
    // read in the loop upper bounds
    if( ctrl == ':' )
    {        
        for(;;)
        {
            fscanf(trace_stream, "%i%c", &num, &ctrl);
            
            if( ctrl == ',' )
            {
                loops_from_tks = loop_buffer_add(loops_from_tks, loop_buffer_new(num));
            }
            else if( ctrl == '\n' || ctrl == ':' )
            {
                loops_from_tks = loop_buffer_add(loops_from_tks, loop_buffer_new(num));
                
                break;
            }
            else
            {
                fprintf(stderr, "%s: 1, %s is not a vaild trace file with loop upper bound list\n\n", usage_name, trace_formula);
                exit(-1);
            }
        }
    }
    
    
    
    calc_line += 1;
    
    // check for the "--" string
    fgets(line_buffer, sizeof(line_buffer), trace_stream);
        
    if( strcmp(line_buffer, "--\n") != 0 )
    {
        fprintf(stderr, "%s: 2, %s is not a vaild trace file\n\n", usage_name, trace_formula);
        exit(-1);
    }
    
    calc_line += 1;
    
    // read in the vertex counter
    fscanf(trace_stream, "%i%c", &num, &ctrl);
    
    if( ctrl != '\n' )
    {
        fprintf(stderr, "%s: 3, %s is not a vaild trace file\n\n", usage_name, trace_formula);
        exit(-1);
    }
    
    calc_line += 1;
    
    // check for the "--" string
    fgets(line_buffer, sizeof(line_buffer), trace_stream);
    
    if( strcmp(line_buffer, "--\n") != 0 )
    {
        fprintf(stderr, "%s: 4, %s is not a vaild trace file\n\n", usage_name, trace_formula);
        exit(-1);
    }
    
    calc_line += 1;
    
    // read in the kripke structure matrix
    int x;
    int y;
    
    ks_matrix = (int **)malloc(sizeof(int *) * num);
    for (x = 0; x < num; x++) 
    {
        ks_matrix[x] = (int *)malloc(sizeof(int) * num);
    }
    
    for(y = 0; y < num; y++)
    {
        for(x = 0; x < num - 1; x++)
        {
            fscanf(trace_stream, "%i%c", &ks_matrix[y][x], &ctrl);
        
            if( ctrl != ' ' )
            {
                fprintf(stderr, "%s: 5, %s is not a vaild trace file\n\n", usage_name, trace_formula);
                exit(-1);
            }
        }
        
        fscanf(trace_stream, "%i%c", &ks_matrix[y][num - 1], &ctrl);
        
        if( ctrl != '\n' )
        {
            fprintf(stderr, "%s: 6, %s is not a vaild trace file\n\n", usage_name, trace_formula);
            exit(-1);
        }
        
        calc_line += 1;
    }
    
    // check for the "--" string
    fgets(line_buffer, sizeof(line_buffer), trace_stream);
    
    if( strcmp(line_buffer, "--\n") != 0 )
    {
        fprintf(stderr, "%s: 7, %s is not a vaild trace file\n\n", usage_name, trace_formula);
        exit(-1);
    }
    
    calc_line += 1;
    
    // read in the predicates of the KS for the trace only!!!
    int ks_state;
    struct trace* result;
    
    for(x = 0; x < num; x++)
    {
        if( fscanf(trace_stream, "%i -> ", &ks_state) != 1)
        {
            fprintf(stderr, "%s: 8, %s is not a vaild trace file\n\n", usage_name, trace_formula);
            exit(-1);
        }
        
        fgets(line_buffer, sizeof(line_buffer), trace_stream);
        
        line_buffer[ strlen(line_buffer)-1 ] = '\0';
        
        result = path;
        
        while( (result = trace_search_ks_state(result, ks_state)) != NULL )
        {
            sprintf(result->label, "%s", line_buffer);
            result = result->next;
        }
    }
    
    // check for the "--" string
    fgets(line_buffer, sizeof(line_buffer), trace_stream);
    
    if( strcmp(line_buffer, "--\n") != 0 )
    {
        fprintf(stderr, "%s: 7, %s is not a vaild trace file\n\n", usage_name, trace_formula);
        exit(-1);
    }
    
    // read in the KS to LTS mapping for the trace only!!!
    int lts_state;
    
    for(x = 0; x < num; x++)
    {
        if( fscanf(trace_stream, "%i -> %i\n", &ks_state, &lts_state) != 2)
        {
            fprintf(stderr, "%s: 8, %s is not a vaild trace file\n\n", usage_name, trace_formula);
            exit(-1);
        }
        
        
        result = path;
        
        while( (result = trace_search_ks_state(result, ks_state)) != NULL )
        {
            result->lts_state = lts_state;
            result = result->next;
        }
    }
    
    DBG(    fprintf(stderr, "parsed trace with kripke structure and LTS states and predicates\n");
            trace_print(path, stderr) );
}



struct trace* trace_new_state(char* l, int k, int s)
{
    struct trace* buffer = (struct trace*)malloc( sizeof(struct trace) );
    
    buffer->label       = malloc(sizeof(unsigned char) * TRACE_LABEL_SIZE);
    sprintf(buffer->label, "%s", l);
    
    buffer->ks_state    = k;
    buffer->lts_state   = s;
    
    buffer->next        = NULL;
    buffer->previous    = NULL;
    
    buffer->loop        = NULL;
    buffer->loop_cnt    = 0;
    
    return buffer;
}


struct trace* trace_add_state(struct trace* root, struct trace* t)
{
    if( root == NULL )
    {
        root = t;
    }
    else
    {
        struct trace* ptr = root;
        
        while( ptr->next != NULL )
        {
            ptr = ptr->next;
        }
        
        ptr->next = t;
        t->previous = ptr;       
    }
    
    return root;
}


struct trace* trace_search_ks_state(struct trace* root, int k)
{
    struct trace* ptr = root;
    
    while( ptr != NULL )
    {
        if( ptr->ks_state == k )
        {
            return ptr;
        }
        
        ptr = ptr->next;
    }
    
    return NULL;
}


struct trace* trace_search_label(struct trace* root, char* l)
{
    struct trace* ptr = root;
    
    while( ptr != NULL )
    {
        if( strcmp(ptr->label, l) == 0 )
        {
            return ptr;
        }
        
        ptr = ptr->next;
    }
    
    return NULL;
}

struct trace* trace_search_loop(struct trace* t)
{
    struct trace* ptr = t->previous;
    
    while(  ptr != NULL)
    {        
        if( ptr->ks_state == t->ks_state )
        {   
            t->loop = ptr;
            
            return ptr;
        }
        else
        {
            if( ptr->loop != NULL )
            {
                return NULL;
            }
        }
        
        ptr = ptr->previous;
    }
    
//    if( t->previous->lts_state > t->lts_state )
//    {
//        struct trace* ptr = t->previous;
//        
//        while( ptr != NULL )
//        {
//            if( ptr->lts_state == t->lts_state )
//            {
//                t->loop = ptr;
//                
//                return ptr;
//            }
//            
//             ptr = ptr->previous;
//        }
//    }
    
    return NULL;
}


void trace_print(struct trace* root, FILE* stream)
{
    struct trace* ptr = root;
    
    while( ptr != NULL )
    {
        fprintf(stream, "%i\t%i\t%s\n", ptr->ks_state, ptr->lts_state, ptr->label);
        
        ptr = ptr->next;
    }
    
    fprintf(stream, "\n");
}


void trace_print_labels_with_format(struct trace* root, FILE* stream, char* pre, char* seperator, char* post)
{
    struct trace* ptr = root;
    int i = 0;
    
    if( ptr != NULL )
    {
        fprintf(stream, "%s%s", pre, ptr->label);
        
        ptr = ptr->next;
        
        while( ptr != NULL )
        {
            i++;
            if( i % 4 == 0 )
            {
                fprintf(stream, "%s\n\t%s", seperator, ptr->label);
            }
            else
            {
                fprintf(stream, "%s%s", seperator, ptr->label);
            }
            
            
//            fprintf(stream, "%s%s", seperator, ptr->label);
            
            ptr = ptr->next;
        }
        
        fprintf(stream, "%s", post);
    }
}




struct loop_buffer* loop_buffer_new(int b)
{
    struct loop_buffer* buffer = (struct loop_buffer*)malloc( sizeof(struct loop_buffer) );
    
    buffer->bound   = b;
    buffer->next    = NULL;

    return buffer;
}


struct loop_buffer* loop_buffer_add(struct loop_buffer* root, struct loop_buffer* t)
{
    if( root == NULL )
    {
        root = t;
    }
    else
    {
        struct loop_buffer* ptr = root;
        
        while( ptr->next != NULL )
        {
            ptr = ptr->next;
        }
        
        ptr->next = t;
    }
    
    return root;
}


































