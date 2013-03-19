import matplotlib.pyplot as plt

o_z_data = []
o_x_data = []
o_y_data = []
a_z_data = []
a_x_data = []
a_y_data = []

with open('o_z.txt') as file:
	for line in file:
		line = line.strip()
		o_z_data.append(line)
with open('o_x.txt') as file:
	for line in file:
		line = line.strip()
		o_x_data.append(line)
with open('o_y.txt') as file:
	for line in file:
		line = line.strip()
		o_y_data.append(line)
with open('a_z.txt') as file:
	for line in file:
		line = line.strip()
		a_z_data.append(line)
with open('a_x.txt') as file:
	for line in file:
		line = line.strip()
		a_x_data.append(line)
with open('a_y.txt') as file:
	for line in file:
		line = line.strip()
		a_y_data.append(line)
			
plt.plot(o_z_data, 'm-')
plt.plot(o_x_data, 'c-')
plt.plot(o_y_data, 'y-')
plt.plot(a_z_data, 'g-')
plt.plot(a_x_data, 'r-')
plt.plot(a_y_data, 'b-')
plt.title('Orientation and Accelerometer')
plt.show()
