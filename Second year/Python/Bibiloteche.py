with open("biblioteche.csv", "r") as file:
    for linea in file:
        linea = linea.strip()
        colonna = linea.split(",")
