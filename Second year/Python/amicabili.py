def sommadivisori(n):
    somma = 0
    for i in range(1, n):
        if n % i == 0:
            somma += i
    return somma

def amicabili(n1, n2):
    if sommadivisori(n1) == n2 and sommadivisori(n2) == n1:
        return True
    else:
        return False

n1 = int(input())
n2 = int(input())
print(amicabili(n1,n2))
