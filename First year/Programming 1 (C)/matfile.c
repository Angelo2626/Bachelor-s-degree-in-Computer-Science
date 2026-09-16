#include<stdio.h>
#include<stdlib.h>

void creamatrice(FILE *f, int n, int a[n][n]);
void stampamatrice(int n, int a[n][n]);

int main(){

    FILE *f=fopen("input.txt","a+");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    int n;
    printf("Inserisci un numero: ");
    scanf("%d", &n);
    int a[n][n];
    creamatrice(f,n,a);
    return 0;
}

void creamatrice(FILE *f, int n, int a[n][n]){
    int num, i=0, j=0;
    while(fscanf(f, "%d", &num)==1){
        if(i<n){
            if(j<n){
                a[i][j]=num;
                j++;
            }
            if(j==n){
                j=0;
                i++;
            }
        }
    }
    while(i<n){
        if(j<n){
            a[i][j]=0;
            j++;
        }
        if(j==n){
            j=0;
            i++;
        }
    }
    stampamatrice(n,a);
}

void stampamatrice(int n, int a[n][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            printf("%-3d ", a[i][j]);
        }
        printf("\n");
    }
}