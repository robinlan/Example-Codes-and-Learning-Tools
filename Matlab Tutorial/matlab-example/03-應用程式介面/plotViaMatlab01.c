/* 此範例說明如何在微軟的視窗環境下，由 C 程式來呼叫 MATLAB 引擎 */
#include <windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <direct.h>
#include "mex.h"
#include "engine.h"

#define BUFSIZE 256

int PASCAL WinMain (HINSTANCE hInstance,
                    HINSTANCE hPrevInstance,
                    LPSTR     lpszCmdLine,
                    int       nCmdShow){
	char buffer[BUFSIZE];
	mxArray *app;
	Engine *ep;

	/* 啟動 MATLAB 引擎 */
	if (!(ep = engOpen(NULL))){		// 產生一個 MATLAB 引擎物件
		MessageBox ((HWND)NULL, (LPSTR)"Can't start MATLAB engine",
			(LPSTR)"plotViaMatlab01.c", MB_OK);
		exit(-1);
	}

	/* 切換目錄並執行 plotSine.m */
	_getcwd(buffer, BUFSIZE);		// 將此程式所在目錄存入字串 buffer
	app = mxCreateString(buffer);		// 產生 MATLAB 內部的字串變數 app
	engPutVariable(ep, "appDir", app);	// 將字串變數 app 至入工作空間的變數 appDir
	engEvalString(ep, "cd(appDir)");	// 將 MATLAB 的工作目錄切換至字串 appDir 所指定的目錄
	engEvalString(ep, "plotSine");		// 執行同目錄下的 plotSine.m
	
	/* 取得 MATLAB 輸出訊息 */
	engOutputBuffer(ep, buffer, BUFSIZE);	// 設定 buffer 可以接收 MATLAB 的輸出訊息
	engEvalString(ep, "whos");		// 在 MATLAB 引擎執行 whos 指令
	MessageBox((HWND)NULL, (LPSTR)buffer, (LPSTR) "MATLAB - whos", MB_OK);		// 顯示 buffer 的內容

	engClose(ep);				// 最後關閉 MATLAB 引擎
	return(0);
}