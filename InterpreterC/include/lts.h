
#ifndef LTS_H
#define LTS_H

#include "interpreter.h"

//#define LTS_BUFFER      4096

void lts_interpreting(void);
void lts_printer(FILE* stream, char line_prefix[], int print_full);


#endif /* LTS_H */
