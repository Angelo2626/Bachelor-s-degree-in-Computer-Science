#include<stdio.h>
#include<stdlib.h>
#include<time.h>

void stampa(int n, int m[][n]);

void occorrenze(int n, int m[n][n], int val);

int main(){

    srand(time(NULL));

    int n;

    printf("Inserisci un numero: ");
    scanf("%d", &n);

    int m[n][n];
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            m[i][j]=(rand()%(99+0-1))+0;
        }
    }

    stampa(n,m);
    
    int val;
    printf("Inserisci un valore intero: ");
    scanf("%d", &val);
    
    occorrenze(n,m,val);
    return 0;
}

void stampa(int n, int m[][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            printf("%-2d ", m[i][j]);
        }
        printf("\n");
    }
}

void occorrenze(int n, int m[n][n], int val){
    int flag=0;

    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            if(val==m[i][j]){
                flag++;
                m[i][j]=0;
            }
        }
        printf("Riga %d: %d occorrenza/e\n", i, flag);
        flag=0;
    }

    stampa(n, m);
}