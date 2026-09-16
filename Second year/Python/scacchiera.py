def SommaPezzi(scacchiera):
    SB = 0
    SN = 0
    for i in range(len(scacchiera)):
        for j in range(len(scacchiera[0])):
            if scacchiera[i][j] == "P":
                SN += 1
            elif scacchiera[i][j] == "C":
                SN += 3
            elif scacchiera[i][j] == "A":
                SN += 3
            elif scacchiera[i][j] == "T":
                SN += 5
            elif scacchiera[i][j] == "D":
                SN += 9
            elif scacchiera[i][j] == "R":
                SN += 0
            if scacchiera[i][j] == "p":
                SB += 1
            elif scacchiera[i][j] == "c":
                SB += 3
            elif scacchiera[i][j] == "a":
                SB += 3
            elif scacchiera[i][j] == "t":
                SB += 5
            elif scacchiera[i][j] == "d":
                SB += 9
            elif scacchiera[i][j] == "r":
                SB += 0
    return SB,SN

def conto(scacchiera):
    cP = cT = cC = cA = cD = cR = cp = ct = cc = ca = cd = cr = 0
    for i in range(len(scacchiera)):
        for j in range(len(scacchiera[0])):
            if scacchiera[i][j] == "P":
                cP += 1
            elif scacchiera[i][j] == "C":
                cC += 1
            elif scacchiera[i][j] == "A":
                cA += 1
            elif scacchiera[i][j] == "T":
                cT += 1
            elif scacchiera[i][j] == "D":
                cD += 1
            elif scacchiera[i][j] == "R":
                cR += 1
            if scacchiera[i][j] == "p":
                cp += 1
            elif scacchiera[i][j] == "c":
                cc += 1
            elif scacchiera[i][j] == "a":
                ca += 1
            elif scacchiera[i][j] == "t":
                ct += 1
            elif scacchiera[i][j] == "d":
                cd += 1
            elif scacchiera[i][j] == "r":
                cr += 1

    if cP <= 8 and cp <= 8 and cT <= 2 and ct <= 2 and cC <= 2 and cc <= 2 and cA <= 2 and ca <= 2 and cD <=1 and cd <=1 and cR <= 1 and cr <= 1:
        return 1
    else:
        return -1


scacchiera = [["T","C","A","D","R","A","C","T"],
             ["P","P","P","P","P","P","P","P"],
             [" "," "," "," "," "," "," "," "],
             [" "," "," "," "," "," "," "," "],
             [" "," "," "," "," "," "," "," "],
             [" "," "," "," "," "," "," "," "],
             ["p","p","p","p","p","p","p","p"],
             ["t","c","a","d","r","a","c","t"]]

SommaBianchi, SommaNeri = SommaPezzi(scacchiera)

if SommaBianchi > SommaNeri:
    print("1")
elif SommaNeri > SommaBianchi:
    print("2")
elif SommaBianchi == SommaNeri:
    print("0")

ContoPezzi = conto(scacchiera)
