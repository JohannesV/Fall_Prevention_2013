import matplotlib.pyplot as plt
import math

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

peaks = []

GRAVITY = 9.81
ERROR_MARGIN = 3
THRESHOLD = GRAVITY + ERROR_MARGIN

peak_start = -1
peak_end = -1

for i, v in enumerate(vector):
	if v > THRESHOLD:
		# This might be the start of a new peak
		if peak_start < 0:
			peak_start = i
		# Or is the continuation of a current peak
		else:
			pass
	else:
		# This might mean the end of a peak
		if peak_start > 0:
			peak_end = i
			peaks.append( (peak_start, peak_end) )
			peak_start = -1
		# Or it might be the contiunation of a low
		else:
			pass

for peak in peaks:
	y = [THRESHOLD, THRESHOLD]
	x = [peak[0]-0.5, peak[1]-0.5]
	plt.plot(x,y,'r-')
#	plt.axhline(y=THRESHOLD, xmin=peak[0], xmax=peak[1], color='r')

plt.plot(vector)
plt.title('Accelerometer Vector Length')
plt.show()
