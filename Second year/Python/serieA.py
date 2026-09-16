def seriearmonica(n):
    serie = 0
    for i in range(1, n+1):
        serie = serie + (1/i)
    return round(serie, 1)
n = int(input())
print(seriearmonica(n))
