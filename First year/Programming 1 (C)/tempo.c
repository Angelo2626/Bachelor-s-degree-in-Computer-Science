#include<stdio.h>

typedef struct tempo_str{
    int ore;
    int minuti;
    int secondi;
}tempo;

void calcolo(tempo t[3], tempo risultato);

int main(){

    tempo t[3], risultato;

    for(int i=0; i<3; i++){
        
        printf("Inserisci le ore: ");
        scanf("%d", &t[i].ore);
        printf("Inserisci i minuti: ");
        scanf("%d", &t[i].minuti);
        printf("Inserisci i secondi: ");
        scanf("%d", &t[i].secondi);

    }


    calcolo(t,risultato);

    return 0;
}

void calcolo(tempo t[3], tempo risultato){
    risultato.ore=0, risultato.minuti=0, risultato.secondi=0;
    int ro=0, rm=0, rs=0;

    for(int i=0; i<3; i++){
        risultato.secondi=risultato.secondi+t[i].secondi;
        if(risultato.secondi>=60){
            risultato.secondi=risultato.secondi-60;
            rs++;
        }else{
            rs=0;
        }

        risultato.minuti=risultato.minuti+t[i].minuti+rs;
        if(risultato.minuti>=60){
            risultato.minuti=risultato.minuti-60;
            rm++;
        }else{
            rm=0;
        }
        risultato.ore=risultato.ore+t[i].ore+rm;
        if(risultato.ore>=24){
            risultato.ore=risultato.ore-24;
            ro++;
        }
    }

    printf("%d giorno/i, %d ore/i, %d minuto/i, %d secondo/i", ro, risultato.ore, risultato.minuti, risultato.secondi);
}