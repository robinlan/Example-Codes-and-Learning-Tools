#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>
using namespace std;

int m_time = 0;

class warrior{
public:
	string name;
	int num;
	int strength;
	int weapon;
	int mind;
	int royalty;
};

class redhq{
public:
	int M;   
	int assign;
	warrior war[5];
	redhq(int N){
		assign = 0;
		war[0].name="iceman";
		war[1].name="lion";
		war[2].name="wolf";
		war[3].name="ninja";
		war[4].name="dragon";
		for(int i = 0; i < 5; i++)
			war[i].num = 0;
		M = N;
	}
	bool enough(){
		for(int i = 0; i<5; i++)
			if(M>=war[i].strength)
				return true;
		return false;
	}
	int born(int a_time,int red_pos){
		int pos;
		for(int i = 0 ; i< 5; i++){
			pos = (red_pos+i)%5;
			if(M >= war[pos].strength){
				assign = pos;
				break;
			}
		}
		war[assign].num++;
		M -= war[assign].strength;
		printf("%03d red %s %d born with strength %d,%d %s in red headquarter\n",a_time,war[assign].name.c_str(),a_time+1,
			war[assign].strength,war[assign].num,war[assign].name.c_str());

		return assign+1;
	}
};
class bluehq{
public:
	int M;
	int assign;
	warrior war[5];
	bluehq(int N){
		assign = 0;
		war[0].name="lion";
		war[1].name="dragon";
		war[2].name="ninja";
		war[3].name="iceman";
		war[4].name="wolf";
		for(int i = 0; i < 5; i++)
			war[i].num = 0;
		M = N;
	}
	bool enough(){
		for(int i = 0; i<5; i++)
			if(M>=war[i].strength)
				return true;
		return false;
	}
	int born(int a_time,int blue_pos){
		int pos;
		for(int i = 0 ; i< 5; i++){
			pos = (blue_pos+i)%5;
			if(M >= war[pos].strength){
				assign = pos;
				break;
			}
		}
		war[assign].num++;
		M -= war[assign].strength;
		printf("%03d blue %s %d born with strength %d,%d %s in blue headquarter\n",a_time,war[assign].name.c_str(),a_time+1,
			war[assign].strength,war[assign].num,war[assign].name.c_str());

		return assign+1;
	}
};

int main(int argc, char* argv[])
{
	int n,m;
	int init[5]={0,0,0,0,0};
	
	cin >> n;
	for(int j = 1; j<=n; j++)
	{
		int pos_red = 0;
    	int pos_blue = 0;
        m_time = 0;
		cin >> m;
		for(int i=0; i<5; i++)
			cin >> init[i];

		cout << "Case:" << j << endl;
		redhq red(m);
		red.war[0].strength = init[2];
		red.war[1].strength = init[3];
		red.war[2].strength = init[4];
		red.war[3].strength = init[1];
		red.war[4].strength = init[0];

		bluehq blue(m);
		blue.war[0].strength = init[3];
		blue.war[1].strength = init[0];
		blue.war[2].strength = init[1];
		blue.war[3].strength = init[2];
		blue.war[4].strength = init[4];

		int flag = 1;
		int flag_2 = 1;
		while(red.enough() || blue.enough())
		{
			if(red.enough())
			{
				pos_red = red.born(m_time,pos_red);
			}
			else if(flag)
			{	
				printf("%03d red headquarter stops making warriors\n",m_time);
				flag = 0;
			}

			if(blue.enough())
			{
				pos_blue = blue.born(m_time,pos_blue);
			}
			else if(flag_2)
			{	
				printf("%03d blue headquarter stops making warriors\n",m_time);
				flag_2 = 0;
			}
			m_time++;
		}
		if(flag)
			printf("%03d red headquarter stops making warriors\n",m_time);

		if(flag_2)
			printf("%03d blue headquarter stops making warriors\n",m_time);

	}
	system("pause");
	return 0;
}
