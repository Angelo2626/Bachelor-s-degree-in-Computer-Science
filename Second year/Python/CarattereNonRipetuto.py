def ripetizione(text):
    flag = 0
    for i in range(1, len(text)):
        if text[0] != text[1]:
            print(text[0])
            flag = 1
            break
        if text[i] != text[i-1]:
            print(text[i])
            flag = 1
            break
    if flag == 0:
        print (-1)

text = input()
ripetizione(text)
