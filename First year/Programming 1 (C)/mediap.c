#include<stdio.h>

int main(){
    float n,c=0,media=0, somma=0;
    while(1){
        printf("Inserisci un numero: ");
        scanf("%f", &n);
        if(n<0){
            break;
        }else{
            c++;
            somma=somma+n;
            media=somma/c;
            printf("La media dei numeri inseriti fino ad ora e: %f\n", media);
        }
    }
    return 0;
}