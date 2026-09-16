#include<stdio.h>
#include<stdlib.h>
struct node{
    int data;
    struct node* next;
};

void riempi(struct node** head, int n);
void stampa(struct node* head);
void trova(struct node* head, int num);
void cancella3(struct node** head);
void aggiungicoda(struct node** head, int num);
int conta(struct node* head);
void arr(int c, int a[c], struct node* head);
void inverti(int c, int a[c], struct node** head);

int main(){

    struct node* head=NULL;

    int n;

    for(int i=0; i<5; i++){
        printf("Inserisci un nuovo valore della lista: ");
        scanf("%d", &n);
        riempi(&head, n);
    }

    stampa(head);

    trova(head, 15);

    cancella3(&head);

    stampa(head);
    
    aggiungicoda(&head, 37);

    stampa(head);

    int count=conta(head);

    int a[count];

    arr(count, a, head);

    inverti(count, a, &head);

    stampa(head);    
    return 0;
}

void riempi(struct node** head, int n){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.");
        exit(0);
    }

    nuovo->data=n;
    nuovo->next=(*head);

    (*head)=nuovo;
}

void stampa(struct node* head){
    while(head!=NULL){
        printf("|%d|->", head->data);
        head=head->next;
    }
    printf("NULL\n");
}

void trova(struct node* head, int num){
    int flag=0;
    while(head!=NULL){
        if(head->data==num){
            flag++;
        }
        head=head->next;
    }
    if(flag>0){
        printf("%d e presente nella lista\n", num);
    }else{
        printf("%d non e stato trovato nella lista\n", num);
    }
}

void cancella3(struct node** head){

    if(*head==NULL){
        printf("La lista è vuota.\n");
        return ;
    }

    struct node* tmp=*head;
    struct node* pre= NULL;
    int c=1;

    while(tmp!=NULL && c<3){
        pre=tmp;
        tmp=tmp->next;
        c++;
    }

    if(tmp==NULL){
        printf("La lista ha meno di 3 elementi.\n");
        return;
    }

    pre->next=tmp->next;
    free(tmp);
    printf("Nodo 3 cancellato.\n");
}

void aggiungicoda(struct node** head, int num){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    nuovo->data=num;
    nuovo->next=NULL;
    if((*head)==NULL){
        (*head)=nuovo;
        return;
    }
    struct node* tmp=(*head);
    while(tmp->next!=NULL){
        tmp=tmp->next;
    }
    tmp->next=nuovo;
}

int conta(struct node* head){
    int count=0;
    while(head!=NULL){
        head=head->next;
        count++;
    }
    return count;
}

void arr(int c, int a[c], struct node* head){
    struct node* tmp=head;
    int i=c-1;
    while(tmp!=NULL){
        a[i]=tmp->data;
        tmp=tmp->next;
        i--;
    }
}

void inverti(int c, int a[c], struct node** head){
    struct node* tmp=(*head);
    for(int i=0; i<c; i++){
        tmp->data=a[i];
        tmp=tmp->next;
    }

}