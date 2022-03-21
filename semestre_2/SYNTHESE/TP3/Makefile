# compiler
CC = clang

DEBUG = no

ifeq ($(DEBUG),yes)
  CFLAGS  = -g -Wpointer-arith -Wall -std=c17
else
  CFLAGS  = -O2 -std=c17
endif

lib = $(libG3X) $(DBG_LIB)
inc = -I./include $(incG3X)
src = src/

# cible pour les binaires (.a,.so)
bin = .

# règle générique de création de xxx.o à partir de src/xxx.c
%.o : $(src)%.c
	@echo "module $@"
	@$(CC) $(CFLAGS) $(inc) -c $< -o $@
	@echo "------------------------"

# règle générique de création de l'executable xxx à partir de src/xxx.c (1 seul module)
% : %.o
	@echo "assemblage [$^]->$@"
	@$(CC) $^ $(lib) -o $@
	@echo "------------------------"

.PHONY : clean cleanall ?

# informations sur les paramètres de compilation
? :
	@echo "---------compilation informations----------"
	@echo "  processor      : $(PROCBIT)"
	@echo "  compiler       : $(CC)"
	@echo "  options        : $(CFLAGS)"
	@echo "  lib g3Ox/OpenGl : $(libG3X)$(COMP)"
	@echo "  headers        : $(incG3X)"
clean :
	@rm -f *.o
cleanall :
	@rm -f *.o g2x_squelette g2x_example

