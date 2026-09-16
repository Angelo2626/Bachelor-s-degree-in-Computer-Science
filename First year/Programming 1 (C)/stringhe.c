#include<stdio.h>
#include<string.h>

void stampa(char s1[]);

void vocali(char s1[]);

int main(){
    char s1[20];
    int c=0;
    char car;

    while(1){
        printf("Inserisci un carattere: ");
        scanf(" %c", &car);
        if(car!='#' && c<19){
            s1[c]=car;
            c++;
        }else{
            break;
        }
    }

    s1[c]='\0';

    stampa(s1);
    vocali(s1);
    return 0;
}

void stampa(char s1[]){
    printf("%s", s1);
}

void vocali(char s1[]){
    char a='a', e='e', i='i', o='o', u='u';
    char s2[40];
    int c1=0, c2=0;
    while(s1[c1]!='\0'){
        if(s1[c1]==a || s1[c1]==e || s1[c1]==i || s1[c1]==o || s1[c1]==u){
            s2[c2]=s1[c1];
            c2++;
            s2[c2]=s1[c1];
            c2++;
            c1++;
        }else{
            s2[c2]=s1[c1];
            c1++;
            c2++;
        }
    }
    s2[c2]='\0';
    printf("\n");
    stampa(s2);
}