#include<stdio.h>

void sort(int *p1, int *p2, int *p3);

int main(){

    int a=12, b=3, c=14;
    int *p1=&a, *p2=&b, *p3=&c;

    sort(p1, p2, p3);

    printf("%d  %d  %d\n", *p1, *p2, *p3);
    return 0;
}


void sort(int *p1, int *p2, int *p3){
    int tmp=0;
    if(*p1<*p2 && *p1<*p3){
        if(*p3<*p2){
            tmp=*p2;
            *p2=*p3;
            *p3=tmp;
        }
    }else if(*p2<*p1 && *p2<*p3){
        if(*p1<*p3){
            tmp=*p1;
            *p1=*p2;
            *p2=tmp;
        }else{
            tmp=*p1;
            *p1=*p2;
            *p2=tmp;
            tmp=*p3;
            *p2=*p3;
            *p3=*p1;
        }
    }else if(*p3<*p1 && *p3<*p2){
        if(*p1<*p2){
            tmp=*p1;
            *p1=*p3;
            *p3=*p2;
            *p2=tmp;
        }else{
            tmp=*p1;
            *p1=*p3;
            *p3=tmp;
        }
    }
}