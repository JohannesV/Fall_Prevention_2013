import matplotlib.pyplot as plt
import sys

data = []

filename = sys.argv[-1]

with open(filename) as file:
	for line in file:
		line = line.strip()
		data.append(line)
	
plt.plot(data)
plt.title('Dataplot of ' + filename)
plt.show()
