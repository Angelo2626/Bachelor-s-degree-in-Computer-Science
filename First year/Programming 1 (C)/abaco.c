#include<stdio.h>

int inverti(int n);
void scomponi(int n);
void abaco(int copia, int c);

int main(){
    int n;
    printf("Inserisci un numero: ");
    scanf("%d", &n);
    n=inverti(n);
    scomponi(n);
    return 0;
}

void scomponi(int n){
    int c;
    if(n>0){
        c=n%10;
        abaco(c,c);
        scomponi(n/10);
    }
}

void abaco(int copia, int c){
    if(copia>0){
        printf("o");
        abaco(copia-1, c);
    }else{
        printf("\t%d\n", c);
    }
}

int inverti(int n){
    int copia=0, c;
    while(n>0){
        c=n%10;
        copia=(copia*10)+c;
        n=n/10;
    }
    return copia;
}

