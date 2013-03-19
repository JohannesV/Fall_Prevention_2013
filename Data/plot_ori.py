import matplotlib.pyplot as plt

o_z_data = []
o_x_data = []
o_y_data = []

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
	
plt.plot(o_z_data, 'g-')
plt.plot(o_x_data, 'r-')
plt.plot(o_y_data, 'b-')
plt.title('Orientation')
plt.show()
