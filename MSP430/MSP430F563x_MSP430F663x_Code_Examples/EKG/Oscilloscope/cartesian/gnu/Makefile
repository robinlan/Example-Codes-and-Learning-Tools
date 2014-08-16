# Standard defines:
CXX  	=	g++

LIBS = -L/usr/local/lib/mingw `fltk-config --ldflags` -lm -lpthreadGC

CXX_FLAGS	=	-O -Wall `fltk-config --cxxflags`

C_FLAGS	=	-O -Wall `fltk-config --cflags` -DWIN32

LD_FLAGS	=	-O -Wall `fltk-config --ldflags`

SRCS	=	../Cartesian.cpp ../scope/scope.cpp ../scope/serial.c

OBJS	=	Cartesian.o scope.o serial.o

ALLOBJS	=	$(EXOBJS)

ALLBIN	=	scope
ALLTGT	=	scope

# User defines:

#@# Targets follow ---------------------------------

all:	$(ALLTGT)

objs:	$(ALLOBJS)

cleanobjs:
	rm -f $(ALLOBJS)

cleanbin:
	rm -f $(ALLBIN)

clean:	cleanobjs cleanbin

cleanall:	cleanobjs cleanbin

#@# Dependency rules follow -----------------------------

scope: $(OBJS)
	$(CXX) $(CXX_FLAGS) -o scope $(OBJS) $(LIBS)

Cartesian.o: ../Cartesian.cpp ../Cartesian.H
	$(CXX) $(CXX_FLAGS) -c -o $@ $<

scope.o: ../scope/scope.cpp ../Cartesian.H ../scope/serial.h
	$(CXX) $(CXX_FLAGS) -c -o $@ $<

serial.o: ../scope/serial.c ../scope/serial.h
	$(CC) $(C_FLAGS) -c -o $@ $<
