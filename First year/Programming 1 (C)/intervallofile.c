#include<stdio.h>
#include<stdlib.h>
int main(){

    int a,b;

    printf("Inserisci un numero (scagli tra:1 54 63 1 3 54 13 54 24 54 24 12 45 24 54 5 6 87 4 2 3 4 5 6 7 2 ): ");
    scanf("%d", &a);
    
    printf("Inserisci un numero (scagli tra:1 54 63 1 3 54 13 54 24 54 24 12 45 24 54 5 6 87 4 2 3 4 5 6 7 2 ): ");
    scanf("%d", &b);

    FILE *f=fopen("input.txt","r");

    if(f==NULL){
        printf("Errore nell'apertura del file\n");
        exit(1);
    }

    char c;
    int count=0,flag=0;
    while((c=fgetc(f))!=EOF){
        if(c==a){
            flag=1;
        }
        if(flag==1){
            if(c!=' '){
                count++;
            }
        }
        if(c==b){
            flag=0;
        }
    }

    printf("Ci sono %d valori nell'intervallo [%d, %d].\n", count, a, b);
    fclose(f);
    return 0;
}