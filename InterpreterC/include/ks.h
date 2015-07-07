
#ifndef KS_H
#define KS_H

#include "interpreter.h"


struct trace
{
    char*           label;
    int             ks_state;
    int             lts_state;
    
    struct node*    ast;
    
    struct trace*   next;
    struct trace*   previous;
    
    struct trace*   loop;
    int             loop_cnt;
};

struct loop_buffer
{
    int                 bound;
    struct loop_buffer* next;
};


void ks_init_symbolic_execution(void);

void ks_predicate_parse_and_ast_generation(void);

void ks_sanity_check(void);

void ks_generate(void);



struct trace* trace_new_state(char* l, int k, int s);

struct trace* trace_add_state(struct trace* root, struct trace* t);

struct trace* trace_search_ks_state(struct trace* root, int k);

struct trace* trace_search_label(struct trace* root, char* l);

struct trace* trace_search_loop(struct trace* t);


void trace_print(struct trace* root, FILE* stream);

void trace_print_labels_with_format(struct trace* root, FILE* stream, char* pre, char* seperator, char* post);


struct loop_buffer* loop_buffer_new(int b);
struct loop_buffer* loop_buffer_add(struct loop_buffer* root, struct loop_buffer* t);



#endif /* KS_H */
