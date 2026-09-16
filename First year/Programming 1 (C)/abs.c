#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int main(){
    srand(time(NULL));
    int a[5][5];
    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            a[i][j]=(rand()%(20-0+1)+1);
        }
    }
    for(int i=0; i<5; i++){
        for(int j=0; j<5; j++){
            printf("%2d ", a[i][j]);
        }
        printf("\n");
    }

    int diff=0, max=0;

    for(int indice=0; indice<5; indice++){
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                for(int x=0; x<5; x++){
                    diff=abs(a[j][indice]-a[x][indice]);
                    if(diff>max){
                        max=diff;
                    }
                }
            }
        }
    }

    printf("\n%d\n",max);
    return 0;
}