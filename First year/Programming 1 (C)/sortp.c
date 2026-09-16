#include<stdio.h>

void sort(int *pa, int *pb, int *pc);

int main(){

    int a=12, b=3, c=14;
    int *pa=&a, *pb=&b, *pc=&c;

    sort(pa, pb, pc);
    return 0;
}

void sort(int *pa, int *pb, int *pc){
    if(*pa<*pb && *pa<*pc){
        if(*pb<*pc){
            printf("%d  %d  %d\n", *pa, *pb, *pc);
        }else{
            printf("%d  %d  %d\n", *pa, *pc, *pb);
        }
    }else if(*pb<*pa && *pb<*pc){
        if(*pa<*pc){
            printf("%d  %d  %d\n", *pb, *pa, *pc);
        }else{
            printf("%d  %d  %d\n", *pb, *pc, *pa);
        }
    }else if(*pc<*pa && *pc<*pb){
        if(*pa<*pb){
            printf("%d  %d  %d\n", *pc, *pa, *pb);
        }else{
            printf("%d  %d  %d\n", *pc, *pb, *pa);
        }
    }
}