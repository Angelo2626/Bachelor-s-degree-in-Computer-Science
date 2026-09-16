def conta_vocali(text):
    vocali = "aeiou"
    count = 0
    for char in text:
        if char in vocali:
            count += 1
    return count
text = input().lower()
count = conta_vocali(text)
print(count)
