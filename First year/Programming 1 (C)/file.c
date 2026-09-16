#include<stdio.h>
#include<stdlib.h>
int main(){

    FILE *fid=fopen("file.txt", "w");
    if(fid==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }else{
        printf("File aperto con successo.\n");
    }

    fprintf(fid, "%d", 42);
    fclose(fid);
    printf("File chiuso.\n");

    int code;
    FILE *f=fopen("file.txt", "r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
    }else{
        printf("File aperto con successo.\n");
    }
    fscanf(f, "%d", &code);
    printf("%d\n", code);
    fclose(f);
    printf("File chiuso.\n");
    return 0;
}