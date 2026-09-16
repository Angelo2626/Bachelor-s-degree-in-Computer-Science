#include<stdio.h>
#include<stdlib.h>
int main(){
    FILE *f=fopen("canzone.txt","r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }
    char parola;
    while((parola=getc(f))!=EOF){
        putchar(parola);
    }

    fseek(f, 0, SEEK_SET);
    printf("\n\n");
    
    char *line=NULL;
    size_t len=0;
    while(getline(&line, &len, f)!=-1){
        printf("%s", line);
    }

    fclose(f);
    return 0;
}