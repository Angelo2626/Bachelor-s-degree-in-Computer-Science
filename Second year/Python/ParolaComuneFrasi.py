words1 = input().split()
words2 = input().split()

result = []

for parola in words1:
    if parola in words2 and parola not in result:
        result.append(parola)

print(result)
