import matplotlib.pyplot as plt

steps = []

with open("data.txt", 'r') as file:
	for line in file:
		line = line.strip()
		steps.append(float(line))

for s in steps:
	plt.axvline(x=s, ymin=0, ymax=1)

plt.show()
