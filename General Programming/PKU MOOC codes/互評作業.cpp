//第五次互評作業之三 
/*
#include <iostream>
#include <stdlib.h>
#include <cstring>
using namespace std;

class MyString:public string{
    public:
    string words;
    MyString(){}
    MyString(const string & s):string(s){words = s;}
    MyString(const char * c):string(c){words = c;} 
    ~MyString(){} 
    MyString & MyString::operator=(const char * s); 
    char & MyString::operator[](int i);
    MyString operator ()(const int &a,const int &b){return substr(a,b);}
    MyString & MyString::operator += (const char * s); 
}; 

ostream & operator<<(ostream & os, const MyString & st)
{
    os << st.words;
    return os; 
}

MyString & MyString::operator=(const char * s)
{
    int len = std::strlen(s);
    for(int i=0;i<len;i++){words[i]=s[i];}
    return *this;
}

int CompareString( const void * e1, const void * e2) {
    MyString * s1 = (MyString * ) e1;
    MyString * s2 = (MyString * ) e2;
    if( * s1 < *s2 )     return -1;
    else if( *s1 == *s2) return 0;
    else if( *s1 > *s2 ) return 1;
}

char & MyString::operator[](int i)
{
    return words[i];
}

MyString & MyString::operator += (const char * s)
{
	words = words + s; 
	return *this;
}

int main(){
    MyString s1("abcd-"),s2,  
    s3("efgh-"),s4(s1);
    cout << "1. " << s1 << s2 << s3<< s4<< endl;
    s4 = s3;    s3 = s1 + s3; 
    cout << "2. " << s1 << endl;
    cout << "3. " << s2 << endl;
    cout << "4. " << s3 << endl;
    cout << "5. " << s4 << endl;
    cout << "6. " << s1[2] << endl;
    s2 = s1;    s1 = "ijkl-"; 
    s1[2] = 'A' ;
    cout << "7. " << s2 << endl;
    cout << "8. " << s1 << endl;
    s1 += "mnop"; 
    cout << "9. " << s1 << endl;
    s4 = "qrst-" + s2;   
    cout << "10. " << s4 << endl;
    s1 = s2 + s4 + " uvw " + "xyz";  
    cout << "11. " << s1 << endl;
    
    MyString SArray[4] = {"big","me","about","take"};
    qsort(SArray,4,sizeof(MyString), CompareString); 
    for( int i = 0;i < 4;++i )
        cout << SArray[i] << endl;
        
//輸出s1從下標0開始長度為10的子串
    cout << s1(0,4) << endl; 
//輸出s1從下標5開始長度為10的子串
    cout << s1(5,10) << endl; 
    
    system("pause");
    return 0;
}
*/

//第四次互評作業之三 

/*#include <iostream>
#include <stdlib.h>
#include <cstring>
using namespace std;

class Array2
{
private:
int row, col;
int **pData;
public:
Array2():row(0), col(0), pData(0) {}
Array2(int r, int c);
~Array2();
Array2(const Array2 & a);
int * operator [] (int index);
int operator () (int i, int j);
Array2 & Array2::operator = ( const Array2 & right);
};
Array2::Array2(int r, int c)
{
int i, j;

pData = new int *[r];

for(i = 0; i < r; i++)
pData[i] = new int [c];

for(i = 0; i < r; i++)
for(j = 0; j < c; j++)
*(*(pData + i) + j) = 0;

row = r;
col = c;
}

Array2::~Array2()
{
if(pData) { 
for(int i = 0; i < this->row; i++)
           delete [] pData[i];  // pData[i] = new int[col], so it must be deleted  as an array.
}
delete [] pData; 
pData = NULL;
}

Array2::Array2(const Array2 & a)
{
this->~Array2();

int i, j;

for(i = 0; i < a.row; i++)
pData[i] = new int [a.col];
pData[i]=(int *)malloc(sizeof(pData[i]));

for(i = 0; i < a.row; i++)
for(j = 0; j < a.col; j++)
*(*(pData + i) + j) = *(*(a.pData + i) + j);

row = a.row;
col = a.col;
}

int * Array2::operator[] (int index)
{
return pData[index];
}

int Array2::operator() (int i, int j)
{
return *(*(pData + i) + j);
}

Array2 & Array2::operator = ( const Array2 & right)
{
 if(this == &right)
  return *this;
 else{
  if(row !=right.row || col != right.col)
  {
   this ->~Array2();
   row = right.row; col = right.col;

   pData = new int* [row];
 for(int i=0; i<row; i++)
  pData[i] = new int[col];
   
  }
  
  for(int i=0; i<row; i++)   
   for(int j=0; j<col; j++)
    pData[i][j] = right.pData[i][j];
 }

// return *this；

}

int main() {
	Array2 a(3,4);
	int i,j;
	for(  i = 0;i < 3; ++i )
		for(  j = 0; j < 4; j ++ )
			a[i][j] = i * 4 + j;
	for(  i = 0;i < 3; ++i ) {
		for(  j = 0; j < 4; j ++ ) {
			cout << a(i,j) << ",";
		}
		cout << endl;
	}
	cout << "next" << endl;
	Array2 b; 	b = a;
	for(  i = 0;i < 3; ++i ) {
		for(  j = 0; j < 4; j ++ ) {
			cout << b[i][j] << ",";
		}
		cout << endl;
	}
    system("pause");
	return 0;
}*/

