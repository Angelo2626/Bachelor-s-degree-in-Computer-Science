import numpy as np
import pandas as pd
from matplotlib import pyplot as plt

ds = np.random.randint(0, 100, (200, 4))
df = pd.DataFrame(ds)

print(df)

sommacumulativa = df.cumsum()
sommacumulativa.hist(bins=20, figsize=(10, 6))
plt.suptitle("Somma cumulativa del DataFrame")
plt.show()
