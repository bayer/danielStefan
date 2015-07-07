
#ifndef NODE_H
#define NODE_H

#include "interpreter.h"
#include "mathsat.h"

typedef enum 
{  
    NODE_LEAF=0,                //0
    NODE_PROGRAM,
    NODE_DECLARATIONS,
    NODE_STATEMENTS,
    NODE_STATEMENT,
    NODE_PARENEXPR,             //5
    NODE_EXPR,
    NODE_TEST,
    NODE_SUM,
    NODE_EQUAL,
    NODE_LESS,                  //10
    NODE_VAR,
    NODE_INT,
    NODE_TERM,
    NODE_INPUT,
    NODE_INPUT_VAR,             //15
    NODE_OUTPUT,
    NODE_OUTPUT_VAR,
    NODE_IF,
    NODE_WHILE,
    NODE_ASSERT,                //20
    NODE_ASSIGN,
    NODE_PARENEXPR_NOT,
    NODE_PLUS,
    NODE_MINUS,
    NODE_PREDICATES,            //25
    NODE_GREATER
} node_t;

struct node
{
	/* ABSTRACT SYNTAX TREE NODE */
	node_t			type;
	struct node*	left;
	struct node*	right;
	
    /* SYMBOL DATA */
	long int		value;
    char*           name;

	unsigned int 	line;	

    /* LTS */
    char*                   label;
    int                     state;
    
    long int                loop_counter;
    
    /* SYMBOL TABLE */
    struct table_element*   ident;
    
    int                     ident_index;
    
    /* SYMBOLIC EXECUTION - MATHSAT */
    msat_term               formula;
};


#define LEAF                new_node(NODE_LEAF,NULL,NULL,\
                                0,NULL,0)
#define INTERNAL(TYPE,L,R)  new_node(TYPE,L,R,\
                                0,NULL,0)

#define WHILE_INTR(L,R,LINE)new_node(NODE_WHILE,L,R,\
                                0,NULL,LINE)

#define ID(NAME,LINE)       new_node(NODE_VAR,NULL,NULL,\
                                0,NAME,LINE)
#define INT(VALUE,LINE)     new_node(NODE_INT,NULL,NULL,\
                                VALUE,NULL,LINE)
#define ID_IN(NAME,LINE)    new_node(NODE_INPUT_VAR,NULL,NULL,\
                                0,NAME,LINE)
#define ID_OUT(NAME,LINE)   new_node(NODE_OUTPUT_VAR,NULL,NULL,\
                                0,NAME,LINE)


struct node* new_node(node_t            type, 
                      struct node*      left, 
                      struct node*      right,
                      long int          value,
                      char*             name,
                      unsigned int      line);

#define TRAVERSE_PREORDER      -1
#define TRAVERSE_POSTORDER      1
#define TRAVERSE_MIDORDER       0

#define TRAVERSE_LEFT_TO_RIGHT  0
#define TRAVERSE_RIGHT_TO_LEFT  1

void traverse(struct node* root,
              int with_order,
              int from_left_to_right,
              void (*with_action)(struct node* node));

void free_node(struct node* root);



void print_ascii_tree(struct node* t, FILE* stream);


#endif /* NODE_H */
