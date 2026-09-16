numbers = input().split()
numbers = [int(num) for num in numbers]

result = 0
for num in numbers:
    if num > result:
        result = num
print(result)
