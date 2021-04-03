# -*- ecoding: utf-8 -*-
# @Author: HuangYiHong
# @Time: 2021-03-31 9:54

import pandas as pd
import collections
import matplotlib.pyplot as plt
data = pd.read_csv('./data.csv').values
attrs = ['id','project_name','class_cnt','method_cnt','member_cnt','invoke_cnt']
attrs2 = ['ori_score','refact_score','stability','exec_time','disturbance_rate']
attrs.extend(attrs2)
Record = collections.namedtuple('Record',attrs)
records = []

info = dict()
for item in data:
    a,s,d,f,g,h,j,k,l,z,x = item
    record = Record(a,s,d,f,g,h,j,k,l,z,x)
    records.append(Record(a,s,d,f,g,h,j,k,l,z,x))
    projectName = record.project_name
    if info.get(projectName) is None:
        info[projectName] = []
    if record.disturbance_rate > 0.05:
        info[record.project_name].append(record.stability)
    # break
import numpy as np
x = [0.1,0.2,0.3,0.4]

plt.title('stability in different project')
plt.xlabel('disturbance_rate')
plt.ylabel('stability',rotation=0,labelpad=10)

for k,v in info.items():
    plt.plot(x,v,label = k)
plt.legend(loc='best')
plt.show()