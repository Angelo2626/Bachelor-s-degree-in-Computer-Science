#include<stdio.h>
#include<stdlib.h>
int main(){
    FILE *f=fopen("input1.txt","r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    char *r;
    size_t len=0;
    while(getline(&r,&len,f)!=-1){
        printf("%s", r);
    }

    fclose(f);
    return 0;
}