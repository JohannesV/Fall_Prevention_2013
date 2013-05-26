import matplotlib.pyplot as plt
import math
import peak_algorithm as pa

z_data = []
x_data = []
y_data = []

with open('a_z.txt') as file:
	for line in file:
		line = line.strip()
		z_data.append(float(line))
with open('a_x.txt') as file:
	for line in file:
		line = line.strip()
		x_data.append(float(line))
with open('a_y.txt') as file:
	for line in file:
		line = line.strip()
		y_data.append(float(line))

vector = []
for i in xrange(len(z_data)):
	length = math.sqrt(x_data[i]**2 + z_data[i]**2 + y_data[i]**2)
	vector.append(length)

#plt.plot(vector)
#s_vector_1 = pa.smooth_moving(vector, 1)
#plt.plot(s_vector_1)
#s_vector_2 = pa.smooth_moving(vector, 2)
#plt.plot(s_vector_2)
s_vector_3 = pa.smooth_moving(vector, 3)
plt.plot(s_vector_3)
#s_vector_4 = pa.smooth_moving(vector, 4)
#plt.plot(s_vector_4)
#s_vector_5 = pa.smooth_moving(vector, 5)
#plt.plot(s_vector_5)

#peaks = pa.find_peaks(time_series=s_vector_5)
#plt.plot(peaks)

#plt.hlines(th, 0, 4500)

# Print peaks
#for peak in peaks:
#	plt.vlines(peak, min(vector), max(vector), color='r')

plt.title('Accelerometer Vector Length')
plt.show()
