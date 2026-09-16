#include<stdio.h>
#include<stdlib.h>
#include<string.h>
int main(){

    FILE *f=fopen("testo.txt","r");

    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    char parola[15], pprecedente[15]={};

    while(fscanf(f,"%s", parola)==1){
        if(strcmp(pprecedente,parola)==0 && (strlen(parola))>4){
            printf("%s ", parola);
        }
        strcpy(pprecedente, parola);
    }

    printf("\n");
    fclose(f);

    return 0;
}