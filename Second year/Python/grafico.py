import numpy as np
import pandas as pd
from matplotlib import pyplot as plt

ds = {
    "A" : np.random.rand(100),
    "B" : np.random.rand(100),
    "C" : np.random.rand(100),
    "D" : np.random.rand(100)
}

df = pd.DataFrame(ds)
cumulativa = df.cumsum()
cumulativa.plot()
plt.show()
