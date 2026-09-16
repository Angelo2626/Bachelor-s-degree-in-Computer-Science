#include<stdio.h>
#include<stdlib.h>

void pitagora(int a[5][10]);

int main(){

    FILE *f=fopen("Nuovo_file.txt", "w");
    if(f==NULL){
        printf("Errore nell'apertura/creazione del file.\n");
    }else{
        printf("File aperto/creato con successo.\n");
    }

    int a[5][10];
    pitagora(a);

    for(int i=0; i<5; i++){
        for(int j=0; j<10; j++){
            fprintf(f, "%3d ", a[i][j]);
        }
        fprintf(f,"\n");
    }

    fclose(f);
    printf("File chiuso.\n");
    return 0;
}

void pitagora(int a[5][10]){
    for(int i=0; i<5; i++){
        for(int j=0; j<10; j++){
            if(j==0){
                a[i][j]=i+1;
            }else{
                a[i][j]=(i+1)*(j+1);
            }
        }
    }
}