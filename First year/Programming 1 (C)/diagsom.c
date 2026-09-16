#include<stdio.h>
int main(){
    
    int a[4][4];
    for(int i=0; i<4; i++){
        for(int j=0; j<4; j++){
            scanf("%d ", &a[i][j]);
        }
    }

    int sommatoria=0;

    for(int i=0; i<4; i++){
        for(int j=0; j<4; j++){
            if(i!=j && i<j){
                for(int x=0; x<=a[i][j]; x++){
                    sommatoria=sommatoria+x;
                }
            }
        }
    }

    printf("La sommatoria e %d \n", sommatoria);
    return 0;
}