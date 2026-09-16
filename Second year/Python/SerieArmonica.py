def harmonic_series(n):
    serie = 0
    if n <= 0:
        return 0
    else:
        for i in range(1, n+1):
            serie = 1/i + serie
    return serie

n = int(input())
harmonic_series(n)
print(round(harmonic_series(n), 2))
