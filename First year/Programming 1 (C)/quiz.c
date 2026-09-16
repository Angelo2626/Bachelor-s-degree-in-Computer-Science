#include<stdio.h>

int main(){

    int n, somma=0;
    do{
        scanf("%d", &n);
        for(int i=1; i<n; i++){
            if(n%i==0){
                somma++;
            }
        }
    }while(n>0);

    printf("%d", somma);
    return 0;
}