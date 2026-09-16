def frazione_uguali(nome_file):
    count_corrette = 0
    count_totali = 0

    with open(nome_file, "r") as file:
        for riga in file:
            riga = riga.strip().rstrip(";")
            if not riga:
                continue

            parti = riga.split("=")
            if len(parti) != 2:
                continue

            sinistra = sum(int(x) for x in parti[0].split("+"))
            destra = sum(int(x) for x in parti[1].split("+"))

            if sinistra == destra:
                count_corrette += 1
            count_totali += 1

    if count_totali == 0:
        return 0
    return count_corrette / count_totali


nome_file = "numeri.txt"
frazione = frazione_uguali(nome_file)
print(f"Frazione di uguaglianze corrette: {frazione:.2f} %")
