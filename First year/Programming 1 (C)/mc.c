#include<stdio.h>
int main(){

    char a[5][5];
    for(int i=0; i<5; i++){
        for(int y=0; y<5; y++){
            scanf("%c", &a[i][y]);
        }
    }
    int c=0, cmax=0;
    char max;

    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            for(int x=0; x<5; x++){
                for(int y=0; y<5; y++){
                    if(a[i][j]==a[x][y]){
                        c++;
                    }
                }
            }
            if(c>cmax){
                cmax=c;
                c=0;
                max=a[i][j];
            }else{
                c=0;
            }
        }
    }

    printf("Il carattere che compare piu volte e %c: \n", max);

    for(int i=0; i<5; i++){
        for(int y=0; y<5; y++){
            if(a[i][y]==max){
                printf("* ");
            }else{
                printf("%c ", a[i][y]);
            }
        }
        printf("\n");
    }
    return 0;
}