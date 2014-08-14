/*=======================================================
 * 此範例程式可以呼叫 MATLAB 編譯器所產生的程式庫 matLib
 * Roger Jang, 20080211
 *=======================================================*/

#include <stdio.h>
/* 導入 MCR 標頭檔以及（由 MATLAB 編譯器產生）特定應用相關的標頭檔 */
#include "matLib.h"	


/* 顯示C資料型態為 mxArray 的雙倍精準陣列 */
void matDisplay(const mxArray* in){
	int i, j;
	int rowNum = mxGetM(in);	/* 橫列個數 */
	int colNum = mxGetN(in);	/* 直行個數 */
	double *data = mxGetPr(in);	/* 指到輸入陣列中的雙倍精準資料 */
    
	/* 印出資料 */
	for(i=0; i<colNum; i++){
		for(j=0; j<rowNum; j++)
			printf("%4.2f\t", data[j*colNum+i]);
		printf("\n");
	}
	printf("\n");
}

/* 主要工作函式 */
void *mainFunction(int *errorCode){
	mxArray *in1, *in2;	/* 定義送到程式庫函式的輸入矩陣 */
	mxArray *out = NULL;	/* 定義程式庫函式的輸出矩陣 */
	double data[] = {1, 3, 5, 2, 4, 6, 0, 8, 9};	/* 測試用的資料 */

	/* 呼叫 mclInitializeApplication() 以確認此應用程式已經被正確地啟始 */
	if(!mclInitializeApplication(NULL, 0)){
		fprintf(stderr, "錯誤：無法啟始應用程式！\n");
		*errorCode=-1;
		return;
	}
	/* 呼叫 matLibInitialize() 以確認 MATLAB編譯器的程式庫已經被正確地啟始 */
	if (!matLibInitialize()){
		fprintf(stderr,"錯誤：無法啟始程式庫！\n");
		*errorCode=-2;
		return;
	}
    
	/* 創造輸入矩陣 */
	in1 = mxCreateDoubleMatrix(3, 3, mxREAL);
	in2 = mxCreateDoubleMatrix(3, 3, mxREAL);
	memcpy(mxGetPr(in1), data, 9*sizeof(double));	/* 將 data 中的資料拷貝至 in1 */
	memcpy(mxGetPr(in2), data, 9*sizeof(double));	/* 將 data 中的資料拷貝至 in2 */
    
        /* 呼叫由 MATLAB 編譯器產生的程式庫 matLib */
	/* 計算矩陣相乘 */
	mlfMyMatMultiply(1, &out, in1, in2);
	printf("矩陣相乘的結果：\n"); matDisplay(out);
	mxDestroyArray(out); out=NULL;	/* 收回配置給 out 的記憶體 */
	/* 計算反矩陣 */
	mlfMyMatInv(1, &out, in1);
	printf("計算反矩陣的結果：\n"); matDisplay(out);
	mxDestroyArray(out); out=NULL;	/* 收回配置給 out 的記憶體 */

	/* 收回配置給 in1 和 in2 的記憶體 */
	mxDestroyArray(in1); in1=NULL;
	mxDestroyArray(in2); in2=NULL;

	/* 呼叫 matLibTerminate() 以終止應用程式庫 */
	matLibTerminate();
	/* 呼叫 mclTerminateApplication() 以終止此應用程式 */
	mclTerminateApplication();
}

/* 主程式 */
int main(){
	int errorCode=0;
	mainFunction(&errorCode);
	return(errorCode);
}