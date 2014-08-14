/*
 * MATLAB Compiler: 4.3 (R14SP3)
 * Date: Sat Jul 05 11:32:39 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:matLib" "-W" "lib:matLib"
 * "-T" "link:lib" "myMatInv.m" "myMatMultiply.m" 
 */

#ifndef __matLib_h
#define __matLib_h 1

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

#ifdef EXPORTING_matLib
#define PUBLIC_matLib_C_API __global
#else
#define PUBLIC_matLib_C_API /* No import statement needed. */
#endif

#define LIB_matLib_C_API PUBLIC_matLib_C_API

#elif defined(_HPUX_SOURCE)

#ifdef EXPORTING_matLib
#define PUBLIC_matLib_C_API __declspec(dllexport)
#else
#define PUBLIC_matLib_C_API __declspec(dllimport)
#endif

#define LIB_matLib_C_API PUBLIC_matLib_C_API


#else

#define LIB_matLib_C_API

#endif

/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_matLib_C_API 
#define LIB_matLib_C_API /* No special import/export declaration */
#endif

extern LIB_matLib_C_API 
bool matLibInitializeWithHandlers(mclOutputHandlerFcn error_handler,
                                  mclOutputHandlerFcn print_handler);

extern LIB_matLib_C_API 
bool matLibInitialize(void);

extern LIB_matLib_C_API 
void matLibTerminate(void);


extern LIB_matLib_C_API 
void mlxMyMatInv(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[]);

extern LIB_matLib_C_API 
void mlxMyMatMultiply(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[]);


extern LIB_matLib_C_API void mlfMyMatInv(int nargout, mxArray** invMat
                                         , mxArray* mat);

extern LIB_matLib_C_API void mlfMyMatMultiply(int nargout, mxArray** out
                                              , mxArray* mat1, mxArray* mat2);

#ifdef __cplusplus
}
#endif

#endif
