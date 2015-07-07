
#include "table.h"


struct table_element* table_new_element(table_element_type t, char* n, long int v)
{
    struct table_element* buffer = (struct table_element*)malloc( sizeof(struct table_element) );
    
    buffer->type    = t;
    buffer->name    = n;
    buffer->value   = v;
    buffer->next    = NULL;
    
    buffer->not_accessed = TRUE;
    
    return buffer;
}


struct table_element* table_add_element(struct table_element* root, struct table_element* n)
{
    if( root == NULL )
    {
        root = n;
    }
    else
    {
        struct table_element* ptr = root;
        
        while( ptr->next != NULL )
        {
            ptr = ptr->next;
        }
        
        ptr->next = n;
        
       
    }
    
    return root;
}


struct table_element* table_search_element(struct table_element* root, char *name)
{
    struct table_element* ptr = root;
    
    while( ptr != NULL )
    {
        if( strcmp(ptr->name, name) == 0 )
        {
            return ptr;
        }
        
        ptr = ptr->next;
    }
    
    return NULL;
}


void table_print(struct table_element* root, FILE* stream)
{
    struct table_element* ptr = root;
    
    while( ptr != NULL )
    {
        switch( (int)(ptr->type) )
        {
            case TABLE_ELEMENT_TYPE_VAR:        fprintf(stream, "V"); break;
            case TABLE_ELEMENT_TYPE_INPUT:      fprintf(stream, "I"); break;
            case TABLE_ELEMENT_TYPE_OUTPUT:     fprintf(stream, "O"); break;
            case TABLE_ELEMENT_TYPE_INOUTPUT:   fprintf(stream, "T"); break;    
        }
        
        fprintf(stream, ", %s, %li\n", ptr->name, ptr->value);
        
        ptr = ptr->next;
    }
    
    fprintf(stream, "\n");
}

















































