/* Copyright 1997-2005 The MathWorks, Inc. */

/*
 * MRANKP.C
 * "Posix" C main program illustrating the use of the MATLAB Math Library.
 * Calls mlfMrank, obtained by using MCC to compile mrank.m.
 *
 * $Revision: 1.3.6.5 $
 *
 */

#include <stdio.h>
#include <math.h>
#include "libPkg.h"
#include "main_for_lib.h"


#define seterr(x)  ((inputs*)in)->err=x

void *run_main( void *in )
{
    mxArray *N;    /* Matrix containing n. */
    mxArray *R = NULL;    /* Result matrix. */
    int      n;    /* Integer parameter from command line. */

    seterr(0); /*reset the error code */
    /* Get any command line parameter. */
    if (((inputs*)in)->ac >= 2) {
        n = atoi(((inputs*)in)->av[1]);
    } else {
        n = 12;
    }

    /* Call the mclInitializeApplication routine. Make sure that the application
     * was initialized properly by checking the return status. This initialization
     * has to be done before calling any MATLAB API's or MATLAB Compiler generated
     * shared library functions. */
    if( !mclInitializeApplication(NULL,0) )
    {
        fprintf(stderr, "Could not initialize the application.\n");
        seterr(-2);
	return in;
    }
    /* Call the library intialization routine and make sure that the
     * library was initialized properly */
    if (!libPkgInitialize())
    {
      fprintf(stderr,"Could not initialize the library.\n");
      seterr(-3);
    }
    else
    {
	/* Create a 1-by-1 matrix containing n. */
        N = mxCreateScalarDouble(n);
      
	/* Call mlfMrank, the compiled version of mrank.m. */
	mlfMrank(1, &R, N);
	
	/* Print the results. */
	mlfPrintmatrix(R);
	
	/* Free the matrices allocated during this computation. */
	mxDestroyArray(N);
	mxDestroyArray(R);
	
	libPkgTerminate();    /* Terminate the library of M-functions */
    }
/* On MAC, you need to call mclSetExitCode with the appropriate exit status
 * Also, note that you should call mclTerminate application in the end of
 * your application. mclTerminateApplication terminates the entire 
 * application and exits with the exit code set using mclSetExitCode. Note
 * that this behavior is only on MAC platform.
 */
#ifdef __APPLE_CC__
    mclSetExitCode(((inputs*)in)->err);
#endif
    mclTerminateApplication();
    return in;
}
