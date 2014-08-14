clear all		                   % 清除所有變數
for i = 3:6    
	eval(['x', int2str(i) , '= magic(' , int2str(i) , ') ; ']);  
end  
whos x*