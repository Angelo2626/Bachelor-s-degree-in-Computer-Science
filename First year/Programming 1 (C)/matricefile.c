#include<stdio.h>
#include<stdlib.h>

int verifica(FILE *f, int n);
void riempi(int n, int m[n][n], FILE *f);
void stampa(int n, int m[n][n]);

int main(){

    FILE *f = fopen("input.txt", "a+");

    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
    }else{
        printf("Il file e stato aperto con successo.\n");
    }

    int n,r;
    printf("Inserisci la dimensione della matrice quadrata.");
    scanf("%d", &n);

    int m[n][n];

    r=verifica(f,n);
    if(r==0){
        printf("Il file non contiene abbastanza numeri per riempire la matrice.\n");
        fclose(f);
        exit(0);
    }

    rewind(f);

    riempi(n,m,f);

    stampa(n,m);

    rewind(f);
    
    fprintf(f, "%d", 0);

    fclose(f);
    printf("File chiuso con successo.\n");

    return 0;
}

int verifica(FILE *f, int n){
    int ver=n*n, c=0, num;
    while(fscanf(f, "%d", &num)!=EOF){
        c++;
    }
    if(c>=ver){
        return 1;
    }else{
        return 0;
    }
}

void riempi(int n, int m[n][n], FILE *f){
    int num;
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            fscanf(f, "%d", &num);
            m[i][j]=num;
        }
    }
    
}

void stampa(int n, int m[n][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            printf("%-3d ", m[i][j]);
        }
        printf("\n");
    }
}