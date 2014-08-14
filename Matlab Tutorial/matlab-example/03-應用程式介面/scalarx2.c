/* 此函式為 MATLAB 的 MEX 檔案，其輸入為一個純量，輸出則為此純量的兩倍。 */

#include "mex.h"	/* mex.h 包含 mxArray 結構的定義，以及其他相關資訊 */

/* prhs = pointer to the right-hand-side arguments，即指向輸入變數列的指標 */
/* prls = pointer to the  left-hand-side arguments，即指向輸出變數列的指標 */
#define IN  prhs[0]	/* 定義 IN  為此函式的第一個輸入變數，以便後續取用 */
#define OUT plhs[0]	/* 定義 OUT 為此函式的第一個輸出變數，以便後續取用 */

/* 此函式的功能為將 x 的第零個元素乘以2，在將結果送到 y 的第零個元素。 */
/* 此函式將會被 mexFunction 所呼叫。 */
void timestwo(double y[], double x[]) {
	y[0] = 2.0*x[0];
}

/* 此函式為和 MATLAB 溝通的主要函式 */
void mexFunction( int nlhs, mxArray *plhs[],
                  int nrhs, const mxArray *prhs[] ) {
	double *x, *y;
	int    no_rows, no_cols;
  
	/* 檢查輸出和輸入變數個數是否都是1，其中		  */
	/* nrhs = no. of right-hand-side arguments（輸入變數個數）*/
	/* nlhs = no. of  left-hand-side arguments（輸出變數個數）*/
	if(nrhs!=1)	/* 檢查輸入變數個數是否是1 */
		mexErrMsgTxt("One input required.");
	if(nlhs>1)	/* 檢查輸出變數個數是否是1 */
		mexErrMsgTxt("Too many output arguments");
  
	/* 檢查輸入變數是否合格 */
	no_rows = mxGetM(IN);	/* 橫列維度 */
	no_cols = mxGetN(IN);	/* 直行維度 */
	if(!(no_rows==1 && no_cols==1))	/* 檢查輸入變數是否為純量 */
		mexErrMsgTxt("Input must be a scalar.");
	if(mxIsComplex(IN))		/* 檢查輸入變數是否為實數 */
		mexErrMsgTxt("Input must be noncomplex.");
	if(!mxIsDouble(IN))		/* 檢查輸入變數是否為 double */
		mexErrMsgTxt("Input must be a double.");
  
	/* 配置記憶體給輸出變數 */
	OUT = mxCreateDoubleMatrix(no_rows, no_cols, mxREAL);
  
	/* 取得輸入和輸出變數的指標 */
	x = mxGetPr(IN);
	y = mxGetPr(OUT);
  
	/* 執行實際的運算：將輸入純量乘以2 */
	timestwo(y, x);
}
