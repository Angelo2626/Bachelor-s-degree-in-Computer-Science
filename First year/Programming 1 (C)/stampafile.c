#include<stdio.h>
#include<stdlib.h>
int main(){
    char c;
    FILE *f=fopen("input1.txt", "r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n\n");
    }else{
        printf("File aperto correttamente.\n\n");
    }
    while((c=getc(f))!=EOF){
        printf("%c\n", c);
    }
    fclose(f);
    printf("File chiuso.\n");
    return 0;
}