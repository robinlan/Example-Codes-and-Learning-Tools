// 图的相邻矩阵表示方法,还要用到最小值堆
#include <iostream.h>
#include <queue>
#define UNVISITED 0
#define VISITED 1
#define INFINITE 9999    //设置最大值
#define N 5 // 定义图的顶点数

#include "Graph_matrix.h"
#include "MinHeap.h"


//[代码7.8] Dijkstra算法
class Dist  {      //定义Dist类，下面的Dijkstra算法和Floyd算法要用到
 public:
	 int index;      //顶点的索引值，仅Dijkstra算法会用到
	 int length;     //顶点之间的距离
	 int pre;       //路径最后经过的顶点
	 Dist() {};
	 ~Dist() {};

	 bool operator < (const Dist & arg)  {
		 return (length < arg.length);
	 }
	 bool operator == (const Dist &arg)  {
		 return (length==arg.length);
	 }
	 bool operator > (const Dist &arg)  {
		 return (length>arg.length);
	 }
	 bool operator <=(const Dist &arg)  {
		 return (length<=arg.length);
	 }
	 bool operator >= (const Dist &arg)  {
		 return (length>=arg.length);
	 }
};

//Dijkstra算法，其中参数G是图，参数s是源顶点，D是保存最短距离及其路径的数组
void Dijkstra(Graph& G, int s, Dist* &D)  {
	D = new Dist[G. VerticesNum()];          	// D数组
	for (int i = 0; i < G.VerticesNum(); i++) {   	// 初始化Mark数组、D数组
		G.Mark[i] = UNVISITED;
        D[i].index = i;
        D[i].length = INFINITE;
        D[i].pre = s;
    }
    D[s].length = 0; 
    MinHeap<Dist> H(G. EdgesNum());       	// 最小值堆（minheap）
    H.Insert(D[s]);
	for (i = 0; i < G.VerticesNum(); i++) {
		bool FOUND = false;
        Dist d;
        while (!H.isEmpty())  {
			d = H.RemoveMin(); 
			if(G.Mark[d.index]==UNVISITED) {                //打印出路径信息
				cout<< "vertex index: " <<d.index<<"   ";
				cout<< "vertex pre  : " <<d.pre  <<"   ";
				cout<< "V0 --> V" << d.index <<"  length    : " <<d.length<<endl;
			}
			
			if (G.Mark[d.index] == UNVISITED) { //找到距离s最近的顶点
				FOUND = true;
				break;
			}
        }
		if (!FOUND)
            break;
        int v = d.index;
		G.Mark[v] = VISITED;           		// 把该点加入已访问组
		// 因为v的加入，需要刷新v邻接点的D值
		for (Edge e = G.FirstEdge(v); G.IsEdge(e);e = G.NextEdge(e))
			if (D[G.ToVertex(e)].length > (D[v].length+G.Weight(e))) {
				D[G.ToVertex(e)].length = D[v].length+G.Weight(e);
				D[G.ToVertex(e)].pre = v;
				H.Insert(D[G.ToVertex(e)]);
			}
	}
}



int A[N][N] =  {          //图7.20  单源最短路径的示例
//  v0  v1  v2  v3  v4  
	 0, 10,  0, 30, 100,
     0,  0, 50,  0,  0, 
     0,  0,  0,  0, 10, 
     0, 10, 20,  0, 60, 
     0,  0,  0,  0,  0, 
};

void main()
{
 Graphm aGraphm(N); // 建立图
 aGraphm.IniGraphm(&aGraphm, A); // 初始化图
 Dist *D;
 Dijkstra(aGraphm, 0, D);
}
