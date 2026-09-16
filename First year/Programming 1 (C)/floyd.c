#include<stdio.h>
int main(){

    int n,somma=1;
    scanf("%d", &n);
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            if(j<=i){
                printf("%-3d", somma++);
            }
        }
        printf("\n");
    }
    return 0;
}