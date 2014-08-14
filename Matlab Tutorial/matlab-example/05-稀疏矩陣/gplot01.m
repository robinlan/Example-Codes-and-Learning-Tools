A = spconvert([1 2 1; 2 3 1; 2 4 1; 3 4 1; 3 5 1; 5 6 1; 4 6 1]);
xy = [0 1; 1 2; 1 0; 2 0; 2 2; 3 1];				% 每一個列向量是一組 (x, y) 座標
gplot(A, xy, '-o')						% 畫出無向圖（Undirected Graph）