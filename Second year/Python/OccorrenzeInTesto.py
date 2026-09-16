def contaparole(content):
    dizionario = {}
    with open("conteggio.txt", "a") as conteggio:
        for parola in content.split():
            if parola.isalpha() == True and parola.lower() not in dizionario:
                dizionario.update({parola.lower(): content.count(parola)})
                conteggio.write(f"{parola.lower()}: {content.count(parola)}\n")

with open("testo.txt", "r") as file:
    content = file.read()
    contaparole(content)
