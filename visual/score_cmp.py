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

ori_scores = []
refact_scores = []
for item in data:
    a,s,d,f,g,h,j,k,l,z,x = item
    record = Record(a,s,d,f,g,h,j,k,l,z,x)
    records.append(Record(a,s,d,f,g,h,j,k,l,z,x))
    ori_scores.append(record.ori_score)
    refact_scores.append(record.refact_score)
    # break
import numpy as np


print(ori_scores)

plt.title('compare between ori and refact')
plt.xticks([])
plt.plot(ori_scores,'b--',label='ori_score')

plt.plot(refact_scores,'g',label = 'refact_score')
plt.legend(loc='best', frameon=True)
plt.show()