#include<stdio.h>
int main(){
    int n;
    printf("Inserisci un numero: ");
    scanf("%d", &n);

    int a[5][5];
    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            if(i>=j){
                a[i][j]=n+i;
            }else{
                a[i][j]=n+j;
            }
        }
    }

    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            printf("%2d", a[i][j]);
        }
        printf("\n");
    }
    return 0;
}