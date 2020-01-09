---
layout: post
title:  "关联启动规则"
summary: "面向喜欢折腾的用户"
date:   2018-07-14 15:58:00
categories: jekyll
---
<!-- more -->

## 前言
注意，正常情况下，加入关联启动限制 列表中的应用会被禁止任何应用偷偷后台启动），可以满足正常使用需求。规则设置只是提供给**非新手**使用，用来做更精细的控制。**新手无需尝试**！


## 语法

规则语法为三个角色，分别是**行动**，行动包括```ALLOW（允许）```、```DENY（拒绝）```，角色，包括**启动者**和**目标**，两个应用的包名。
也可以用`THIS`表示某个应用自己。

举例说明：

如果想允许微信启动QQ，可以添加规则：```ALLOW com.tencent.mm com.tencent.qq```；  
如果想允许Android系统启动所有应用，可以添加规则：```ALLOW android *```；  
如果想要拒绝任何应用启动微信：```DENY * com.tencent.mm```。  

支持以进程代号代替角色，目前可用代号包括：`SHELL`，`ROOT`。    
例如，如果想允许SHELL启动任何QQ：```ALLOW SHELL com.tencent.qq```

### 针对MIUI，FLYME等ROM
据用户反馈，MIUI用户需要添加如下规则：  
```ALLOW THIS THIS```  
解决状态栏消息点击无法跳转问题。