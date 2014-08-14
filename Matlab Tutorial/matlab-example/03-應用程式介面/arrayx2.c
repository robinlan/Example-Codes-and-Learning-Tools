/* 此函式為 MATLAB 的 MEX 檔案，其輸入為一個向量，輸出則為2乘以此向量。 */

#include "mex.h"	/* mex.h 包含 mxArray 結構的定義，以及其他相關資訊 */

/* prhs = pointer to the right-hand-side arguments，即指向輸入變數列的指標 */
/* prls = pointer to the  left-hand-side arguments，即指向輸出變數列的指標 */
#define IN  prhs[0]	/* 定義 IN  為此函式的第一個輸入變數，以便後續取用 */
#define OUT plhs[0]	/* 定義 OUT 為此函式的第一個輸出變數，以便後續取用 */

/* 此函式的功能為將向量 x 的每一個元素乘以2，再將結果送到 y 向量。 */
/* 此函式將會被 mexFunction 所呼叫。 */
void timestwo(double y[], double x[], int length) {
	int i;
	for (i=0; i<length; i++)
		y[i] = 2.0*x[i];
}

/* 此函式為和 MATLAB 溝通的主要函式 */
void mexFunction( int nlhs, mxArray *plhs[],
                  int nrhs, const mxArray *prhs[] ) {
	double *xr, *xi, *yr, *yi;
	int length = mxGetM(IN)*mxGetN(IN);
  
	/* 檢查輸出和輸入變數個數是否都是1，其中		  */
	/* nrhs = no. of right-hand-side arguments（輸入變數個數）*/
	/* nlhs = no. of  left-hand-side arguments（輸出變數個數）*/
	if(nrhs!=1)	/* 檢查輸入變數個數是否是1 */
		mexErrMsgTxt("One input required.");
	if(nlhs>1)	/* 檢查輸出變數個數是否是1 */
		mexErrMsgTxt("Too many output arguments");
  
	/* 檢查輸入變數是否合格 */
	if(!mxIsDouble(IN))		/* 檢查輸入變數是否為 double */
		mexErrMsgTxt("Input must be a double.");
  
	/* 配置記憶體給輸出變數 */
	OUT = mxCreateDoubleMatrix(mxGetM(IN), mxGetN(IN), mxCOMPLEX);
  
	/* 取得輸入和輸出變數的實部指標 */
	xr = mxGetPr(IN);
	yr = mxGetPr(OUT);
	/* 取得輸入和輸出變數的虛部指標 */
	if (mxIsComplex(IN)) {
		xi = mxGetPi(IN);
		yi = mxGetPi(OUT);
	}
  
	/* 執行實際的運算：將輸入向量的實部乘以2 */
	timestwo(yr, xr, length);
	/* 執行實際的運算：將輸入向量的虛部乘以2 */
	if (mxIsComplex(IN))
		timestwo(yi, xi, length);
}
