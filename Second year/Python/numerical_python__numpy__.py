import numpy as np

# DIMENSIONI DEGLI ARRAY
array_0d = np.array(14) # array di 0 dimensioni
print("Array in 0D: ", array_0d)

array_1d = np.array([1, 2, 3, 4, 5]) # array di 1 dimensione
print("\nArray in 1D: ", array_1d)
print(array_1d[-2]) # con un indice negativo si parte dall'ultimo elemento

array_2d = np.array([[1, 2, 3],
                     [4, 5, 6]]) # array di 2 dimensioni
print("\nArray in 2D: ")
print(array_2d)

array_3d = np.array([[[1, 2, 3], [4, 5, 6]],
                     [[7, 8, 9], [10, 11, 12]]]) # array di 3 dimensioni
print("\nArray in 3D: ")
print(array_3d)

array_7d = np.array([1, 2, 3], ndmin = 7) # creazione di un array di n dimensioni
print(f"La dimensione dell'array: {array_7d.ndim}") # ndim per la dimensione dell'array
print("Array a 7 dimensioni: ", array_7d)

#######################################################################################
#######################################################################################
#######################################################################################

# ARANGE, ZEROS, ONES
import numpy as np
array_a_range = np.arange(2, 40, 3) # array che va da 2 a 40 (escluso) con salti di 3
print("\nArray con range (2, 40, 3): ", array_a_range)

array_zeros = np.zeros(5) # array con 5 zeri di 1 dimensione
print("\nArray con 5 zeri in 1D: ", array_zeros)

array_zeros_2d = np.zeros((3, 2)) # array con 3 righe e 2 colonne di zeri di 2 dimensioni
print("\nArray con 3 righe e 2 colonne di zeri in 2D: ")
print(array_zeros_2d)

array_zeros_3d = np.zeros((3, 2, 5)) # array di zeri a 3 dimensioni
print("\nArray in 3D (3, 2, 5): ")
print(array_zeros_3d)

array_ones_1d = np.ones(5) # stessa cosa di zeros
print("\nArray in 1D con cinque 1: ", array_ones_1d)

####################################################################
####################################################################
####################################################################

# SLICING
import numpy as np

array = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
print(array[0:3]) # indice 0 incluso a indice 3 escluso (quindi fino a indice 2)
print(array[3:]) # da indice 3 incluso fino alla fine dell'array
print(array[:5]) # dall'inizio fino a indice 5 escluso
print(array[1:-1:2]) # step: da indice 1 alla fine (-1), a salti di 2
print(array[::2]) # si può scrivere anche così

####################################################################
####################################################################
####################################################################

# TIPI DI DATI IN NUMPY
import numpy as np

array = np.array([1, 2, 3, 4, 5])
print(array.dtype) # restituisce il tipo di dati dell'array

array = np.array([1, 2, 3, 4, 5], dtype = 'S') # crea un array di tipo stringa
print(array)
print(array.dtype)

array = array.astype(int) # converte l'array in tipo int
print(array.dtype)

"""
i - integer
b - boolean
u - unsigned integer
f - float
c - complex float
m - timedelta
M - datetime
O - object
S - string
U - unicode string
V - fixed chunk of memory for other type ( void )
"""

####################################################################
####################################################################
####################################################################

# COPY E VIEW
import numpy as np

array = np.array([1, 2, 3, 4, 5])

nuovo_array = array.copy() # copia l'array in uno nuovo, che ha i suoi dati personali

array[0] = 10
print(nuovo_array) # le modifiche sull'array originale non vanno anche sulle copie
print(array)

array = np.array([1, 2, 3, 4, 5])
nuovo_array = array.view() # crea una copia dell'array, che non ha dati personali
array[0] = 10
print(nuovo_array) # ogni modifica cambia sia l'array originale che la copia
print(array)

print(nuovo_array.base) # verifica se l'array ha dati personali o no
"""Se .base restituisce l'array originale, allora l'array non ha dati personali (quindi è view)"""

####################################################################
####################################################################
####################################################################

# ARRAY SHAPE E RESHAPE
import numpy as np

array = np.array([[1, 2, 3, 4], [5, 6, 7, 8]])
print(array.shape) # restituisce una tupla con il numero di elementi di ogni dimensione

array = np.array([1, 2, 3, 4], ndmin=5)
print(f"Array shape: {array.shape}")

array = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])
nuovo_array_2d = array.reshape(4, 3) # modifica il numero di dimensioni e il numero di elementi in ogni dimensione
print(nuovo_array_2d)

nuovo_array_3d = array.reshape(2, 3, 2) # converte l'array in uno in 3 dimensioni
print(nuovo_array_3d)

array = np.array([1, 2, 3, 4, 5, 6, 7, 8])
nuovo_array_sconosciuto = array.reshape(2, 2, -1) # con -1 numpy calcola la dimensione in automatico
print(nuovo_array_sconosciuto)

array = np.array([[1, 2, 3], [4, 5, 6]])
nuovo_array = array.reshape(-1) # converte l'array in uno in 1 dimensione
print(nuovo_array)

####################################################################
####################################################################
####################################################################

# ARRAY ITERATING
import numpy as np

array = np.array([1, 2, 3])

print("Iterazione classica: ")
for x in array: # iterazione classica
    print(x)

array = np.array([[1, 2, 3], [4, 5, 6]])

print("Iterazione in 2D con le righe: ")
for x in array:
    print(x)

print("Iterazione in 2D di ogni elemento: ")
for x in array:
    for y in x:
        print(y)

array = np.array([[[1, 2], [3, 4]], [[5, 6], [7, 8]]])

print("Iterazione in 3D attraverso i sotto array in 2D: ")
for x in array:
    print(x)

print("Iterazione in 3D di ogni elemento: ")
for x in array:
    for y in x:
        for z in y:
            print(z)

print("Iterazione in 3D usando np.nditer(): ")
for x in np.nditer(array): # Iterazione scalare per ogni dimensione, anche molto grande
    print(x)

print("Iterazione usando nditer() ma cambiando datatype: ")
for x in np.nditer(array, flags = ["buffered"], op_dtypes = 'S'): # cambia gli elementi dell'array in tipo Stringa
    print(x)
"""["buffered"] si usa perché potrebbe servire spazio extra in memoria"""

print("Iterazione di ogni elemento dell'array facendo salti di 2: ")
array = np.array([[1, 2, 3], [4, 5, 6]])
for x in np.nditer(array[:, ::2]):
    print(x)

####################################################################
####################################################################
####################################################################

# ARRAY JOINING
import numpy as np

array1 = np.array([1, 2, 3])
array2 = np.array([4, 5, 6])
array = np.concatenate((array1, array2)) # unisce due array
print(array)

array1 = np.array([[1, 2], [3, 4]])
array2 = np.array([[5, 6], [7, 8]])
array = np.concatenate((array1, array2), axis = 1) # unisce due array in righe
print(array)

array1 = np.array([1, 2, 3])
array2 = np.array([4, 5, 6])
array = np.stack((array1, array2), axis = 1) # unisce con lo stack (nuove axis)
print(array)

array = np.hstack((array1, array2)) # stack in righe
print(array)

array = np.vstack((array1, array2)) # stack in colonne (il secondo sotto il primo)
print(array)

array = np.dstack((array1, array2)) # stack in altezza (il secondo affianco al primo)
print(array)

####################################################################
####################################################################
####################################################################

# ARRAY SPLITTING
import numpy as np

array = np.array([1, 2, 3, 4, 5, 6])
nuovo_array = np.array_split(array, 3) # divide l'array in 3 array (contenuti in una lista)
print(nuovo_array)
print(nuovo_array[1]) # accede al secondo array creato
print(nuovo_array[1][1]) # accede al secondo elemento del secondo array creato

""" hsplit = hstack
    dsplit = dstack
    vsplit = vstack """

####################################################################
####################################################################
####################################################################

# SEARCHING ARRAY
import numpy as np

array = np.array([1, 2, 3, 4, 4, 4, 5])
x = np.where(array == 4) # restituisce gli indici in cui il valore è 4
print(x)

array = np.array([1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
x = np.where(array % 2 == 0) # restituisce gli indici in cui il valore è pari
print(x)

####################################################################
####################################################################
####################################################################

# SORTING ARRAYS
import numpy as np

array = np.array([1, 2, 5, 3, 6, 4])
print(np.sort(array)) # ordina l'array (restituisce una copia)

####################################################################
####################################################################
####################################################################

# FILTERING ARRAY
import numpy as np

array = np.array([1, 2, 3, 4, 5])
x = [True, False, True, True, False]
nuovo_array = array[x] # il nuovo array prende gli elementi il cui indice ha il valore True
print(nuovo_array)

filter_array = [] # lista vuota

for elemento in array:
    if elemento % 2 == 0:
        filter_array.append(True)
    else:
        filter_array.append(False)

nuovo_array = array[filter_array]
print(filter_array)
print(nuovo_array)

filter_array = array % 2 # altro metodo molto più facile
print(filter_array)

####################################################################
####################################################################
####################################################################

# NUMPY RANDOM
from numpy import random
x = random.randint(100) # numero casuale tra 0 e 100
print("Numero casuale:", x)

f = random.rand() # float tra 0 e 1
print("Float casuale tra 0 e 1:", f)

array_float = random.rand(5) # array di tipo float con 5 numeri da 0 a 1
print("Array di tipo float:", array_float)

array = random.randint(100, size = 5) # crea un array con 5 numeri casuali tra 0 e 100
print("Array casuale:", array)

array_2d = random.randint(100, size = (3, 2)) # crea un array 2d con 3 righe e 2 colonne, tutto casuale
print("Array casuale in 2D:", array_2d)

x = random.choice([1, 2, 3, 4, 5]) # prende un numero casuale da un array
print("Numero casuale estratto da un array:", x)

####################################################################
####################################################################
####################################################################

# RANDOM DATA DISTRIBUTION
from numpy import random

array = random.choice([1, 2, 3, 4, 5], p = ([0.1, 0.1, 0.2, 0.3, 0.3]), size = (100))
print(array)
"""Crea un array di 100 elementi seguendo una distribuzione: ogni numero dell'array ha una certa probabilità di uscita
   p = 0 --> non uscirà mai
   p = 1 --> uscirà sempre
   la somma di tutte le p deve essere 1"""

####################################################################
####################################################################
####################################################################

# RANDOM PERMUTATIONS
from numpy import random
import numpy as np

array = np.array([1, 2, 3, 4, 5])
random.shuffle(array) # mischia l'ordine degli elementi dell'array (non crea una copia)
print(array)

array = np.array([1, 2, 3, 4, 5])
nuovo_array = random.permutation(array) # mischia l'ordine dell'array senza cambiare l'array originale
print(array)
print(nuovo_array)

####################################################################
####################################################################
####################################################################

# NUMPY UFUNCS --> Universal Functions
import numpy as np

x = [1, 2, 3, 4]
y = [4, 5, 6, 7]
z = np.add(x, y) # addiziona ogni elemento degli array
print(z)
"""Fa la stessa cosa con sottrazione e moltiplicazione"""

def myadd(x, y):
    return x + y

myadd = np.frompyfunc(myadd, 2, 1) # crea una ufunction
print(myadd([1, 2, 3, 4], [5, 6, 7, 8]))
