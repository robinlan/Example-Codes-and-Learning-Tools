// 此範例說明如何由 MATLAB 的結構陣列轉成 C 的結構陣列
// Roger Jang, 20050402

#include "mex.h"
#include <string.h>

#define	IN	prhs[0]		// 定義輸出變數

typedef struct {
	char *name;
	double phone;
} PHONEBOOK;
PHONEBOOK *friend;	// C 的結構陣列

void
mexFunction(int nlhs,mxArray *plhs[],int nrhs,const mxArray *prhs[]) {
	int i, elementNum, strLen, status;

	// 檢查輸入和輸出參數的個數
	if (nrhs<1) mexErrMsgTxt("One input argument required.");
	if (nlhs>0) mexErrMsgTxt("No output argument required.");

	elementNum = mxGetNumberOfElements(IN);		// 元素個數
	friend = (PHONEBOOK *)malloc(elementNum*sizeof(PHONEBOOK));	// 產生 C 的結構陣列
    
	// 填入 C 結構陣列的欄位值
	for (i=0; i<elementNum; i++) {
		// 填入 phone 欄位
		mxArray *fieldValue;
		fieldValue = mxGetField(IN, i, "phone");
		friend[i].phone = *mxGetPr(fieldValue);
		// 填入 name 欄位
		fieldValue = mxGetField(IN, i, "name");
		strLen = (2*mxGetM(fieldValue)*mxGetN(fieldValue))+1;	// 乘上2, 以對付 2-byte 中文字
		friend[i].name = (char *)malloc(strLen*sizeof(char));
		status = mxGetString(fieldValue, friend[i].name, strLen);
		if(status != 0) 
			mexWarnMsgTxt("Not enough space. String is truncated.");
	}

	// 列印 C 結構陣列
	for (i=0; i<elementNum; i++) {
		printf("friend[%d].name = %s\n", i, friend[i].name);
		printf("friend[%d].phone = %f\n", i, friend[i].phone);
	}

	// Free memory
	for (i=0; i<elementNum; i++)
		free(friend[i].name);
	free(friend);
}