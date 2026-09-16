text = input()
see = []
for char in text:
    if char not in see:
        see.append(char)

print(''.join(see))
