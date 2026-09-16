#include<stdio.h>
int main(){
    int a[4][4];
    for(int i=0; i<4; i++){
        for(int j=0; j<4; j++){
            scanf("%d ", &a[i][j]);
        }
    }

    int flag=0, ipr=0, ise=3;
    for(int i=0; i<4; i++){
        if(a[ise][ise]>a[ipr][ipr]){
            flag++;
        }
        ipr++;
        ise--;
    }

    if(flag==4){
        printf("OK\n");
    }else{
        printf("NO\n");
    }
    return 0;
}