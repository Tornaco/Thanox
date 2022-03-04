---
layout: default
title:  "后台启动规则"
summary: "面向喜欢折腾的用户"
date:   2018-07-14 15:58:00
nav_order: 6
---
<!-- more -->

## 功能介绍
Thanox的后台启动功能可以有效的拦截app在后台被偷偷启动的问题，通常一个app如果被禁止了后台启动，那么这个app无论何时都是无法在后台启动的。



后台启动规则可以更精确的控制app的后台启动行为，例如；通过规则，可以只允许微信在后台被QQ启动而禁止被其他app启动。


## 规则语法
规则语法为两个部分：

* **行动**；行动包括两种，分别是：```ALLOW（允许）```、```DENY（拒绝）```

* 角色；包括**启动者app**和**目标app**，分别为对用两个app的包名，也可以用`THIS`表示某个应用自己。



举例说明：

如果想允许微信启动QQ，可以添加规则：```ALLOW com.tencent.mm com.tencent.qq```；  
如果想允许Android系统启动所有应用，可以添加规则：```ALLOW android *```；  
如果想要拒绝任何应用启动微信：```DENY * com.tencent.mm```。  



便利：

支持一些常用的名称代替角色，目前可用代号包括：`SHELL`，`ROOT`。    
例如，如果想允许Shell启动任何QQ：```ALLOW SHELL com.tencent.qq```