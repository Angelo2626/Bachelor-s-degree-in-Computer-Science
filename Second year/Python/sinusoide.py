import numpy as np
import matplotlib.pyplot as plt
t = np.linspace(0, 2, 1000)  # 1000 punti tra 0 e 2 secondi
f1 = 2   # 2 Hz
f2 = 5   # 5 Hz
f3 = 10  # 10 Hz

# Segnali sinusoidali
s1 = np.sin(2 * np.pi * f1 * t)
s2 = np.sin(2 * np.pi * f2 * t)
s3 = np.sin(2 * np.pi * f3 * t)
plt.figure(figsize=(10,6))

plt.plot(t, s1, label="2 Hz")
plt.plot(t, s2, label="5 Hz")
plt.plot(t, s3, label="10 Hz")

plt.xlabel("Tempo (s)")
plt.ylabel("Ampiezza")
plt.title("Segnali sinusoidali con frequenze diverse")
plt.legend()
plt.grid(True)
plt.show()

segnale1 = np.fft.fft(s1)
segnale2 = np.fft.fft(s2)
segnale3 = np.fft.fft(s3)

plt.plot(segnale1, segnale2, segnale3)
plt.show()
