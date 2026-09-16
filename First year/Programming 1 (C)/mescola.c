#include<stdio.h>
#include<stdlib.h>
#include<time.h>

int main(){
    srand(time(NULL));
    int array[9]={1,2,3,4,5,6,7,8,9};
        for (int i = 9 - 1; i > 0; i--) {
        int j = rand() % (i + 1);

        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    for(int i=0; i<9; i++){
        printf("%d ", array[i]);
    }
    printf("\n");
    return 0;
}
