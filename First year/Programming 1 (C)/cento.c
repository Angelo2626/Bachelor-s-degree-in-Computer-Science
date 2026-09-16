#include<stdio.h>

#define n 10

void riempimento(int m[n][n]);

void stampa(int m[n][n]);

void minore(int val, int m[n][n]);

int main(){
    
    int m[n][n];
    
    riempimento(m);

    int val;

    while(1){
        printf("Inserisci un valore compreso tra 0 e 10: ");
        scanf("%d", &val);
        if(val<0 || val>9){
            printf("Errore!\n");
        }else{
            break;
        }
    }

    minore(val, m);
    
    return 0;
}

void riempimento(int m[n][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            m[i][j]=(i*8)+(j*8);
        }
    }

    stampa(m);
}

void stampa(int m[n][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            printf("%-3d ", m[i][j]);
        }
        printf("\n");
    }
}

void minore(int val, int m[n][n]){
    int max=0, min;
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            if(val>i){
                if(m[i][j]>max){
                    max=m[i][j];
                }
            }
        }
        printf("Il valore massimo della riga di indice %d e %d\n", i, max);
        if(val>i){
            if(i==0){
                min=max;
            }else if(max<min){
                min=max;
            }
        }
        max=0;
    }
    printf("%d\n", min);
}