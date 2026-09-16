#include<stdio.h>
#include<stdlib.h>

struct node{
    int eta;
    int sesso;
    struct node* next;
};

void inserisci(struct node** head, int eta, int sesso);
void stampa(struct node* head);
int adolescenti(struct node* head, int c, int ado[]);
int don(struct node* head, int c, int donne[]);
int uom(struct node* head, int c, int uomini[]);
void ordina(int c,int a[c]);

int main(){
    struct node* head=NULL;
    int eta,sesso,c=0;
    while(1){
        printf("inserisci l'eta della persona (1-100): ");
        scanf("%d", &eta);
        if(eta==-1){
            break;
        }
        printf("Inserisci il sesso della persona (1=uomo/2=donna): ");
        scanf("%d", &sesso);
        inserisci(&head, eta, sesso);
        c++;
    }
    stampa(head);
    int *ado=(int*)malloc(c*sizeof(int)), *donne=(int*)malloc(c*sizeof(int)), *uomini=(int*)malloc(c*sizeof(int));
    int ca=adolescenti(head,c,ado);
    int cd=don(head,c,donne);
    int cm=uom(head,c,uomini);
    ado=realloc(ado,ca*sizeof(int));
    printf("adolescenti: ");
    for(int i=0; i<ca; i++){
        printf("%d ", ado[i]);
    }
    donne=realloc(donne,cd*sizeof(int));
    ordina(cd,donne);
    printf("\ndonne: ");
    for(int i=0; i<cd; i++){
        printf("%d ", donne[i]);
    }
    uomini=realloc(uomini,cm*sizeof(int));
    ordina(cm,uomini);
    printf("\nuomini: ");
    for(int i=0; i<cm; i++){
        printf("%d ", uomini[i]);
    }
    return 0;
}

void inserisci(struct node** head, int eta, int sesso){
    struct node* nuovo=(struct node*)malloc(sizeof(struct node));
    if(nuovo==NULL){
        printf("Errore nell'allocazione della memoria.\n");
        exit(1);
    }
    nuovo->eta=eta;
    nuovo->sesso=sesso;
    nuovo->next=*head;
    *head=nuovo;
}

void stampa(struct node* head){
    struct node* tmp=head;
    while(tmp!=NULL){
        printf("%d-%d -->", tmp->eta, tmp->sesso);
        tmp=tmp->next;
    }
    printf("NULL\n");
}

int adolescenti(struct node* head, int c, int ado[]){
    int count=0;
    struct node* tmp=head;
    while(tmp!=NULL){
        if(tmp->eta<20){
            ado[count]=tmp->eta;
            count++;
        }
        tmp=tmp->next;
    }
    return count;
}

int don(struct node* head, int c, int donne[]){
    int count=0;
    struct node* tmp=head;
    while(tmp!=NULL){
        if(tmp->sesso==2){
            donne[count]=tmp->eta;
            count++;
        }
        tmp=tmp->next;
    }
    return count;
}

int uom(struct node* head, int c, int uomini[]){
    int count=0;
    struct node* tmp=head;
    while(tmp!=NULL){
        if(tmp->sesso==1){
            uomini[count]=tmp->eta;
            count++;
        }
        tmp=tmp->next;
    }
    return count;
}

void ordina(int c,int a[c]){
    int pre;
    for(int i=0; i<c; i++){
        for(int j=0; j<c-1; j++){
            if(a[i]<a[j]){
                pre=a[j];
                a[j]=a[i];
                a[i]=pre;
            }
        }
    }
}