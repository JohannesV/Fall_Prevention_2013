import math, numpy, time

def find_peaks(time_series=[], window=10, std_threshold=0.8):

	peak_function_values = [0]
	peaks = []
	
	# Compute peak function
	for i in xrange(1,len(time_series)-1,1):
		value = s2(window, i, time_series)
		peak_function_values.append(value)
	# Add dummy value to peak_function_values for the last element
	peak_function_values.append(0)

	# Calculate mean and standard deviation for all positive values
	positives = [v for v in peak_function_values if v > 0]
 	mean = numpy.mean(positives)
	std = numpy.std(positives)

#	return peak_function_values, mean+(std*std_threshold)
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

# Dummy-function.
def s0(window, i, time_series):
	return time_series[i]

# Max-calculation
def s1(window, i, time_series):
	before = get_slice(time_series, i-window, i)
	diff1 = [time_series[i]-b for b in before]
	maxdiff1 = max(diff1)
	
	after = get_slice(time_series, i+1, i+window+1)
	diff2 = [time_series[i]-a for a in after]
	maxdiff2 = max(diff2)

	return float(maxdiff1+maxdiff2)/2.0

# Avg-calculation
def s2(window, i, time_series):
	before = get_slice(time_series, i-window, i)
	diff1 = [time_series[i]-b for b in before]
	sumdiff1 = sum(diff1)
	kdiff1 = float(sumdiff1)/float(window)
	
	after = get_slice(time_series, i+1, i+window+1)
	diff2 = [time_series[i]-a for a in after]
	sumdiff2 = sum(diff2)
	kdiff2 = float(sumdiff2)/float(window)

	return float(kdiff1+kdiff2)/2.0

# MaxEnt-calculation of 
def s3(window, i, 
time_series):
	N = get_slice(time_series, i-window, i)+get_slice(time_series, i+1, i+window+1)
	Nbar = get_slice(time_series, i-window, i+window+1)
	return 0.0 - (entropy(N) - entropy(Nbar))

KERNEL_WIDTH = 5

def entropy(list):
	return sum([((0.0-prw(i,list))*math.log(prw(i,list))) for i in xrange(len(list)-KERNEL_WIDTH)])

# The Parzen-Rosenblatt window estimate of probability density
def prw(i, peak_func):
	if peak_func[i] - peak_func[i+KERNEL_WIDTH] != 0:
		prefix = (1.0 / len(peak_func)*math.fabs(peak_func[i] - peak_func[i+KERNEL_WIDTH]))
		suffix = sum([gauss_kernel((peak_func[i]-peak_func[j])/math.fabs(peak_func[i]-peak_func[i+KERNEL_WIDTH])) for j in xrange(len(peak_func))])
		return prefix*suffix
	else:
		return 1.0

def gauss_kernel(x):
	return (1.0/math.sqrt(2.0*math.pi)) * math.exp((-1.0/2.0)*(x**2))

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

def smooth_moving(series, window):
	smoothed_series = []
	for i in xrange(len(series)):
		window_series = get_slice(series, i-window, i+1+window)
		smoothed = numpy.mean(window_series)
		smoothed_series.append(smoothed)
	return smoothed_series
