//***************************//
#include <iostream>
#include "BinaryTree.h"


void main() {
	//建一棵树(如图5.5所示)
	BinaryTree<char> a, b, c, d, e, f, g, h, i,nulltree;
	d.CreateTree('D', nulltree, nulltree);
	g.CreateTree('G', nulltree, nulltree);
	h.CreateTree('H', nulltree, nulltree);
	i.CreateTree('I', nulltree, nulltree);
	f.CreateTree('F', h, i);
	e.CreateTree('E', g, nulltree);
	b.CreateTree('B', d, e);
	c.CreateTree('C', nulltree, f);
	a.CreateTree('A', b, c);
	
	//前序周游二叉树
	cout << "Preorder sequence is: "<<endl;
	a.PreOrder(a.Root());				//递归
	cout << endl;
	cout << "Preorder sequence Without Recursion is: " <<endl;
	a.PreOrderWithoutRecursion(a.Root());//非递归
	cout << endl;

	//中序周游二叉树
	cout << "Inorder sequence is: "<<endl;
	a.InOrder(a.Root());			//递归
	cout << endl;
	cout << "Inorder sequence Without Recursion is: " <<endl;
	a.InOrderWithoutRecursion(a.Root());//非递归
	cout << endl;

	//后序周游二叉树
	cout << "Postorder sequence is: "<<endl;
	a.PostOrder(a.Root());			//递归
	cout << endl;
	cout << "Postorder sequence Without Recursion is: " <<endl;
	a.PostOrderWithoutRecursion(a.Root());//非递归	
	cout << endl;	

	//root
	cout << "Root is: " << a.Root()->value() <<endl;

/*	//delete tree
	a.DeleteBinaryTree(a.Root());
	cout<<"Tree is deleted."<<endl;        //没有问题，在析构函数中调用
*/

}