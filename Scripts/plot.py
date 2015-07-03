'''
Created on Jun 27, 2015

@author: andreea
'''
#!/usr/bin/python

import numpy as np
import matplotlib.pyplot as plt

with open("register_user.txt") as f:
    data = f.read()

data = data.split('\n')
print data
del data[-1]
x = [row.split(' ')[0] for row in data]
y = [row.split(' ')[1] for row in data]

fig = plt.figure()
# ax = plt.gca()
# ax.set_autoscale_on(False)
plt.axis([1.0, 1000.0, 0.07, 0.2])
ax1 = fig.add_subplot(111)

ax1.set_title("Register User Requests")    
ax1.set_xlabel('Number of Registered Users')
ax1.set_ylabel('Time (ms)')
ax1.plot(x,y, c='r', label='register_user')

leg = ax1.legend()

plt.show()