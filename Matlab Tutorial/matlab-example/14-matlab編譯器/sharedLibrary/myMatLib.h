/*
 * MATLAB Compiler: 4.17 (R2012a)
 * Date: Thu Aug 16 20:07:12 2012
 * Arguments: "-B" "macro_default" "-B" "csharedlib:myMatLib" "-W"
 * "lib:myMatLib" "-T" "link:lib" "myMatInv.m" "myMatMultiply.m" "-d"
 * "sharedLibrary" 
 */

#ifndef __myMatLib_h
#define __myMatLib_h 1

#if defined(__cplusplus) && !defined(mclmcrrt_h) && defined(__linux__)
#  pragma implementation "mclmcrrt.h"
#endif
#include "mclmcrrt.h"
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

#ifdef EXPORTING_myMatLib
#define PUBLIC_myMatLib_C_API __global
#else
#define PUBLIC_myMatLib_C_API /* No import statement needed. */
#endif

#define LIB_myMatLib_C_API PUBLIC_myMatLib_C_API

#elif defined(_HPUX_SOURCE)

#ifdef EXPORTING_myMatLib
#define PUBLIC_myMatLib_C_API __declspec(dllexport)
#else
#define PUBLIC_myMatLib_C_API __declspec(dllimport)
#endif

#define LIB_myMatLib_C_API PUBLIC_myMatLib_C_API


#else

#define LIB_myMatLib_C_API

#endif

/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_myMatLib_C_API 
#define LIB_myMatLib_C_API /* No special import/export declaration */
#endif

extern LIB_myMatLib_C_API 
bool MW_CALL_CONV myMatLibInitializeWithHandlers(
       mclOutputHandlerFcn error_handler, 
       mclOutputHandlerFcn print_handler);

extern LIB_myMatLib_C_API 
bool MW_CALL_CONV myMatLibInitialize(void);

extern LIB_myMatLib_C_API 
void MW_CALL_CONV myMatLibTerminate(void);



extern LIB_myMatLib_C_API 
void MW_CALL_CONV myMatLibPrintStackTrace(void);

extern LIB_myMatLib_C_API 
bool MW_CALL_CONV mlxMyMatInv(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[]);

extern LIB_myMatLib_C_API 
bool MW_CALL_CONV mlxMyMatMultiply(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[]);



extern LIB_myMatLib_C_API bool MW_CALL_CONV mlfMyMatInv(int nargout, mxArray** invMat, mxArray* mat);

extern LIB_myMatLib_C_API bool MW_CALL_CONV mlfMyMatMultiply(int nargout, mxArray** out, mxArray* mat1, mxArray* mat2);

#ifdef __cplusplus
}
#endif
#endif
