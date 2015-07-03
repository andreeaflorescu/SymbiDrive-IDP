'''
Created on Jun 28, 2015

@author: andreea
'''

import numpy as np
import matplotlib.pyplot as plt

with open("find_pool.txt") as f:
    data = f.read()

data = data.split('\n')
print data
del data[-1]
x_val = [row.split(' ')[0] for row in data]
y_val = [row.split(' ')[1] for row in data]

x = x_val[1::2]
y = y_val[1::2]
fig = plt.figure()
# ax = plt.gca()
# ax.set_autoscale_on(False)
plt.axis([1.0, 2000.0, 0.07, 0.16])
ax1 = fig.add_subplot(111)

ax1.set_title("Join Pool Requests")    
ax1.set_xlabel('Number of Pools')
ax1.set_ylabel('Time (ms)')
ax1.plot(x,y, c='r', label='Area Distributed GPS Points')

x = x_val[0::2]
y = y_val[0::2]
ax1.plot(x,y, c='b', label='Random GPS Points')


leg = ax1.legend()

plt.show()