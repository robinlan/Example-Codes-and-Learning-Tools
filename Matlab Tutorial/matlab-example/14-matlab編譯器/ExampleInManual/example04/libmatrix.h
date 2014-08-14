/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Mon Feb 11 11:07:26 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:libmatrix" "-W"
 * "lib:libmatrix" "-T" "link:lib" "addmatrix.m" "multiplymatrix.m"
 * "eigmatrix.m" "-v" 
 */

#ifndef __libmatrix_h
#define __libmatrix_h 1

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

#ifdef EXPORTING_libmatrix
#define PUBLIC_libmatrix_C_API __global
#else
#define PUBLIC_libmatrix_C_API /* No import statement needed. */
#endif

#define LIB_libmatrix_C_API PUBLIC_libmatrix_C_API

#elif defined(_HPUX_SOURCE)

#ifdef EXPORTING_libmatrix
#define PUBLIC_libmatrix_C_API __declspec(dllexport)
#else
#define PUBLIC_libmatrix_C_API __declspec(dllimport)
#endif

#define LIB_libmatrix_C_API PUBLIC_libmatrix_C_API


#else

#define LIB_libmatrix_C_API

#endif

/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_libmatrix_C_API 
#define LIB_libmatrix_C_API /* No special import/export declaration */
#endif

extern LIB_libmatrix_C_API 
bool MW_CALL_CONV libmatrixInitializeWithHandlers(mclOutputHandlerFcn error_handler,
                                                  mclOutputHandlerFcn print_handler);

extern LIB_libmatrix_C_API 
bool MW_CALL_CONV libmatrixInitialize(void);

extern LIB_libmatrix_C_API 
void MW_CALL_CONV libmatrixTerminate(void);


extern LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxAddmatrix(int nlhs, mxArray *plhs[],
                               int nrhs, mxArray *prhs[]);

extern LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxMultiplymatrix(int nlhs, mxArray *plhs[],
                                    int nrhs, mxArray *prhs[]);

extern LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxEigmatrix(int nlhs, mxArray *plhs[],
                               int nrhs, mxArray *prhs[]);


extern LIB_libmatrix_C_API bool MW_CALL_CONV mlfAddmatrix(int nargout
                                                          , mxArray** a
                                                          , mxArray* a1
                                                          , mxArray* a2);

extern LIB_libmatrix_C_API bool MW_CALL_CONV mlfMultiplymatrix(int nargout
                                                               , mxArray** m
                                                               , mxArray* a1
                                                               , mxArray* a2);

extern LIB_libmatrix_C_API bool MW_CALL_CONV mlfEigmatrix(int nargout
                                                          , mxArray** e
                                                          , mxArray* a1);

#ifdef __cplusplus
}
#endif

#endif
