import datetime, time, random

NEXT_STEP_PROB = 0.9
GROUP_TIME_GAP = 480 * 1000
STEP_TIME_GAP = 8 * 1000

step_list = []

# Find the beginning and end time stamps

dt1 = datetime.datetime(2013, 4, 30, 7, 0)
dt2 = datetime.datetime(2013, 5, 1, 21, 0)

start = time.mktime(dt1.timetuple()) * 1000
end = time.mktime(dt2.timetuple()) * 1000

# Start generating data
current = start
group = False

while current < end:
	# See if next step will be the start of new group, or continuation of old one
	if group:
		step_list.append(current)
		# See if this is the end of a group
		if random.random() > NEXT_STEP_PROB:
			current += random.random() * STEP_TIME_GAP
		# The group ends here
		else:
			group = False
			current += random.gauss(0.5, 0.2) * GROUP_TIME_GAP
	# We start a group
	else:
		group = True
		step_list.append(current)
		current += random.random() * STEP_TIME_GAP

with open("data.txt", 'w') as file:
	for s in step_list:
		file.write(str(s)+"\n")
