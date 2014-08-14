/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Fri Feb 08 19:43:27 2008
 * Arguments: "-B" "macro_default" "-W" "lib:libPkg" "-T" "link:exe" "mrank"
 * "printmatrix" "mrankp.c" "main_for_lib.c" 
 */

#ifndef __libPkg_h
#define __libPkg_h 1

#if defined(__cplusplus) && !defined(mclmcr_h) && defined(__linux__)
#  pragma implementation "mclmcr.h"
#endif
#include "mclmcr.h"
#ifdef __cplusplus
extern "C" {
#endif

#if defined(__SUNPRO_CC)
/* Solaris shared libraries use __global, rather than mapfiles
 * to define the API exported from a shared library. __global is
 * only necessary when building the library -- files including
 * this header file to use the library do not need the __global
 * declaration; hence the EXPORTING_<library> logic.
 */

#ifdef EXPORTING_libPkg
#define PUBLIC_libPkg_C_API __global
#else
#define PUBLIC_libPkg_C_API /* No import statement needed. */
#endif

#define LIB_libPkg_C_API PUBLIC_libPkg_C_API

#elif defined(_HPUX_SOURCE)

#ifdef EXPORTING_libPkg
#define PUBLIC_libPkg_C_API __declspec(dllexport)
#else
#define PUBLIC_libPkg_C_API __declspec(dllimport)
#endif

#define LIB_libPkg_C_API PUBLIC_libPkg_C_API


#else

#define LIB_libPkg_C_API

#endif

/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_libPkg_C_API 
#define LIB_libPkg_C_API /* No special import/export declaration */
#endif

extern LIB_libPkg_C_API 
bool MW_CALL_CONV libPkgInitializeWithHandlers(mclOutputHandlerFcn error_handler,
                                               mclOutputHandlerFcn print_handler);

extern LIB_libPkg_C_API 
bool MW_CALL_CONV libPkgInitialize(void);

extern LIB_libPkg_C_API 
void MW_CALL_CONV libPkgTerminate(void);


extern LIB_libPkg_C_API 
bool MW_CALL_CONV mlxMrank(int nlhs, mxArray *plhs[],
                           int nrhs, mxArray *prhs[]);

extern LIB_libPkg_C_API 
bool MW_CALL_CONV mlxPrintmatrix(int nlhs, mxArray *plhs[],
                                 int nrhs, mxArray *prhs[]);


extern LIB_libPkg_C_API bool MW_CALL_CONV mlfMrank(int nargout, mxArray** r
                                                   , mxArray* n);

extern LIB_libPkg_C_API bool MW_CALL_CONV mlfPrintmatrix(mxArray* m);

#ifdef __cplusplus
}
#endif

#endif
