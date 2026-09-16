#include<stdio.h>

typedef struct str_frazione{
    int numeratore;
    int denominatore;
}frazione;

int main(){

    frazione num1,num2;

    printf("Inserisci il numeratore del primo numero: ");
    scanf("%d", &num1.numeratore);
    printf("Inserisci il denominatore del primo numero: ");
    scanf("%d", &num1.denominatore);
    
    printf("Inserisci il numeratore del secondo numero: ");
    scanf("%d", &num2.numeratore);
    printf("Inserisci il denominatore del secondo numero: ");
    scanf("%d", &num2.denominatore);

    int somma=0, semplificazione=0, c=1, m1, m2;

    if(num1.denominatore>num2.denominatore){
        c=num1.denominatore;
        while(1){
            if(c%num2.denominatore!=0){
                c=c+num1.denominatore;
            }else{
                break;
            }
        }
        m1=c/num1.denominatore;
        m2=c/num2.denominatore;
    }else if(num2.denominatore>num1.denominatore){
        c=num2.denominatore;
        while(1){
            if(c%num1.denominatore!=0){
                c=c+num2.denominatore;
            }else{
                break;
            }
        }
        m1=c/num1.denominatore;
        m2=c/num2.denominatore;
    }else if(num1.denominatore==num2.denominatore){
        m1=1;
        m2=1;
        c=num1.denominatore;
    }

    frazione risultato;
    risultato.denominatore=c;
    somma=(num1.numeratore*m1)+(num2.numeratore*m2);
    risultato.numeratore=somma;
    printf("La somma tra le due frazioni e uguale a %d/%d\n", risultato.numeratore, risultato.denominatore);
    semplificazione=risultato.numeratore/risultato.denominatore;
    printf("La semplificazione della frazione e %d\n", semplificazione);
    return 0;
}