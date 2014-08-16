#include <cstdlib>
#include <iostream>
#include <string>

using namespace std;

int KMPStrMatching(string T, string P,  int *N) {
    int i = 0;							// 模式的下标变量
    int j = 0;							// 目标的下标变量
    int pLen = P. length ( );             		// 模式的长度
    int tLen = T.length( );					// 目标的长度

    if (tLen < pLen) 				    		// 如果目标比模式短，匹配无法成功
        return (-1);          			
    while ( i < pLen  &&  j < tLen)  {  		// 反复比较对应字符来开始匹配
       		if ( i == -1  ||  T[j] == P[i]) 
    			i++,  j++;
    		else i = N[i];
    }
    if ( i >= pLen)
    		return (j - pLen + 1);
    else return (-1);
}


int *findNext(string P) {
	int i = 0; 
    int k = -1; 
    int m = P.length();    					// m为字符串P的长度
  	assert(m > 0);     					// 若m＝0，退出
  	int *next = new int[m];  				// 动态存储区开辟整数数组
    assert(next != 0);   					// 若开辟存储区域失败，退出
  	next[0] = -1;
    while (i < m) { 						// 计算i=1..m-1的next值
		while (k >= 0 && P[i] != P[k])  		// 求最大首尾子串
			k = next[k];		
        i++;
		k++;
		if (i == m) break;
		if (P[i] == P[k] ) 				
			next[i] = next[k];				//  P[i]和P[k]相等，优化
		else next[i] = k;					// 不需要优化，就是位置i的首尾子串长度
    }
    return next;
}

