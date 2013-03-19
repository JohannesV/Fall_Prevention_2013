import math, numpy

def find_peaks(time_series=[], window=4, std_threshold=1.5):

	peak_function_values = [0]
	peaks = []
	
	# Compute peak function
	for i in xrange(1,len(time_series)-1,1):
		value = s(window, i, time_series)
		peak_function_values.append(value)
	# Add dummy value to peak_function_values for the last element
	peak_function_values.append(0)

	# Calculate mean and standard deviation for all positive values
	positives = [v for v in peak_function_values if v > 0]
 	mean = numpy.mean(positives)
	std = numpy.std(positives)

	# Remove statistically insignificant peaks
	for i in xrange(len(peak_function_values)):
		v = peak_function_values[i]
		if v > 0 and (v-mean)>(std_threshold*std):
			peaks.append(i)

	# Remove close peaks
	c = 0
	while c < len(peaks)-1:
		# The peaks are too close, remove the lesser
		if (peaks[c+1] - peaks[c]) <= window:
			if peak_function_values[c+1] > peak_function_values[c]:
				peaks.pop(c)
			else:
				peaks.pop(c+1)
		# The peaks are distant, move counter one step
		else:
			c += 1
	
	return peaks

def s(window, i, time_series):
	before = get_slice(time_series, i-window, i)
	diff1 = [time_series[i]-b for b in before]
	maxdiff1 = max(diff1)
	
	after = get_slice(time_series, i+1, i+window+1)
	diff2 = [time_series[i]-a for a in after]
	maxdiff2 = max(diff2)

	return float(maxdiff1+maxdiff2)/2.0

def get_slice(list, start, end):
	# Slice is entirely contained in list
	if start >= 0 and end < len(list):
		return list[start:end]
	# Start of slice is contained, but not end
	elif (start >= 0) and (not end < len(list)):
		return list[start:]
	# End of slice is contained, but not start
	elif (not start >= 0) and (end < len(list)):
		return list[:end]
	# Slice is larger than list in both directions
	else:
		return list[:]
