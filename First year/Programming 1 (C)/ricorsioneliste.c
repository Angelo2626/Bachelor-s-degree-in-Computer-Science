#include<stdio.h>
#include<stdlib.h>

typedef struct EL{
    int dato1;
    int dato2;
    struct EL *next;
}ELEMENTO;

void inserisci(ELEMENTO **head);
int contamultipli(ELEMENTO *head);
int multiplo(int n1, int n2);
void stampa(ELEMENTO *head);

int main(){
    ELEMENTO *head=NULL;
    int n1,n2;
    for(int i=0; i<5; i++){
        inserisci(&head);
    }
    int c=contamultipli(head);
    stampa(head);
    if(c == -1){
        printf("\nNon ci sono multipli nella lista.\n");
    }else{
        printf("\nCi sono %d Multipli nella lista.\n", c);
    }
    return 0;
}

void inserisci(ELEMENTO **head){
    ELEMENTO *nuovo=(ELEMENTO *)malloc(sizeof(ELEMENTO));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    printf("Inserisci dato1: ");
    scanf("%d", &nuovo->dato1);
    printf("Inserisci dato2: ");
    scanf("%d", &nuovo->dato2);
    nuovo->next=(*head);
    (*head)=nuovo;
}

int contamultipli(ELEMENTO *head){
    if(head==NULL){
        return -1;
    }else{
        int c=0;
        ELEMENTO *tmp=head;
        while(tmp!=NULL){
            int v=multiplo(tmp->dato1, tmp->dato2);
            if(v==1){
                c++;
            }
            tmp=tmp->next;
        }
        return c;
    }
}

int multiplo(int n1, int n2){
    if(n2>n1){
        return -1;
    }else if(n2==n1){
        return 1;
    }else if(n2<n1){
        return multiplo(n1, n2+n2);
    }
}

void stampa(ELEMENTO *head){
    ELEMENTO *tmp=head;
    while(tmp!=NULL){
        printf("|%d - %d|->", tmp->dato1, tmp->dato2);
        tmp=tmp->next;
    }
    printf("NULL");
}