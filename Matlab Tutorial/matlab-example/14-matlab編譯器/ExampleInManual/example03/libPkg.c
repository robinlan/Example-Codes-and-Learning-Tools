/*
 * MATLAB Compiler: 4.4 (R2006a)
 * Date: Fri Feb 08 19:43:27 2008
 * Arguments: "-B" "macro_default" "-W" "lib:libPkg" "-T" "link:exe" "mrank"
 * "printmatrix" "mrankp.c" "main_for_lib.c" 
 */

#include <stdio.h>
#define EXPORTING_libPkg 1
#include "libPkg.h"
#ifdef __cplusplus
extern "C" {
#endif

extern mclComponentData __MCC_libPkg_component_data;

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
#ifndef LIB_libPkg_C_API 
#define LIB_libPkg_C_API /* No special import/export declaration */
#endif

LIB_libPkg_C_API 
bool MW_CALL_CONV libPkgInitializeWithHandlers(
    mclOutputHandlerFcn error_handler,
    mclOutputHandlerFcn print_handler
)
{
    if (_mcr_inst != NULL)
        return true;
    if (!mclmcrInitialize())
        return false;
    if (!mclInitializeComponentInstance(&_mcr_inst,
                                        &__MCC_libPkg_component_data,
                                        true, NoObjectType, LibTarget,
                                        error_handler, print_handler))
        return false;
    return true;
}

LIB_libPkg_C_API 
bool MW_CALL_CONV libPkgInitialize(void)
{
    return libPkgInitializeWithHandlers(mclDefaultErrorHandler,
                                        mclDefaultPrintHandler);
}

LIB_libPkg_C_API 
void MW_CALL_CONV libPkgTerminate(void)
{
    if (_mcr_inst != NULL)
        mclTerminateInstance(&_mcr_inst);
}


LIB_libPkg_C_API 
bool MW_CALL_CONV mlxMrank(int nlhs, mxArray *plhs[],
                           int nrhs, mxArray *prhs[])
{
    return mclFeval(_mcr_inst, "mrank", nlhs, plhs, nrhs, prhs);
}

LIB_libPkg_C_API 
bool MW_CALL_CONV mlxPrintmatrix(int nlhs, mxArray *plhs[],
                                 int nrhs, mxArray *prhs[])
{
    return mclFeval(_mcr_inst, "printmatrix", nlhs, plhs, nrhs, prhs);
}

LIB_libPkg_C_API 
bool MW_CALL_CONV mlfMrank(int nargout, mxArray** r, mxArray* n)
{
    return mclMlfFeval(_mcr_inst, "mrank", nargout, 1, 1, r, n);
}

LIB_libPkg_C_API 
bool MW_CALL_CONV mlfPrintmatrix(mxArray* m)
{
    return mclMlfFeval(_mcr_inst, "printmatrix", 0, 0, 1, m);
}
