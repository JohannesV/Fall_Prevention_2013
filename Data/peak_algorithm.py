def find_peaks(time_series, k, h):

	peaks = []
	a = [0]
	
	# Compute peak function
	for i in xrange(1, len(time_series), 1):
		value = s(k, i, time_series)
		a.append(value)

	# Calculate mean and standard deviation for all positive values
	print a
	positives = [v for v in a if v > 0]
	mean = float(sum(positives)) / float(len(positives))
	std = math.sqrt(float(sum([(v-mean)**2 for v in positives])))

	return peaks

def s(k, i, time_series):
	diff1 = float(max([(time_series[i] - time_series[i-j]) for j in xrange(k)]))
	diff2 = float(max([(time_series[i] - time_series[i+j]) for j in xrange(k)]))
	return (diff1+diff2)/2

find_peaks([-1,0,1,2,3,4],2,1.5)
