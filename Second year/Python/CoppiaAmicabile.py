def sum_of_divisor(n):
    somma = 0
    for i in range(1, n):
        if n % i == 0:
            somma += i
    return somma
def is_amicable_pair(n1, n2):
    if n1 == sum_of_divisor(n2) and n2 == sum_of_divisor(n1):
        return True
    else:
        return False

n1 = int(input())
n2 = int(input())
print(is_amicable_pair(n1, n2))
