#include<stdio.h>
#include<stdlib.h>

struct node{
    int val;
    struct node* next;
};

void inserisci (struct node** head, int n);
void sum(struct node** somma, struct node* head, struct node* testa);
void stampa(struct node* h);

int main(){
    struct node* head=NULL;
    int n;
    for(int i=0; i<5; i++){
        printf("Inserisci numero: ");
        scanf("%d", &n);
        inserisci(&head, n);
    }
    struct node* testa=NULL;
    for(int i=0; i<5; i++){
        printf("Inserisci numero: ");
        scanf("%d", &n);
        inserisci(&testa, n);
    }

    struct node* somma=NULL;
    sum(&somma, head, testa);
    stampa(head);
    printf(" +\n");
    stampa(testa);
    printf(" =\n");
    stampa(somma);
    return 0;
}

void inserisci (struct node** head, int n){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    nuovo->val=n;
    nuovo->next=(*head);

    (*head)=nuovo;

}

void sum(struct node** somma, struct node* head, struct node* testa){
    struct node* tmp=head;
    struct node* tmd=testa;
    int s=0;
    while(tmp!=NULL){
        s=(tmp->val)+(tmd->val);
        struct node* nuovo=(struct node*)malloc(sizeof(struct node));
        if(nuovo==NULL){
            printf("Errore nell'allocazione della memoria.\n");
            exit(1);
        }
        nuovo->val=s;
        nuovo->next=NULL;
        if(*somma==NULL){
            *somma=nuovo;
        }else{
            struct node* temp=*somma;
            while(temp->next!=NULL){
                temp=temp->next;
            }
            temp->next=nuovo;
        }
        tmp=tmp->next;
        tmd=tmd->next;
    }
}

void stampa(struct node* h){
    struct node* tmp=h;
    while(tmp!=NULL){
        printf("|%d|->", tmp->val);
        tmp=tmp->next;
    }
    printf("NULL");
}