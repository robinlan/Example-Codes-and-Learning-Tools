/* 此範例說明如何在微軟的視窗環境下，由 C 程式來呼叫 MATLAB 引擎 */
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "engine.h"

int PASCAL WinMain (HINSTANCE hInstance,
                    HINSTANCE hPrevInstance,
                    LPSTR     lpszCmdLine,
                    int       nCmdShow){
	Engine *ep;
	mxArray *T;
	double time[10] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	/* 啟動 MATLAB 引擎 */
	if (!(ep = engOpen(NULL))) {
		MessageBox ((HWND)NULL, (LPSTR)"Can't start MATLAB engine", 
			(LPSTR)"plotViaMatlab02.c", MB_OK);
		exit(-1);
	}
	
	T = mxCreateDoubleMatrix(1, 10, mxREAL);	// 產生一個 MATLAB 的內部變數 T
	memcpy((char *)mxGetPr(T), (char *)time, 10*sizeof(double));	// 將 time 的值拷貝到 T
	engPutVariable(ep, "T", T);					// 將 T 的值送到 MATLAB 工作空間的變數 T

	/* 自由落體的位移對時間的函數：distance = (1/2)g.*t.^2 */
	engEvalString(ep, "D = .5.*(-9.8).*T.^2;");			// 將字串送到 MATLAB 去執行
	engEvalString(ep, "plot(T, D, 'o-');");				// 畫出執行結果
	engEvalString(ep, "title('自由落體的距離與時間關係圖');");
	engEvalString(ep, "xlabel('時間 (秒)');");
	engEvalString(ep, "ylabel('距離 (米)');");

	mxDestroyArray(T);			// 移除 MATLAB 內部變數 T
	engClose(ep);				// 最後關閉 MATLAB 引擎
	return(0);
}