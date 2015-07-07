
default: help

TARGET	= interpreter

L 		= lexer
P 		= parser
O		= oxout
N       = node
S       = lts
T       = table
K       = ks

LEX 	= flex
YAC 	= yacc
OX		= ox

C		= cc
CFLAGS	= -Wall -std=c99

I		= -I`pwd`/include -I`pwd`/obj -I`pwd`/lib -I/usr/local/include -I/usr/include
LIB     = -L`pwd`/lib -lmathsat_`uname -s`-`uname -m` -L/usr/local/lib -L/usr/lib -lgmpxx -lgmp -lstdc++

CC		= $(C) $(CFLAGS) $(I)

$(TARGET): obj obj/$(P).o obj/$(L).o obj/$(N).o obj/$(K).o obj/$(T).o obj/$(S).o obj/$(TARGET).o
	$(CC) -o $(TARGET) \
    obj/$(P).o obj/$(L).o obj/$(N).o obj/$(K).o obj/$(T).o obj/$(S).o obj/$(TARGET).o \
    $(LIB)

obj:
	mkdir -p obj

obj/$(TARGET).o: src/$(TARGET).c
	$(CC) -o obj/$(TARGET).o -c src/$(TARGET).c

obj/$(K).o: src/$(K).c
	$(CC) -o obj/$(K).o -c src/$(K).c

obj/$(T).o: src/$(T).c
	$(CC) -o obj/$(T).o -c src/$(T).c

obj/$(N).o: src/$(N).c
	$(CC) -o obj/$(N).o -c src/$(N).c

obj/$(S).o: src/$(S).c
	$(CC) -o obj/$(S).o -c src/$(S).c

obj/$(P).o: obj/$(P).y
	$(YAC) -o obj/$(P).c -d obj/$(P).y
	$(CC) -o obj/$(P).o -c obj/$(P).c 2> /dev/null

obj/$(L).o: obj/$(L).l
	$(LEX) -oobj/$(L).c obj/$(L).l
	$(CC) -o obj/$(L).o -c obj/$(L).c 2> /dev/null

obj/$(P).y obj/$(L).l: src/$(P).y src/$(L).l
	$(OX) src/$(P).y src/$(L).l
	mv $(O).l obj/$(L).l
	mv $(O).y obj/$(P).y
#	mv *.h obj/

clean: obj
	rm -f $(TARGET) obj/*

tests:
	cd test; sh test.sh; cd ..

manual:
	cd doc; make; cd ..

help:
	@cat README

