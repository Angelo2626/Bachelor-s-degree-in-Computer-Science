#include<stdio.h>
#include<stdlib.h>

struct node{
    int data;
    struct node* next;
};

int contaCifre(int n);
void positivo(int c, int n, int a[c]);
void negativo(int c, int n, int a[c]);
void stampa(struct node* head);
void inserisci(int c, int a[c], struct node** head);

int main(){

    struct node* head=NULL;
    int n;
    printf("Inserisci un numero: ");
    scanf("%d", &n);

    int c=contaCifre(n);
    int a[c];
    if(n>=0){
        positivo(c,n,a);
    }else{
        negativo(c,n,a);
    }
    inserisci(c,a,&head);
    stampa(head);
    return 0;
}

int contaCifre(int n){
    int c=0;
    n=abs(n);
    while(n>0){
        n=n/10;
        c++;
    }
    return c;
}

void negativo(int c, int n, int a[c]){
    n=abs(n);
    for(int i=c-1; i>=0; i--){
        a[i]=n%10;
        n=n/10;
    }
}

void positivo(int c, int n, int a[c]){
    for(int i=0; i<c; i++){
        a[i]=n%10;
        n=n/10;
    }
}

void stampa(struct node* head){
    while(head!=NULL){
        printf("|%d|->", head->data);
        head=head->next;
    }
    printf("NULL\n");
}

void inserisci(int c, int a[c], struct node** head){
    for(int i=0; i<c; i++){
        for(int j=0; j<a[i]; j++){
            struct node* nuovo=(struct node*)malloc(sizeof(struct node));
            if(nuovo==NULL){
                printf("Errore nell'allocazione della memoria.\n");
                exit(1);
            }
            nuovo->data=a[i];
            nuovo->next=(*head);

            (*head)=nuovo;
        }
    }
}