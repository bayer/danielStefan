
#include "node.h"

char *node_t_str[] =
{
    "LEAF",
    "PROG",
    "DECL",
    "STMS",
    "STM",
    "PEXP",
    "EXP",
    "TEST",
    "SUM",
    "EQU",
    "LESS",
    "VAR",
    "INT",
    "TERM",
    "IN",
    "IVAR",
    "OUT",
    "OVAR",
    "IF",
    "WHILE",
    "ASSERT",
    "ASSIGN",
    "!PEXP",
    "PLUS",
    "MINUS",
    "PREDICATE",
    "GREATER"
};

struct node* new_node(node_t            type, 
                      struct node*      left, 
                      struct node*      right,
                      long int          value,
                      char*             name,
                      unsigned int      line)
{
	struct node* n = (struct node*)malloc(sizeof(struct node));
    
    if(n == NULL)
	{
		fprintf(stderr, "ERROR: memory allocation failed, out of memory\n");
		exit(-1);
	}
    
    n->type 		= type;
    
	n->left 		= left;
	n->right 		= right;
	
    n->value 		= value;
    n->name 		= name;
    n->line 		= line;
	
    n->label        = malloc(sizeof(unsigned char) * NODE_LABEL_SIZE);
    n->state        = -1;
    
    n->loop_counter = -1;
    
    n->ident        = NULL;
    n->ident_index  = -1;
    
    n->formula.repr = NULL;
    
    switch( (int)n->type ) 
    {
        case NODE_VAR:
        {
            sprintf(n->label, "%s", n->name);
            break;
        }
        case NODE_INT:
        {
            sprintf(n->label, "%li", n->value);
            break;
        }
    }
    
	return n;
}


void traverse(struct node* root,
              int with_order,
              int from_left_to_right,
              void (*with_action)(struct node* node))
{
    if(with_order == TRAVERSE_PREORDER)
    {
        (*with_action)(root);
    }
    
    if( from_left_to_right == TRAVERSE_LEFT_TO_RIGHT )
    {
        if( root->left != NULL )
        {
            traverse(root->left, 
                     with_order, 
                     from_left_to_right, 
                     with_action);
        }
        
        if(with_order == TRAVERSE_MIDORDER)
        {
            (*with_action)(root);
        }
        
        if( root->right != NULL )
        {
            traverse(root->right, 
                     with_order, 
                     from_left_to_right, 
                     with_action);
        }
    }
    else
    {
        if( root->right != NULL )
        {
            traverse(root->right, 
                     with_order, 
                     from_left_to_right, 
                     with_action);
        }
        
        if(with_order == TRAVERSE_MIDORDER)
        {
            (*with_action)(root);
        }
        
        if( root->left != NULL )
        {
            traverse(root->left, 
                     with_order, 
                     from_left_to_right, 
                     with_action);
        }
    }
    
    if(with_order == TRAVERSE_POSTORDER)
    {
        (*with_action)(root);
    }
}


void free_node(struct node* root) 
{
    if (root == NULL)
    {
        return;
    }
    
    free_node(root->left);
    free_node(root->right);
    
    free(root);
}








// ----------------------------------------------------------
// printing abstract syntax tree to console
// modified to interact with this abstract syntax tree node structure
// original source code from: 
// http://web.archive.org/web/20071224095835/http://www.openasthra.com/wp-content/uploads/2007/12/binary_trees1.c


typedef struct asciinode_struct asciinode;

struct asciinode_struct
{
    asciinode * left, * right;
    
    //length of the edge from this node to its children
    int edge_length; 
    
    int height;      
    
    int lablen;
    
    //-1=I am left, 0=I am root, 1=right   
    int parent_dir;   
    
    //max supported unit32 in dec, 10 digits max
    //char label[11];  
    
    char label[128];
};


#define MAX_HEIGHT 4096
int lprofile[MAX_HEIGHT];
int rprofile[MAX_HEIGHT];
#define INFINITY (1<<20)

//adjust gap between left and right nodes
int gap = 4;  

//used for printing next node in the same level, 
//this is the x coordinate of the next char printed
int print_next;    

static int MIN (int X, int Y)  
{
    return ((X) < (Y)) ? (X) : (Y);
}

static int MAX (int X, int Y)  
{
    return ((X) > (Y)) ? (X) : (Y);
}

static asciinode* build_ascii_tree_recursive(struct node* t) 
{
    asciinode* node;
    
    if (t == NULL) return NULL;
    
    node = malloc(sizeof(asciinode));
    node->left = build_ascii_tree_recursive(t->left);
    node->right = build_ascii_tree_recursive(t->right);
    
    if (node->left != NULL) 
    {
        node->left->parent_dir = -1;
    }
    
    if (node->right != NULL) 
    {
        node->right->parent_dir = 1;
    }
    
    if( trace_flag == FALSE )
    {
        sprintf(node->label, "%s {%s}",
                node_t_str[ t->type ], 
                t->label);
    }
    else
    {
        if( t->formula.repr == NULL )
        {
            sprintf(node->label, "%s {%s}",
                    node_t_str[ t->type ], 
                    t->label);
        }
        else
        {
            sprintf(node->label, "%s | %s",
                    node_t_str[ t->type ], 
                    msat_term_repr(t->formula));
        }
    }
    
    node->lablen = strlen(node->label);
    
    return node;
}


//Copy the tree into the ascii node structre
static asciinode * build_ascii_tree(struct node* t) 
{
    asciinode *node;
    if (t == NULL) return NULL;
    node = build_ascii_tree_recursive(t);
    node->parent_dir = 0;
    return node;
}

//Free all the nodes of the given tree
static void free_ascii_tree(asciinode *node) 
{
    if (node == NULL) return;
    free_ascii_tree(node->left);
    free_ascii_tree(node->right);
    free(node);
}

//The following function fills in the lprofile array for the given tree.
//It assumes that the center of the label of the root of this tree
//is located at a position (x,y).  It assumes that the edge_length
//fields have been computed for this tree.
static void compute_lprofile(asciinode *node, int x, int y) 
{
    int i, isleft;
    if (node == NULL) return;
    isleft = (node->parent_dir == -1);
    lprofile[y] = MIN(lprofile[y], x-((node->lablen-isleft)/2));
    if (node->left != NULL) 
    {
        for (i=1; i <= node->edge_length && y+i < MAX_HEIGHT; i++) 
        {
            lprofile[y+i] = MIN(lprofile[y+i], x-i);
        }
    }
    compute_lprofile(node->left, x-node->edge_length-1, y+node->edge_length+1);
    compute_lprofile(node->right, x+node->edge_length+1, y+node->edge_length+1);
}

static void compute_rprofile(asciinode *node, int x, int y) 
{
    int i, notleft;
    if (node == NULL) return;
    notleft = (node->parent_dir != -1);
    rprofile[y] = MAX(rprofile[y], x+((node->lablen-notleft)/2));
    if (node->right != NULL) 
    {
        for (i=1; i <= node->edge_length && y+i < MAX_HEIGHT; i++) 
        {
            rprofile[y+i] = MAX(rprofile[y+i], x+i);
        }
    }
    compute_rprofile(node->left, x-node->edge_length-1, y+node->edge_length+1);
    compute_rprofile(node->right, x+node->edge_length+1, y+node->edge_length+1);
}

//This function fills in the edge_length and 
//height fields of the specified tree
static void compute_edge_lengths(asciinode *node) 
{
    int h, hmin, i, delta;
    if (node == NULL) return;
    compute_edge_lengths(node->left);
    compute_edge_lengths(node->right);
    
    /* first fill in the edge_length of node */
    if (node->right == NULL && node->left == NULL) 
    {
        node->edge_length = 0;
    } 
    else 
    {
        if (node->left != NULL) 
        {
            for (i=0; i<node->left->height && i < MAX_HEIGHT; i++) 
            {
                rprofile[i] = -INFINITY;
            }
            compute_rprofile(node->left, 0, 0);
            hmin = node->left->height;
        } 
        else 
        {
            hmin = 0;
        }
        if (node->right != NULL) 
        {
            for (i=0; i<node->right->height && i < MAX_HEIGHT; i++) 
            {
                lprofile[i] = INFINITY;
            }
            compute_lprofile(node->right, 0, 0);
            hmin = MIN(node->right->height, hmin);
        } 
        else 
        {
            hmin = 0;
        }
        delta = 4;
        for (i=0; i<hmin; i++) 
        {
            delta = MAX(delta, gap + 1 + rprofile[i] - lprofile[i]);
        }
        
        //If the node has two children of height 1, then we allow the
        //two leaves to be within 1, instead of 2 
        if (((node->left != NULL && node->left->height == 1) ||
             (node->right != NULL && node->right->height == 1))&&delta>4) 
        {
            delta--;
        }
	    
        node->edge_length = ((delta+1)/2) - 1;
    }
    
    //now fill in the height of node
    h = 1;
    if (node->left != NULL) 
    {
        h = MAX(node->left->height + node->edge_length + 1, h);
    }
    if (node->right != NULL) 
    {
        h = MAX(node->right->height + node->edge_length + 1, h);
    }
    node->height = h;
}

//This function prints the given level of the given tree, assuming
//that the node has the given x cordinate.
static void print_level(asciinode *node, int x, int level, FILE* stream) 
{
    int i, isleft;
    if (node == NULL) return;
    isleft = (node->parent_dir == -1);
    if (level == 0) 
    {
        for (i=0; i<(x-print_next-((node->lablen-isleft)/2)); i++) 
        {
            fprintf(stream, " ");
        }
        print_next += i;
        fprintf(stream, "%s", node->label);
        print_next += node->lablen;
    } 
    else if (node->edge_length >= level) 
    {
        if (node->left != NULL) 
        {
            for (i=0; i<(x-print_next-(level)); i++) 
            {
                fprintf(stream, " ");
            }
            print_next += i;
            fprintf(stream, "/");
            print_next++;
        }
        if (node->right != NULL) 
        {
            for (i=0; i<(x-print_next+(level)); i++) 
            {
                fprintf(stream, " ");
            }
            print_next += i;
            fprintf(stream, "\\");
            print_next++;
        }
    } 
    else 
    {
        print_level(node->left, 
                    x-node->edge_length-1, 
                    level-node->edge_length-1, stream);
        print_level(node->right, 
                    x+node->edge_length+1, 
                    level-node->edge_length-1, stream);
    }
}

//prints ascii tree for given Tree structure
void print_ascii_tree(struct node* t, FILE* stream) 
{
    asciinode *proot;
    int xmin, i;
    if (t == NULL) return;
    proot = build_ascii_tree(t);
    compute_edge_lengths(proot);
    for (i=0; i<proot->height && i < MAX_HEIGHT; i++) 
    {
        lprofile[i] = INFINITY;
    }
    compute_lprofile(proot, 0, 0);
    xmin = 0;
    for (i = 0; i < proot->height && i < MAX_HEIGHT; i++) 
    {
        xmin = MIN(xmin, lprofile[i]);
    }
    for (i = 0; i < proot->height; i++) 
    {
        print_next = 0;
        print_level(proot, -xmin, i, stream);
        fprintf(stream, "\n");
    }
    if (proot->height >= MAX_HEIGHT) 
    {
        fprintf(stream, "(This tree is taller than %d, and may be drawn incorrectly.)\n", MAX_HEIGHT);
    }
    free_ascii_tree(proot); 
}


