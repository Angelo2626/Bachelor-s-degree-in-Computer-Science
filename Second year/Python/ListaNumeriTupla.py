numbers = input().split()
numbers = [int(num) for num in numbers]

t = ()

for num in numbers:
    t = t + (num,) #ricorda la virgola

print(t)
