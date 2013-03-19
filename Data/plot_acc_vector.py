import matplotlib.pyplot as plt
import math
import peak_algorithm as pa

a_z_data = []
a_x_data = []
a_y_data = []

with open('a_z.txt') as file:
	for line in file:
		line = line.strip()
		a_z_data.append(float(line))
with open('a_x.txt') as file:
	for line in file:
		line = line.strip()
		a_x_data.append(float(line))
with open('a_y.txt') as file:
	for line in file:
		line = line.strip()
		a_y_data.append(float(line))

vector = []
for i in xrange(len(a_z_data)):
	length = math.sqrt(a_x_data[i]**2 + a_z_data[i]**2 + a_y_data[i]**2)
	vector.append(length)

peaks = pa.find_peaks(time_series=vector)

for peak in peaks:
	plt.vlines(peak, min(vector), max(vector), color='r')

plt.plot(vector)
plt.title('Accelerometer Vector Length')
plt.show()
