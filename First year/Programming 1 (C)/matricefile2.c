#include<stdio.h>
#include<stdlib.h>

int verifica(FILE *f, int n);
void riempi(int n, int m[n][n], FILE *f);
void stampa(int n, int m[n][n]);
void riempi0(int n, int m[n][n], FILE *f);

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
        rewind(f);
        riempi0(n,m,f);
    }else{
        rewind(f);
        riempi0(n,m,f);
    }

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

void stampa(int n, int m[n][n]){
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            printf("%-3d ", m[i][j]);
        }
        printf("\n");
    }
}

void riempi0(int n, int m[n][n], FILE *f) {
    if (f == NULL) {
        fprintf(stderr, "Errore: Puntatore al file è NULL.\n");
        return;
    }

    int num;
    int i = 0, j = 0;

    // Leggi gli interi dal file e riempi la matrice
    while (fscanf(f, "%d", &num) != EOF) {
        if (i < n) {
            if (j < n) {
                m[i][j] = num;
                j++;
            }
            if (j == n) {
                j = 0;
                i++;
            }
        }
        if (i >= n) {
            // Abbiamo riempito tutta la matrice, interrompiamo la lettura
            break;
        }
    }

    // Se ci sono righe non completamente riempite, riempi con zeri
    if (i < n) {
        while (j < n) {
            m[i][j] = 0;
            j++;
        }
        i++;
    }

    // Riempi le righe rimanenti con zeri
    for (; i < n; i++) {
        for (j = 0; j < n; j++) {
            m[i][j] = 0;
        }
    }
}
