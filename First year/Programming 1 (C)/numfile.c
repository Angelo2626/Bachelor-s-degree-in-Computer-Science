#include<stdio.h>
#include<stdlib.h>
#include<ctype.h>

int main(){
    FILE *f=fopen("input3.txt", "r");

    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }else{
        printf("Il file e stato aperto correttamente.\n");
    }

    char c,cp='\0';

    while((c=getc(f))!=EOF){
        if(isdigit(c)){
            if((isdigit(cp)) || cp==' '){
                putchar(c);
            }
        }
        if((isdigit(cp)) && c==' '){
            printf("\n");
        }
        cp=c;
    }

    fclose(f);
    printf("File chiuso");
    return 0;
}