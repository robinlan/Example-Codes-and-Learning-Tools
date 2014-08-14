% fplot 指令可同時對多個函數作圖
fplot(@(x)[sin(x), exp(-x)], [0, 10])