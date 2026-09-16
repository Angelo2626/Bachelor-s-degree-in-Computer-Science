#include<stdio.h>
#include<stdlib.h>

struct node{
    int val;
    struct node* next;
};

void riempi(struct node** head, int n);
int controllo(struct node* h1, struct node* h2);

int main(){
    struct node* h1=NULL;
    struct node* h2=NULL;

    int n;
    for(int i=0; i<5; i++){
        printf("Inserisci un valore della lista: ");
        scanf("%d", &n);
        riempi(&h1, n);
    }
    for(int i=0; i<5; i++){
        printf("Inserisci un valore della lista: ");
        scanf("%d", &n);
        riempi(&h2, n);
    }

    int r=controllo(h1,h2);
    if(r==1){
        printf("OK!\n");
    }else{
        printf("NO!\n");
    }
    return 0;
}

void riempi(struct node** head, int n){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(0);
    }

    nuovo->val=n;
    nuovo->next=(*head);

    (*head)=nuovo;
}

int controllo(struct node* h1, struct node* h2){
    struct node* t1=h1;
    struct node* t2=h2;
    int c1=1, c2=1, i=3, n1, n2;
    while(t1!=NULL){
        if(c1==3){
            n1=t1->val;
        }
        c1++;
        t1=t1->next;
    }
    while(t2!=NULL){
        if(c2==3){
            n2=t2->val;
        }
        c2++;
        t2=t2->next;
    }
    if(c1==c2 && n1==n2){
        return 1;
    }else{
        return 0;
    }
}