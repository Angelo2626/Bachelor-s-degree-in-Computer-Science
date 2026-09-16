#include<stdio.h>
#include<stdlib.h>
#include<ctype.h>

void verifica(char parola[]);
int main(){

    FILE *f=fopen("input3.txt", "r");

    if(f==NULL){
        printf("Errore nell'apertura del file.\n");
        exit(1);
    }else{
        printf("File aperto correttamente.\n\n");
    }

    char parola[15];

    while(fscanf(f, "%s", &parola)!=EOF){
        verifica(parola);
    }

    fclose(f);
    printf("\nFile chiuso.\n");
    return 0;
}

void verifica(char parola[]){
    int c=0, cv=0;
    for(int i=0; parola[i]!='\0'; i++){
        c++;
        if(parola[i]=='a' || parola[i]=='e' || parola[i]=='i' || parola[i]=='o' || parola[i]=='u'){
            cv++;
        }
    }
    if(cv>3 && c>3){
        for(int i=0; parola[i]!='\0'; i++){
            if(isalpha(parola[i])){
                printf("%c", parola[i]);
            }
        }
        printf("\n");
    }
}