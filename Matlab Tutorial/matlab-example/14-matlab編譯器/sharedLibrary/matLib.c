/*
 * MATLAB Compiler: 4.3 (R14SP3)
 * Date: Sat Jul 05 11:32:39 2008
 * Arguments: "-B" "macro_default" "-B" "csharedlib:matLib" "-W" "lib:matLib"
 * "-T" "link:lib" "myMatInv.m" "myMatMultiply.m" 
 */

#include <stdio.h>
#define EXPORTING_matLib 1
#include "matLib.h"
#ifdef __cplusplus
extern "C" {
#endif

extern mclComponentData __MCC_matLib_component_data;

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
#ifndef LIB_matLib_C_API 
#define LIB_matLib_C_API /* No special import/export declaration */
#endif

LIB_matLib_C_API 
bool matLibInitializeWithHandlers(
    mclOutputHandlerFcn error_handler,
    mclOutputHandlerFcn print_handler
)
{
    if (_mcr_inst != NULL)
        return true;
    if (!mclmcrInitialize())
        return false;
    if (!mclInitializeComponentInstance(&_mcr_inst,
                                        &__MCC_matLib_component_data,
                                        true, NoObjectType, LibTarget,
                                        error_handler, print_handler))
        return false;
    return true;
}

LIB_matLib_C_API 
bool matLibInitialize(void)
{
    return matLibInitializeWithHandlers(mclDefaultErrorHandler,
                                        mclDefaultPrintHandler);
}

LIB_matLib_C_API 
void matLibTerminate(void)
{
    if (_mcr_inst != NULL)
        mclTerminateInstance(&_mcr_inst);
}


LIB_matLib_C_API 
void mlxMyMatInv(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[])
{
    mclFeval(_mcr_inst, "myMatInv", nlhs, plhs, nrhs, prhs);
}

LIB_matLib_C_API 
void mlxMyMatMultiply(int nlhs, mxArray *plhs[], int nrhs, mxArray *prhs[])
{
    mclFeval(_mcr_inst, "myMatMultiply", nlhs, plhs, nrhs, prhs);
}

LIB_matLib_C_API 
void mlfMyMatInv(int nargout, mxArray** invMat, mxArray* mat)
{
    mclMlfFeval(_mcr_inst, "myMatInv", nargout, 1, 1, invMat, mat);
}

LIB_matLib_C_API 
void mlfMyMatMultiply(int nargout, mxArray** out
                      , mxArray* mat1, mxArray* mat2)
{
    mclMlfFeval(_mcr_inst, "myMatMultiply", nargout, 1, 2, out, mat1, mat2);
}
