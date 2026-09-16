myinput = input().split()
numbers = [int(num) for num in myinput]

result = 0
for num in numbers:
    result = result + num
print(result)
