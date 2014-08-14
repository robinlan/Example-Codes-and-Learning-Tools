#include <math.h>
#include "mex.h"	/* mex.h 包含 mxArray 結構的定義，以及其他相關資訊 */

/* prhs = pointer to the right-hand-side arguments，即指向輸入變數列的指標 */
/* prls = pointer to the  left-hand-side arguments，即指向輸出變數列的指標 */
#define	MAT1 prhs[0] /* 定義 MAT1 為此函式的第一個輸入變數，以便後續取用 */
#define	MAT2 prhs[1] /* 定義 MAT1 為此函式的第二個輸入變數，以便後續取用 */
#define	OUT  plhs[0] /* 定義 OUT  為此函式的第一個輸出變數，以便後續取用 */

/* 此函式為和 MATLAB 溝通的主要函式 */
void mexFunction(
	int nlhs,	mxArray *plhs[],
	int nrhs, const mxArray *prhs[]) {
	double	*out, *mat1, *mat2, square_sum;
	int m, p, n, p2, i, j, k;

	/* 檢查輸出變數個數是否為2、輸入變數個數是否為1，其中	  */
	/* nrhs = no. of right-hand-side arguments（輸入變數個數）*/
	/* nlhs = no. of  left-hand-side arguments（輸出變數個數）*/
	if (nrhs!=2)
		mexErrMsgTxt("PAIRDIST requires two input arguments.");

	/* 檢查維度是否符合要求 */
	m  = mxGetM(MAT1);
	p  = mxGetN(MAT1);
	n  = mxGetM(MAT2);
	p2 = mxGetN(MAT2);
	if (p != p2)
		mexErrMsgTxt("Matrix sizes mismatch!");

	/* 檢查輸入變數資料型態是否符合要求 */
	if (!mxIsNumeric(MAT1) || mxIsSparse(MAT1)  || !mxIsDouble(MAT1)) {
		mexErrMsgTxt("Input 1 is not a full numerical array!");
	}
	if (!mxIsNumeric(MAT2) || mxIsSparse(MAT2)  || !mxIsDouble(MAT2)) {
		mexErrMsgTxt("Input 2 is not a full numerical array!");
	}

	/* 配置記憶體給輸出變數 */
	OUT = mxCreateDoubleMatrix(m, n, mxREAL);

	/* 取得輸入和輸出變數的實部指標 */
	out = mxGetPr(OUT);
	mat1 = mxGetPr(MAT1);
	mat2 = mxGetPr(MAT2);

	/* 實際計算部份 */
	/* MAT1(i, j) 的值是 mat1[(j-1)*m+(i-1)] */
	/* MAT2(i, j) 的值是 mat2[(j-1)*n+(i-1)] */
	/*  OUT(i, j) 的值是  out[(j-1)*m+(i-1)] */

	for (i=1; i<=m; i++)
		for (j=1; j<=n; j++) {
			square_sum = 0;
			for (k=1; k<=p; k++)
				square_sum += pow(mat1[(k-1)*m+(i-1)]-mat2[(k-1)*n+(j-1)], 2.0);
			out[(j-1)*m+(i-1)] = sqrt(square_sum);
		}
}
