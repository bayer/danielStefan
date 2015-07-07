
#ifndef TABLE_H
#define TABLE_H

#include "interpreter.h"


typedef enum 
{  
    TABLE_ELEMENT_TYPE_VAR=0,
    TABLE_ELEMENT_TYPE_INPUT,
    TABLE_ELEMENT_TYPE_OUTPUT,
    TABLE_ELEMENT_TYPE_INOUTPUT
} table_element_type;

struct table_element
{    
    table_element_type      type;
    char*                   name;
    long int                value;
    int                     assign_cnt;
    
    int                     not_accessed;
    
    struct table_element*   next;
};


struct table_element* table_new_element(table_element_type t, char* n, long int v);

struct table_element* table_add_element(struct table_element* root, struct table_element* n);

struct table_element* table_search_element(struct table_element* root, char *name);

void table_print(struct table_element* root, FILE* stream);


#endif /* TABLE_H */
