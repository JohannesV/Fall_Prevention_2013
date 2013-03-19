vector = []
peaks = []

GRAVITY = 9.81
ERROR_MARGIN = 3
THRESHOLD = GRAVITY + ERROR_MARGIN

with open('vector.txt') as file:
	for line in file:
		line = line.strip()
		vector.append(float(line))

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

print len(peaks)
