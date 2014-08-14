// 此範例說明如何由 C 的結構陣列轉成 MATLAB 的結構陣列
// Roger Jang, 20050402

#include "mex.h"
#include <string.h>

#define	OUT	plhs[0]		// 定義輸出變數

struct phonebook {
	const char *name;
	double phone;
};
// C 的結構陣列
struct phonebook friends[] = {{"李宏儒", 3486},{"林政源",3712}, {"陳江村", 2248}};
// 用於 MATLAB 結構陣列的欄位名稱
const char *fieldNames[] = {"name", "phone"};

void
mexFunction(int nlhs,mxArray *plhs[],int nrhs,const mxArray *prhs[]) {
	int structNum = sizeof(friends)/sizeof(struct phonebook);	// 結構陣列的長度
	int fieldNum = sizeof(fieldNames)/sizeof(*fieldNames);		// 欄位的個數
	int dims[2] = {1, structNum};					// 結構陣列的維度
	int i, nameFieldIndex, phoneFieldIndex;

	// 檢查輸入和輸出參數的個數
	if (nrhs>0) mexErrMsgTxt("No input argument required.");
	if (nlhs>1) mexErrMsgTxt("Too many output arguments.");
    
	// 產生輸出結構陣列
	OUT = mxCreateStructArray(2, dims, fieldNum, fieldNames);

	// 取得欄位名稱對應的索引值，以便使用 mxSetFieldByNumber() 對欄位值進行設定
	nameFieldIndex = mxGetFieldNumber(OUT, "name");
	phoneFieldIndex = mxGetFieldNumber(OUT, "phone");

	// 填入 MATLAB 結構陣列的欄位值
	for (i=0; i<structNum; i++) {
		mxArray *fieldValue;
		// 填入欄位名稱 name 的值（有兩種方法）
	//	mxSetField(OUT, i, "name", mxCreateString(friends[i].name));			// 方法一：效率較低
		mxSetFieldByNumber(OUT, i, nameFieldIndex, mxCreateString(friends[i].name));	// 方法二：效率較高
		// 填入欄位名稱 phone 的值（有兩種方法）
		fieldValue = mxCreateDoubleMatrix(1, 1, mxREAL);
		*mxGetPr(fieldValue) = friends[i].phone;
	//	mxSetField(OUT, i, "phone", fieldValue);					// 方法一：效率較低
		mxSetFieldByNumber(OUT, i, phoneFieldIndex, fieldValue);	// 方法二：效率較高
	}
}
