#include<stdio.h>
#include<time.h>
#include<stdlib.h>

#define N 6

void somma(int a[N][N], int x, int y);
int main(){

    srand(time(NULL));
    int a[N][N];

    for(int i=0; i<N; i++){
        for(int j=0; j<N; j++){
            a[i][j]=(rand()%(9+1-1))+1;
        }
    }

    for(int i=0; i<N; i++){
        for(int j=0; j<N; j++){
            printf("%d ", a[i][j]);
        }
        printf("\n");
    }

    int x,y;

    do{
        printf("Inserisci la coordinata x: ");
        scanf("%d", &x);
    }while(x<0 || x>N);

    do{
        printf("Inserisci la coordinata y: ");
        scanf("%d", &y);
    }while(y<0 || y>N);    

    somma(a,x,y);
    return 0;
}
void somma(int a[N][N], int x, int y){
    int somma=0;
    for(int i=0; i<x; i++){
        for(int j=0; j<y; j++){
            somma=somma+a[i][j];
        }
    }
    printf("La somma della sottomatrice e: %d\n", somma);
}