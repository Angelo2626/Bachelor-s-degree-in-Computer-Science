#include<stdio.h>

void calcolo(float r, float *pp, float *pa);

int main(){
    
    float r;

    printf("Inerisci il raggio del cerchio: ");
    scanf("%f", &r);

    float perimetro, area, *pp=&perimetro, *pa=&area;

    calcolo(r,pp,pa);

    printf("L'area del cerchio e %f mentre il perimetro del cerchio e %f\n", *pa, *pp);

    int input;

    do{
        printf("Per continuare premere 0, un altro numero per terminare il programma: ");
        scanf("%d", &input);

        if(input==0){
            float r;

            printf("Inerisci il raggio del cerchio: ");
            scanf("%f", &r);

            float perimetro, area, *pp=&perimetro, *pa=&area;

            calcolo(r,pp,pa);

            printf("L'area del cerchio e %f mentre il perimetro del cerchio e %f\n", *pa, *pp);

        }else{
            printf("Programma terminato.\n");
            break;
        }
    }while(1);

    return 0;
}

void calcolo(float r, float *pp, float *pa){
    *pp=(3.14159*2)*r;
    *pa=(r*r)*3.14159;
}