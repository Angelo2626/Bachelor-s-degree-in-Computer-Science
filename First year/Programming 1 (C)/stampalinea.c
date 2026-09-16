#include<stdio.h>
#include<stdlib.h>
int main(){

    char *line;
    size_t len=0;

    FILE *f=fopen("input1.txt", "r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n\n");
    }else{
        printf("File aperto con successo.\n\n");
    }

    while(getline(&line, &len, f)!=-1){ //ciclo che controlla riga per riga fino alla fine del file
        printf("%s\n", line);
    }

    fclose(f);
    printf("File chiuso.\n");
    return 0;
}