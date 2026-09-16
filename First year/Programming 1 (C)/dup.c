#include<stdio.h>
#define n 3
#define m 4

int main(){
    int a[n][m], b[n*m];
    for(int x=0; x<n; x++){
        for(int y=0; y<m; y++){
            scanf("%d", &a[x][y]);
        }
    }

    int i=0, c=0;
    for(int x=0; x<n; x++){
        for(int y=0; y<m; y++){
            if(a[x][y]>0){
                b[i]=a[x][y];
                i++;
                c++;
            }
        }
    }
    for(int x=0; x<c; x++){
        printf("%d ", b[x]);
    }
    printf("\n");
    return 0;
}