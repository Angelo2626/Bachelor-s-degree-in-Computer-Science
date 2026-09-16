#include<stdio.h>
#include<stdlib.h>

int main(){
    FILE *f=fopen("input1.txt","r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    char c;
    while(fscanf(f, "%c", &c)==1){
        putchar(c);
    }
    fclose(f);
    return 0;
}