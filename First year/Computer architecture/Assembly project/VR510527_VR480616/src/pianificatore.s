.section .data

buffer:
    .space 1024
b_len:
    .long . - buffer
buffer_len:
    .long 0         

bufferitoa:
    .space 1024

menu:
    .ascii "Scegli l'algoritmo desiderato:\n1. Earliest Deadline First (EDF)\n2. Highest Priority First (HPF)\n3. Esci\nScelta: "
menu_len:
    .long . - menu

scelta: 
    .ascii "0\n"
scelta_len:
    .long . - scelta

numero_nv:
    .ascii "Hai inserito un valore non valido, inserire un valore che sia 1, 2 o 3.\n"

numero_nv_len:
    .long . - numero_nv

punti:
    .ascii ":"
punti_len:
    .long . - punti

ac:
    .ascii "\n"
ac_len:
    .long . - ac

con:
    .ascii "Conclusione: "
con_len:
    .long . - con

pen:
    .ascii "\nPenalty: "
pen_len:
    .long . - pen 

pedf:
    .ascii "Pianificazione EDF:\n"
pedf_len:
    .long . - pedf

phpf:  
    .ascii "Pianificazione HPF:\n"
phpf_len:
    .long . - phpf

tmp:
    .long 0
contatore:
    .long 0

id1:
    .long 0
durata1:
    .long 0
scadenza1:
    .long 0
priorita1:
    .long 0

id2:
    .long 0
durata2:
    .long 0
scadenza2:
    .long 0
priorita2:
    .long 0

id3:
    .long 0
durata3:
    .long 0
scadenza3:
    .long 0
priorita3:
    .long 0

id4:
    .long 0
durata4:
    .long 0
scadenza4:
    .long 0
priorita4:
    .long 0

id5:
    .long 0
durata5:
    .long 0
scadenza5:
    .long 0
priorita5:
    .long 0

id6:
    .long 0
durata6:
    .long 0
scadenza6:
    .long 0
priorita6:
    .long 0

id7:
    .long 0
durata7:
    .long 0
scadenza7:
    .long 0
priorita7:
    .long 0

id8:
    .long 0
durata8:
    .long 0
scadenza8:
    .long 0
priorita8:
    .long 0

id9:
    .long 0
durata9:
    .long 0
scadenza9:
    .long 0
priorita9:
    .long 0

id10:
    .long 0
durata10:
    .long 0
scadenza10:
    .long 0
priorita10:
    .long 0

.section .text

.global _start

_start:

movl $5, %eax          
movl 8(%esp), %ebx   
movl $0, %ecx          
int $0x80              
test %eax, %eax        
js termina_programma                

movl %eax, %ebx        

movl $3, %eax
movl $buffer, %ecx
movl $1024, %edx
int $0x80

test %eax, %eax
js termina_programma

movl %eax, buffer_len

leal buffer, %esi

xorl %eax, %eax
xorl %ebx, %ebx
xorl %ecx, %ecx
xorl %edx, %edx

leal id1, %edi

parse_loop:

    movb (%esi, %ecx, 1), %al
    incl %ecx
    cmpb $0, %al
    je parse_end

    cmpl $10, %edx
    jge parse_end

    cmpb $44, %al
    je store_num
    cmpb $10, %al
    je next_line

    subb $48, %al
    imull $10, %ebx
    addl %eax, %ebx

    jmp parse_loop

store_num:

    movl %ebx, (%edi)
    addl $4, %edi
    xorl %ebx, %ebx
    jmp parse_loop

next_line:

    pushl %ecx
    pushl %edx

    subl $221, %ebx
    movl $10, %ecx
    xorl %edx, %edx
    movl %ebx, %eax
    divl %ecx

    movl %eax, (%edi)
    addl $4, %edi

    leal tmp, %ebx
    addl $1, (%ebx)

    xorl %ebx, %ebx
    xorl %eax, %eax

    popl %edx
    popl %ecx
    incl %edx

    jmp parse_loop

parse_end:

    movl %ebx, (%edi)
    leal tmp, %ebx
    addl $1, (%ebx)

stampa_menu:

    movl $4, %eax
    movl $1, %ebx 
    leal menu, %ecx 
    movl menu_len, %edx
    int $0x80 

inserimento:

    movl $3, %eax 
    movl $1, %ebx 
    leal scelta, %ecx
    movl scelta_len, %edx
    incl %edx
    int $0x80 

conversione_str2num:

    leal scelta, %esi 

    xorl %eax, %eax 
    xorl %ebx, %ebx 
    xorl %ecx, %ecx 
    xorl %edx, %edx 

ripeti: 

    movb (%ecx, %esi, 1), %bl 

    cmp $10, %bl 
    je fine_conversione

    subb $48, %bl 
    movl $10, %edx 
    mulb %dl 
    addl %ebx, %eax 

    inc %ecx 
    jmp ripeti 

fine_conversione: 

    cmpl $1, %eax
    je inizio_edf
    cmpl $2, %eax
    je inizio_hpf
    cmpl $3, %eax
    je termina_programma

numero_non_valido:

    movl $4, %eax
    movl $1, %ebx 
    leal numero_nv, %ecx 
    movl numero_nv_len, %edx
    int $0x80 

    jmp stampa_menu

inizio_edf:

    movl $4, %eax
    movl $1, %ebx 
    leal pedf, %ecx 
    movl pedf_len, %edx
    int $0x80 

edf:

    leal id1, %edi
    movl $1, %ecx

loop_edf:

    leal id10, %esi
    cmpl %esi, %edi
    jae exit

    movl 16(%edi), %eax
    cmpl $0, %eax
    je exit

    movl 8(%edi), %eax 
    movl 24(%edi), %ebx 

    cmpl %eax, %ebx
    jl ordina_edf
    je prio 

    movl tmp, %ebx 

    addl $1, %ecx 
    cmpl %ebx, %ecx 
    je exit 

    addl $16, %edi 
    jmp loop_edf

ordina_edf:

    movl (%edi), %eax
    movl 16(%edi), %ebx
    movl %ebx, (%edi)
    movl %eax, 16(%edi)

    movl 4(%edi), %eax
    movl 20(%edi), %ebx
    movl %ebx, 4(%edi)
    movl %eax, 20(%edi)

    movl 8(%edi), %eax
    movl 24(%edi), %ebx
    movl %ebx, 8(%edi)
    movl %eax, 24(%edi)

    movl 12(%edi), %eax
    movl 28(%edi), %ebx
    movl %ebx, 12(%edi)
    movl %eax, 28(%edi)

    jmp edf

prio:

    movl 12(%edi), %eax
    movl 28(%edi), %ebx

    cmpl %eax, %ebx
    jg ordina_edf

    addl $16, %edi

    jmp loop_edf

inizio_hpf:

    movl $4, %eax
    movl $1, %ebx 
    leal phpf, %ecx 
    movl phpf_len, %edx
    int $0x80 

hpf:

    leal id1, %edi
    movl $1, %ecx  

loop_hpf:
    
    leal id10, %esi
    cmpl %esi, %edi
    jae exit

    movl 16(%edi), %eax
    cmpl $0, %eax
    je exit

    movl 12(%edi), %eax 
    movl 28(%edi), %ebx 

    cmpl %eax, %ebx
    jg ordina_hpf
    je sca

    movl tmp, %ebx 

    addl $1, %ecx 
    cmpl %ebx, %ecx 
    je exit

    addl $16, %edi 
    jmp loop_hpf 

ordina_hpf:

    movl (%edi), %eax 
    movl 16(%edi), %ebx 
    movl %ebx, (%edi)
    movl %eax, 16(%edi)

    movl 4(%edi), %eax 
    movl 20(%edi), %ebx 
    movl %ebx, 4(%edi)
    movl %eax, 20(%edi)

    movl 8(%edi), %eax 
    movl 24(%edi), %ebx 
    movl %ebx, 8(%edi)
    movl %eax, 24(%edi)

    movl 12(%edi), %eax 
    movl 28(%edi), %ebx 
    movl %ebx, 12(%edi)
    movl %eax, 28(%edi)
    
    jmp hpf

sca:

    movl 8(%edi), %eax 
    movl 24(%edi), %ebx 

    cmpl %eax, %ebx
    jl ordina_hpf

    addl $16, %edi
    
    jmp loop_hpf

aggiornacontatore:

    popl %edx 
    addl $1, %edx 
    pushl %edx 

    jmp convert_loop

itoa:

    movl $4, %ebx 

    pushl %ebx

    leal bufferitoa, %ecx

    movl %eax, %edx
    movl $10, %ebx

convert_loop:

    xorl %edx, %edx
    divl %ebx
    addb $'0', %dl
    decl %ecx
    movb %dl, (%ecx)
    testl %eax, %eax
    jnz aggiornacontatore

    leal (%ecx), %eax

    popl %edx

    jmp id_str

aggiornacontatore2:

    popl %edx 
    addl $1, %edx 
    pushl %edx 

    jmp convert_loop2

itoa2:

    xorl %edx, %edx

    movl $4, %ebx 

    pushl %ebx

    leal bufferitoa, %ecx

    movl %eax, %edx
    movl $10, %ebx

convert_loop2:

    xorl %edx, %edx
    divl %ebx
    addb $'0', %dl
    decl %ecx
    movb %dl, (%ecx)
    testl %eax, %eax
    jnz aggiornacontatore2

    leal (%ecx), %eax

    popl %edx

    jmp time_str

id_str:

    movl %eax, %ecx

    movl $4, %eax
    movl $1, %ebx 
    int $0x80 

    movl $4, %eax
    movl $1, %ebx 
    leal punti, %ecx 
    movl punti_len, %edx
    int $0x80 

    popl %eax 
    pushl %eax 

    jmp itoa2 

time_str:

    movl %eax, %ecx

    movl $4, %eax
    movl $1, %ebx 
    int $0x80

    movl $4, %eax
    movl $1, %ebx 
    leal ac, %ecx 
    movl ac_len, %edx
    int $0x80 

    jmp fine_stampa

exit:

    movl tmp, %ecx
    cmpl $10, %ecx
    jg fix_tmp
    
    leal id1, %edi
    xorl %ecx, %ecx
    xorl %edx, %edx
    xorl %esi, %esi

    pushl %ecx

ciclo_stampa: 

    movl (%edi), %eax

    jmp itoa

agg_ciclo_stampa:

    popl %edx 
    addl 4(%edi), %edx 
    pushl %edx

    addl $16, %edi

    leal id10, %eax
    cmpl %eax, %edi
    ja fine_stampa

    jmp ciclo_stampa

fine_stampa:

    movl tmp, %edx
    incl %esi

    cmpl %edx, %esi

    je msg_finali

    jmp agg_ciclo_stampa 

msg_finali:

    movl $4, %eax
    movl $1, %ebx 
    leal con, %ecx 
    movl con_len, %edx
    int $0x80 

    popl %eax
    addl 4(%edi), %eax
    jmp itoa3

aggiornacontatore3:

    popl %edx 
    addl $1, %edx 
    pushl %edx 

    jmp convert_loop3

itoa3:

    xorl %edx, %edx

    movl $4, %ebx 

    pushl %ebx

    leal bufferitoa, %ecx

    movl %eax, %edx
    movl $10, %ebx

convert_loop3:

    xorl %edx, %edx
    divl %ebx
    addb $'0', %dl
    decl %ecx
    movb %dl, (%ecx)
    testl %eax, %eax
    jnz aggiornacontatore3

    leal (%ecx), %eax

    popl %edx

    movl $4, %eax
    movl $1, %ebx 
    int $0x80 


    movl $4, %eax
    movl $1, %ebx 
    leal pen, %ecx 
    movl pen_len, %edx
    int $0x80 

    jmp calcolo_penalita

fine_calcolo_pen:

    popl %edx
    movl %eax, %ecx 

    movl $4, %eax
    movl $1, %ebx 
    int $0x80     

    movl $4, %eax
    movl $1, %ebx 
    leal ac, %ecx 
    movl ac_len, %edx
    int $0x80  

    jmp clean_var

termina_programma:

    movl $1, %eax
    xorl %ebx, %ebx
    int $0x80

calcolo_penalita:

    leal id1, %edi 

    movl tmp, %ebx
    movl %ebx, contatore

    xorl %ebx, %ebx
    xorl %edx, %edx
    
    pushl %edx 

ciclo_pen:

    addl 4(%edi), %ebx
    movl 8(%edi), %ecx

    cmpl %ebx, %ecx
    jge pen_calcolata

    pushl %ebx

    subl %ecx, %ebx
    movl 12(%edi), %eax

    imull %ebx, %eax

    popl %ebx
    popl %edx
    addl %eax, %edx
    pushl %edx

pen_calcolata:

    movl contatore, %ecx
    subl $1, %ecx
    movl %ecx, contatore
    cmpl $0, %ecx

    jle itoa4

    addl $16, %edi

    leal id10, %esi
    cmpl %esi, %edi
    ja itoa4
 
    jmp ciclo_pen

aggiornacontatore4:

    popl %edx 
    addl $1, %edx 
    pushl %edx 

    jmp convert_loop4

itoa4:

    popl %eax

    xorl %edx, %edx

    movl $4, %ebx 

    pushl %ebx

    leal bufferitoa, %ecx

    movl %eax, %edx
    movl $10, %ebx

convert_loop4:
    xorl %edx, %edx
    divl %ebx
    addb $'0', %dl
    decl %ecx
    movb %dl, (%ecx)
    testl %eax, %eax
    jnz aggiornacontatore4

    leal (%ecx), %eax

    jmp fine_calcolo_pen

clean_var:

    leal bufferitoa, %edi
    movl $1024, %ecx

clean_bufferitoa:
    movb $0, (%edi)
    inc %edi
    decl %ecx
    jnz clean_bufferitoa

    leal buffer, %edi
    movl tmp, %ecx

clean_buffer:

    movb $0, (%edi)
    inc %edi 
    decl %ecx
    cmpl $0, %ecx
    jne clean_buffer
 
    leal id1, %edi
    xorl %ecx, %ecx
    movl %ecx, tmp
    movl %ecx, contatore

clean_loop:

    movl $0, (%edi)
    incl %ecx

    cmpl $40, %ecx
    je _start

    addl $4, %edi
    jmp clean_loop

fix_tmp:

    movl $10, tmp
    jmp exit
