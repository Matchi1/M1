#==================================================
# version 6c (25 mars 2020) -
#==================================================

DEBUG = no
CPP   = no
VERS  = .6d
PROC  = .$(PROCBIT)

ifeq ($(CPP),yes)
	# clang++ n'accepte pas les réels 1.0 sous type <float> => IL VEUT 1.0f => génère une erreur
	# g++ n'aime pas non plus, mais se contente d'un warning
	CC   = g++ -std=c++11
	LANG = ++
else
	CC = clang -std=c17
	LANG =
endif

ifeq ($(DEBUG),yes)
  CFLAGS  = -O2 -g -Wpointer-arith -Wall
  DBG = .dbg
else
  CFLAGS = -O2
  DBG =
endif

EXT = $(VERS)$(PROC)$(LANG)$(DBG)

lib = -L/usr/lib -lm -lGL -lGLU -lglut
inc = -I/usr/include -I./include
src =  src/

# cible pour les binaires (.a,.so)
bin = .

# liste des modules
modules = g2x_window.o g2x_geom.o g2x_colors.o g2x_tools.o g2x_capture.o g2x_draw.o g2x_transfo.o g2x_pnm.o g2x_geoalgo.o g2x_polygon.o

all : g2x clean

# compilation generique d'un module %.o a partir de src/%.c
%.o : $(src)%.c
	@echo "module $@"
	@$(CC) -fpic $(CFLAGS) $(inc) -c $< -o $@
	@echo "------------------------"

# comilation generique d'un executable - edition de liens
% : $(modules) %.o
	@echo "assemblage [$^]->$@"
	$(CC) $^ $(lib) -o $@$(EXT)
	@echo "------------------------"

# compilation de libg2x
g2x : $(modules)
	@echo "massemblage bibliotheque dynamique $(bin)/lib$@$(EXT).so"
	@$(CC) -fpic -shared $^ $(lib) -o $(bin)/lib$@$(EXT).so
	@echo "assemblage bibliotheque d'archive $(bin)/lib$@$(EXT).a"
	@ar r $(bin)/lib$@$(EXT).a $^
	@echo "------------------------                                "

.PHONY : clean
clean :
	rm -f *.o
