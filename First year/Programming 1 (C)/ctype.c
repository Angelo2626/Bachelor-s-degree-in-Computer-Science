#include<stdio.h>
#include<stdlib.h>
#include<ctype.h>

struct node{
    char carattere;
    struct node* next;
};

void inserisci(struct node** head, char c);
void stampa(struct node* head);

int main(){
    char c[256];
    printf("Inserisci una stringa: ");
    scanf("%s", c);
    struct node* head=NULL;
    for(int i=0; c[i]!='\0'; i++){
        inserisci(&head,c[i]);
    }
    stampa(head);
    return 0;
}

void inserisci(struct node** head, char c){
    if(isalpha(c)){
        struct node* nuovo=(struct node*)malloc(sizeof(struct node));
        if(nuovo==NULL){
            printf("Errore nell'allocazione della memoria.\n");
            exit(1);
        }
        nuovo->carattere=c;
        nuovo->next=NULL;
        if((*head)==NULL){
            *head=nuovo;
        }else{
            struct node* tmp=*head;
            while(tmp->next!=NULL){
                tmp=tmp->next;
            }
            tmp->next=nuovo;
        }
    }else{
        return;
    }
}

void stampa(struct node* head){
    struct node* tmp=head;
    while(tmp->next!=NULL){
        printf("%c->", tmp->carattere);
        tmp=tmp->next;
    }
    printf("%c\n", tmp->carattere);
}