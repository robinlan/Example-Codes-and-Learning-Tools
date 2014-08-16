# Microsoft Developer Studio Generated NMAKE File, Based on cartesian.dsp
!IF "$(CFG)" == ""
CFG=cartesian - Win32 Debug
!MESSAGE No configuration specified. Defaulting to cartesian - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "cartesian - Win32 Release" && "$(CFG)" != "cartesian - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "cartesian.mak" CFG="cartesian - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "cartesian - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "cartesian - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

CPP=cl.exe
MTL=midl.exe
RSC=rc.exe

!IF  "$(CFG)" == "cartesian - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release

ALL : "\prog\cartesian\test\cartesian.exe"


CLEAN :
	-@erase "$(INTDIR)\Cartesian.obj"
	-@erase "$(INTDIR)\example.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "\prog\cartesian\test\cartesian.exe"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /MD /GX /Os /Ob2 /I "." /I ".." /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "WIN32_LEAN_AND_MEAN" /D "VC_EXTRA_LEAN" /D "WIN32_EXTRA_LEAN" /Fp"$(INTDIR)\cartesian.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /o "NUL" /win32 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\cartesian.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=fltk.lib wsock32.lib comctl32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib /nologo /subsystem:windows /incremental:no /pdb:"$(OUTDIR)\cartesian.pdb" /machine:I386 /nodefaultlib:"libcd" /out:"../test/cartesian.exe" /libpath:"..\lib" 
LINK32_OBJS= \
	"$(INTDIR)\Cartesian.obj" \
	"$(INTDIR)\example.obj"

"\prog\cartesian\test\cartesian.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "cartesian - Win32 Debug"

OUTDIR=.\cartesian_
INTDIR=.\cartesian_
# Begin Custom Macros
OutDir=.\cartesian_
# End Custom Macros

ALL : "\prog\cartesian\test\cartesiand.exe" "$(OUTDIR)\cartesian.bsc"


CLEAN :
	-@erase "$(INTDIR)\Cartesian.obj"
	-@erase "$(INTDIR)\Cartesian.sbr"
	-@erase "$(INTDIR)\example.obj"
	-@erase "$(INTDIR)\example.sbr"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\cartesian.bsc"
	-@erase "$(OUTDIR)\cartesiand.pdb"
	-@erase "\prog\cartesian\test\cartesiand.exe"
	-@erase "\prog\cartesian\test\cartesiand.ilk"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP_PROJ=/nologo /MDd /GX /ZI /Od /I "." /I ".." /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "WIN32_LEAN_AND_MEAN" /D "VC_EXTRA_LEAN" /D "WIN32_EXTRA_LEAN" /FR"$(INTDIR)\\" /Fp"$(INTDIR)\cartesian.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /o "NUL" /win32 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\cartesian.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\Cartesian.sbr" \
	"$(INTDIR)\example.sbr"

"$(OUTDIR)\cartesian.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
LINK32_FLAGS=fltkd.lib wsock32.lib comctl32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib /nologo /subsystem:windows /incremental:yes /pdb:"$(OUTDIR)\cartesiand.pdb" /debug /machine:I386 /nodefaultlib:"libcd" /out:"../test/cartesiand.exe" /pdbtype:sept /libpath:"..\lib" 
LINK32_OBJS= \
	"$(INTDIR)\Cartesian.obj" \
	"$(INTDIR)\example.obj"

"\prog\cartesian\test\cartesiand.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("cartesian.dep")
!INCLUDE "cartesian.dep"
!ELSE 
!MESSAGE Warning: cannot find "cartesian.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "cartesian - Win32 Release" || "$(CFG)" == "cartesian - Win32 Debug"
SOURCE=\prog\cartesian\Cartesian.cpp

!IF  "$(CFG)" == "cartesian - Win32 Release"


"$(INTDIR)\Cartesian.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "cartesian - Win32 Debug"


"$(INTDIR)\Cartesian.obj"	"$(INTDIR)\Cartesian.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=\prog\cartesian\test\example.cpp

!IF  "$(CFG)" == "cartesian - Win32 Release"


"$(INTDIR)\example.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "cartesian - Win32 Debug"


"$(INTDIR)\example.obj"	"$(INTDIR)\example.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 


!ENDIF 

