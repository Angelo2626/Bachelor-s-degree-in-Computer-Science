def longest_word(stringa):
    count = 0
    inizio = 0
    max = " "
    space = " "
    for i in range(len(stringa)):
        if stringa[i] != space:
            count += 1
        elif stringa[i] == space:
            if count > len(max):
                max = stringa[inizio:i]
                inizio = i + 1
                count = 0
            else:
                inizio = i + 1
                count = 0

    if count > len(max):
        max = stringa[inizio:len(stringa)]

    return max


stringa = input()
print(longest_word(stringa))
