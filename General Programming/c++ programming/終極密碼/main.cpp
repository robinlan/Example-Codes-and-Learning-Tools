// ½Ð¨Ï¥Î DEV-C++ , VC++

 #include <stdio.h>
 #include <stdlib.h>
 #include <time.h>

 int main() {
    srand(time(NULL));
    int n, count = 0;
    int min = 1, max = 99;
    int ran = rand() % 99 + 1;
    char player[2] = { 'A', 'B' };
    char c[2] = "y";
    do { 
       printf("The code is between %d and %d.\n", min, max );
       printf("Player %c input your guess: ", player[count % 2] );
       scanf("%d", &n);
       if( n >= min && n <= max ) {
          if( n > ran ) 
             max = n - 1;
          else if( n < ran ) 
             min = n + 1;
          else {           
             printf("Bomb!! You are dead!\n\nTry Again (Y/N)? " );
             count = -1;
             min = 1;
             max = 99;
             scanf("%s", &c);
          }
          count++; 
       } else
          printf("Range error.\n" );
    } while( c[0] == 'y' || c[0] == 'Y' );
    system("PAUSE");
    return EXIT_SUCCESS;
}
