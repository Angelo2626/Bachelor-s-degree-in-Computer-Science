#include<stdio.h>
#include<stdlib.h>
int main(){

    FILE *f=fopen("valori.txt","w");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    int n;
    while(1){
        printf("Inserisci un numero nel file: ");
        scanf("%d", &n);
        if(n==0){
            break;
        }
        fprintf(f,"%d ",n);
    }

    fclose(f);
    return 0;
}