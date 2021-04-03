Java 类和方法的重构，使软件的高内聚低耦合程度进一步提高。

此为作者的**本科生毕业论文**

采用二分网络进行重构。

## 指标

$$
w(c_i,m_j)=| \quad   getMehtods(c_i) \cap R_1(m_j)  \quad |
$$

$$
q_{ij}= m^{-1}\sum_jw_{ij}\sum_iw_{ij} 
$$

$$
Q=\frac{1}{m} \sum_{i}\sum_{j}(w_{ij}-q_{ij})c(i,j)   ...........(3)
$$




## 伪代码描述
**Algorithm**:  REFACT  算法
-------------------
**input**: 
* owns 二维数组  
* use  二维数组

说明： 
```
owns[class][method] 代表 id 为 method 的方法， 所在的类的id是否为 class
```
```
use[method][class] 代表 id为 method 的方法，在函数体中使用id为class 的类的方法或成员的次数
```
**output**: 重构队列，每一个元素包含<待重构的方法>和<目标类>

```
for 方法 in 所有方法
    method_id = 方法的id
    class_id = 方法的类的id
    haveRelationParent = false
    do{
        if use[method_id][class_id] != 0:
            haveRelationParent = true
            break
        class_id = 当前类的父类的id
    }while(class_id有意义)

    if not haveRelationParent :
        //方法与自身所在的类以及祖父类没有任何联系
        //判断方法是否与某个其他类有紧密联系
        sum_use=0
        max_use=0
        for 类 in 所有的类:
            class_id = 类的id
            this_use = use[method_id][class_id]
            sum_use += this_use
            max_use = max( max_use, this_use )
        if max_use/sum_use > 亲密系数 :
            //亲密系数暂定为 0.9
            在假候选队列中加入 （方法，紧密的类）
    
for （方法，紧密的类） in 假候选队列:
    //以下条件均为<布尔类型>
    //使用 obj 指代 <方法使用的紧密的类的实例对象>
    条件a = 方法仅仅使用了紧密的类的静态方法或静态成员
    条件b = obj来自于方法的参数
    条件c = obj 是方法的临时局部变量
    条件d = obj 是方法所在类的成员变量
    条件e = 方法是所在类对父类方法的覆盖 或者 有子类有本方法进行了重写
    条件f = obj被本方法进行了返回
	
	重构 = false
    if (e) :
        重构 = false
    else if ( !e  && a ):
        重构 = true
    else if ( !e && !a &&b ):
        重构 = true
    else if ( !e && !a && c && f ):
        重构 = false
    else if ( !e && !a && c && !f ):
        重构 = true
    else if ( !e && !a && d ):
        重构 = false
    if 重构== true：
        将（方法，紧密的类）加入重构队列
return 重构队列

```

