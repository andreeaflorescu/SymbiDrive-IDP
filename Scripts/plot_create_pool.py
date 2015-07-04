'''
Created on Jun 27, 2015

@author: andreea
'''
#!/usr/bin/python

import numpy as np
import matplotlib.pyplot as plt

with open("create_pool.txt") as f:
    data = f.read()

data = data.split('\n')
print data
del data[-1]
x = [row.split(' ')[0] for row in data]
y = [row.split(' ')[1] for row in data]

fig = plt.figure()
# ax = plt.gca()
# ax.set_autoscale_on(False)
plt.axis([1.0, 2000.0, 0.07, 0.2])
ax1 = fig.add_subplot(111)

ax1.set_title("Create Pool Requests")    
ax1.set_xlabel('Number of Pools')
ax1.set_ylabel('Time (ms)')
ax1.plot(x,y, c='r', label='create_pool')

leg = ax1.legend()

plt.show()