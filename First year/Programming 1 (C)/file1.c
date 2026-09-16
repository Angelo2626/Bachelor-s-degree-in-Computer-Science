#include<stdio.h>
#include<stdlib.h>

int main(){
    FILE *f=fopen("input3.txt","r");
    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }

    int num;

    while(fscanf(f, "%d", &num)==1){
        printf("%d\n", num);
    }

    fclose(f);

    return 0;
}
