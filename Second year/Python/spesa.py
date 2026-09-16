prodotti = {"pane": 1.5, "latte": 1.2, "uova": 2.8}
prodotti.update({"acqua": 0.9})
prodotti["pane"] = prodotti["pane"] * 2
for key, value in prodotti.items():
    print(f"{key}: {value}")
