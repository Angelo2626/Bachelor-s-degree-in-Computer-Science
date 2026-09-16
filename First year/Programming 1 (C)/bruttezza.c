#include<stdio.h>
#include<stdlib.h>
#include<time.h>

#define N 5
#define M 6

int main(){

    srand(time(NULL));
    int a[N][M];

    for(int i=0; i<N; i++){
        for(int j=0; j<M; j++){
            a[i][j]=(rand()%(5-0+1))+0;
        }
    }

    for(int i=0; i<N; i++){
        for(int j=0; j<M; j++){
            printf("%d ", a[i][j]);
        }
        printf("\n");
    }

    int cattivo=0, sommac=0, maxc=0;
    for(int i=0; i<N; i++){
        for(int j=0; j<M; j++){
            sommac=sommac+a[i][j];
        }
        if(sommac>maxc){
            maxc=sommac;
            cattivo=i+1;
        }
        sommac=0;
    }

    int sommab=0, maxb=0, brutto=0;

    for(int i=0; i<M; i++){
        for(int j=0; j<N; j++){
            sommab=sommab+a[j][i];
        }
        if(sommab>maxb){
            maxb=sommab;
            brutto=i+1;
        }
        sommab=0;
    }
    printf("Il giudice piu cattivo e il numero %d\n", cattivo);
    printf("Il concorrente piu brutto e %d\n", brutto);
    return 0;
}