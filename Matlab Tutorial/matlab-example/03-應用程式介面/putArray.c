/*=================================================================
 * putarray.c 
 *
 * This example demonstrates how to use mexPurVariable and mexFunctionName.
 * This function takes an input argument, multiplies it by two, and put
 * it into MATLAB base workspace under a variable name "putarray_output".
 *
 * Roger Jang, 19991003, 20070223
/*=================================================================*/
 
#include <stdio.h>
#include <string.h>
#include <math.h>
#include "mex.h"	/* mex.h 包含 mxArray 結構的定義，以及其他相關資訊 */

/* prhs = pointer to the right-hand-side arguments，即指向輸入變數列的指標 */
#define IN prhs[0]	/* 定義 IN 為此函式的第一個輸入變數，以便後續取用 */

/* 此函式為和 MATLAB 溝通的主要函式 */
void
mexFunction(int nlhs,mxArray *plhs[],int nrhs,const mxArray *prhs[]) { 
	char arrayName[40];
	mxArray *arrayPtr;
	int i, status;
    
	/* 檢查輸出變數個數是否為0、輸入變數個數是否為1，其中      */
	/* nrhs = no. of right-hand-side arguments（輸入變數個數） */
	/* nlhs = no. of  left-hand-side arguments（輸出變數個數） */
	if (nrhs!=1) { mexErrMsgTxt("One input arguments required."); } 
	if (nlhs>1) { mexErrMsgTxt("Too many output arguments."); }

	/* 檢查輸入變數資料型態是否符合要求 */
	if (!mxIsDouble(IN) || mxIsComplex(IN))
		mexErrMsgTxt("Input must be real double.");
    
	/* 產生MATLAB基本工作空間的變數名稱，等於此MEX主檔名加上「_output」 */
	strcpy(arrayName, mexFunctionName());
	strcat(arrayName,"_output");

	/* 產生一個資料型態為double的陣列 */
	arrayPtr = mxCreateDoubleMatrix(mxGetM(IN), mxGetN(IN), mxREAL);

	/* 將此陣列的實部乘以2 */
	for (i=0; i<mxGetM(IN)*mxGetN(IN); i++)
		mxGetPr(arrayPtr)[i] = 2*(mxGetPr(IN)[i]);

	/* 將此陣列送到MATLAB的基本工作空間 */
	status = mexPutVariable("base", arrayName, arrayPtr);
    
	if (status==1){	/* 無法將此陣列送到MATLAB的基本工作空間，印出失敗訊息 */
		mexPrintf("Variable %s\n", arrayName);
		mexErrMsgTxt("Could not put variable in the base workspace.\n");
	}
	/* 成功地將此陣列送到MATLAB的基本工作空間，印出成功訊息 */
	mexPrintf("\"%s\" is created in the base workspace.\n", arrayName);
    
	/* 刪除先前產生的陣列，將佔用的記憶體歸還給作業系統*/
	mxDestroyArray(arrayPtr);
}
