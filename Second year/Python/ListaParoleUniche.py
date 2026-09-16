words = input().split()

result = []

for parola in words:
    if parola not in result:
        result.append(parola)

print(result)
