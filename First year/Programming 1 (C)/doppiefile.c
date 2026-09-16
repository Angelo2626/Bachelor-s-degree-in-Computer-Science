#include<stdio.h>
#include<stdlib.h>
#include<string.h>

void verifica(char parola[], char pre[]);

int main(){
    
    FILE *f=fopen("prova.txt", "r");

    if(f==NULL){
        printf("ERRORE nell'apertura del file.\n");
        exit(1);
    }else{
        printf("file aperto correttamente.\n");
    }

    char parola[15], pre[15]="";
    while(fscanf(f, "%s", &parola)!=EOF){
        verifica(parola,pre);
        strcpy(pre,parola);
    }
    printf("\n");
    fclose(f);
    printf("File chiuso.\n");
    return 0;
}

void verifica(char parola[], char pre[]){
    int c=0;
    for(int i=0; parola[i]!='\0'; i++){
        c++;
    }
    if(c>4 && (strcmp(parola,pre)==0)){
        printf("%s ", parola);
    }
}