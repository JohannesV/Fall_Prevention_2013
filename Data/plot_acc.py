import matplotlib.pyplot as plt

a_z_data = []
a_x_data = []
a_y_data = []

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

plt.plot(a_z_data, 'g-')
plt.plot(a_x_data, 'r-')
plt.plot(a_y_data, 'b-')
plt.title('Accelerometer')
plt.show()
