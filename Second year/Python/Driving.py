import pandas as pd
df = pd.read_csv('driving.csv')

media_tempi_frenata = df["brake_rt"].mean()
media_risposta_stimoli = df["sec_rt"].mean()
mancate_frenate = df["brake_rt"].isna().sum()
print(f"La media dei tempi di frenata è {media_tempi_frenata}, mentre la media della risposta agli stimoli è {media_risposta_stimoli}. Il numero delle mancate frenate è {mancate_frenate}")

