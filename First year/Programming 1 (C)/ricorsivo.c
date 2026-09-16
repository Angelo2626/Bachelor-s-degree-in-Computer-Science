#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int sommaricorsiva(int dim, int a[], int somma);

int main(){
    srand(time(NULL));
    int a[10];
    for(int i=0; i<10; i++){
        a[i]=rand()%(9+1-1)+1;
    }
    for(int i=0; i<10; i++){
        printf("%d ", a[i]);
    }
    printf("\n");
    int somma=0,dim=0;
    somma=sommaricorsiva(dim,a,somma);
    printf("\n%d\n", somma);
    return 0;
}

int sommaricorsiva(int dim, int a[], int somma){
    if(dim<=9){
        if(a[dim]%2!=0){
            somma=somma+a[dim];
        }
        return sommaricorsiva(dim+1,a,somma);
    }else{
        return somma;
    }
}