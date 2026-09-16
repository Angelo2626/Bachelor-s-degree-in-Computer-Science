#include<stdio.h>
#include<stdlib.h>

void pitagora(FILE *f, int n);

int main(){
    FILE *f=fopen("tabellina.txt","w");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    int n;
    printf("Inserisci un numero: ");
    scanf("%d", &n);
    pitagora(f,n);
    fclose(f);
    return 0;
}

void pitagora(FILE *f, int n){
    for(int i=1; i<=n; i++){
        for(int j=1; j<=10; j++){
            fprintf(f,"%3d",i*j);
        }
        fprintf(f,"\n");
    }
}