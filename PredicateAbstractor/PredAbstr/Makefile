CC=gcc
CFLAGS=-g -std=c++0x -Wall -D __STDC_LIMIT_MACROS -D __STDC_FORMAT_MACROS -O0
CXX=g++

INCLUDE=
LDFLAGS=-lmathsat -lgmpxx -lgmp -lstdc++

all:	pred_abstr

pred_abstr:	main.o Model.o PredAbstr.o
	$(CXX) $(CFLAGS) $(INCLUDE) -o pred_abstr \
		 main.o Model.o PredAbstr.o $(LDFLAGS)

%.o: %.cpp
	$(CXX) $(CFLAGS) $(INCLUDE) $< -c

clean:
	rm -f *.o pred_abstr
