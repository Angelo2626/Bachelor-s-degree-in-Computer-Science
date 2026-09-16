#include<stdio.h>
#include<string.h>
#include<math.h>

typedef struct str_loc{
    float longitudine;
    float latitudine;
    char nome[30];
}localita;

void calcolo(localita loc[10]);

int main(){

    localita loc[10];

    for(int i=0; i<10; i++){
        printf("Inserisci la longitudine: ");
        scanf("%f", &loc[i].longitudine);
        printf("Inserisci la latitudine: ");
        scanf("%f", &loc[i].latitudine);
        printf("Inserisci il nome della localita: ");
        scanf("%s", loc[i].nome);
    }

    calcolo(loc);

    return 0;
}

void calcolo(localita loc[10]){

    float d, max=0;

    for(int i=0; i<9; i++){
        d=sqrt((pow((loc[i+1].latitudine-loc[i].latitudine),2))+(pow((loc[i+1].longitudine-loc[i].longitudine),2)));
        if(d>max){
            max=d;
        }
    }
    for(int i=0; i<10; i++){
        d=sqrt((pow((loc[i+1].latitudine-loc[i].latitudine),2))+(pow((loc[i+1].longitudine-loc[i].longitudine),2)));
        if(d==max){
            printf("[%s]-->[%s]\n", loc[i].nome, loc[i+1].nome);
        }
    }
}