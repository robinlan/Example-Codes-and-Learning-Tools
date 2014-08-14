A{1,1} = 'this is the first cell.';
A{1,2} = [5+j*6, 4+j*5];
A{2,1} = [1 2 3; 4 5 6; 7 8 9];
A{2,2} = {'Tim'; 'Chris'};
cellplot(A)			% 以圖形的方式顯示異質陣列 A 的內部資料型態