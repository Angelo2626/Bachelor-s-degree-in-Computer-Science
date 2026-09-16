#include<stdio.h>
#include<stdlib.h>
int main(){
    int c=0, n;

    FILE *f=fopen("file.txt", "w");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n\n");
    }else{
        printf("Il file è stato aperto con successo.\n\n");
    }

    while(1){
        printf("Inserisci un numero.");
        scanf("%d", &n);
        if(n!=0){
            fprintf(f, "%d ", n);
            c++;
        }else{
            break;
        }
    }
    fprintf(f, "%d", c);
    fclose(f);
    printf("Il file e stato chiuso con successo.\n");
    return 0;
}