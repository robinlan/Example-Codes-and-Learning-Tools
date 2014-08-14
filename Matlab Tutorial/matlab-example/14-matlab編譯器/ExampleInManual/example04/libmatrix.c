/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Mon Feb 11 11:07:26 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:libmatrix" "-W"
 * "lib:libmatrix" "-T" "link:lib" "addmatrix.m" "multiplymatrix.m"
 * "eigmatrix.m" "-v" 
 */

#include <stdio.h>
#define EXPORTING_libmatrix 1
#include "libmatrix.h"
#ifdef __cplusplus
extern "C" {
#endif

extern mclComponentData __MCC_libmatrix_component_data;

#ifdef __cplusplus
}
#endif


static HMCRINSTANCE _mcr_inst = NULL;


#if defined( _MSC_VER) || defined(__BORLANDC__) || defined(__WATCOMC__) || defined(__LCC__)
#include <windows.h>

static char path_to_dll[_MAX_PATH];

BOOL WINAPI DllMain(HINSTANCE hInstance, DWORD dwReason, void *pv)
{
    if (dwReason == DLL_PROCESS_ATTACH)
    {
        char szDllPath[_MAX_PATH];
        char szDir[_MAX_DIR];
        if (GetModuleFileName(hInstance, szDllPath, _MAX_PATH) > 0)
        {
             _splitpath(szDllPath, path_to_dll, szDir, NULL, NULL);
            strcat(path_to_dll, szDir);
        }
	else return FALSE;
    }
    else if (dwReason == DLL_PROCESS_DETACH)
    {
    }
    return TRUE;
}
#endif
static int mclDefaultPrintHandler(const char *s)
{
    return fwrite(s, sizeof(char), strlen(s), stdout);
}

static int mclDefaultErrorHandler(const char *s)
{
    int written = 0, len = 0;
    len = strlen(s);
    written = fwrite(s, sizeof(char), len, stderr);
    if (len > 0 && s[ len-1 ] != '\n')
        written += fwrite("\n", sizeof(char), 1, stderr);
    return written;
}


/* This symbol is defined in shared libraries. Define it here
 * (to nothing) in case this isn't a shared library. 
 */
#ifndef LIB_libmatrix_C_API 
#define LIB_libmatrix_C_API /* No special import/export declaration */
#endif

LIB_libmatrix_C_API 
bool MW_CALL_CONV libmatrixInitializeWithHandlers(
    mclOutputHandlerFcn error_handler,
    mclOutputHandlerFcn print_handler
)
{
    if (_mcr_inst != NULL)
        return true;
    if (!mclmcrInitialize())
        return false;
    if (!mclInitializeComponentInstance(&_mcr_inst,
                                        &__MCC_libmatrix_component_data,
                                        true, NoObjectType, LibTarget,
                                        error_handler, print_handler))
        return false;
    return true;
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV libmatrixInitialize(void)
{
    return libmatrixInitializeWithHandlers(mclDefaultErrorHandler,
                                           mclDefaultPrintHandler);
}

LIB_libmatrix_C_API 
void MW_CALL_CONV libmatrixTerminate(void)
{
    if (_mcr_inst != NULL)
        mclTerminateInstance(&_mcr_inst);
}


LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxAddmatrix(int nlhs, mxArray *plhs[],
                               int nrhs, mxArray *prhs[])
{
    return mclFeval(_mcr_inst, "addmatrix", nlhs, plhs, nrhs, prhs);
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxMultiplymatrix(int nlhs, mxArray *plhs[],
                                    int nrhs, mxArray *prhs[])
{
    return mclFeval(_mcr_inst, "multiplymatrix", nlhs, plhs, nrhs, prhs);
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV mlxEigmatrix(int nlhs, mxArray *plhs[],
                               int nrhs, mxArray *prhs[])
{
    return mclFeval(_mcr_inst, "eigmatrix", nlhs, plhs, nrhs, prhs);
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV mlfAddmatrix(int nargout, mxArray** a
                               , mxArray* a1, mxArray* a2)
{
    return mclMlfFeval(_mcr_inst, "addmatrix", nargout, 1, 2, a, a1, a2);
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV mlfMultiplymatrix(int nargout, mxArray** m
                                    , mxArray* a1, mxArray* a2)
{
    return mclMlfFeval(_mcr_inst, "multiplymatrix", nargout, 1, 2, m, a1, a2);
}

LIB_libmatrix_C_API 
bool MW_CALL_CONV mlfEigmatrix(int nargout, mxArray** e, mxArray* a1)
{
    return mclMlfFeval(_mcr_inst, "eigmatrix", nargout, 1, 1, e, a1);
}
