---
layout: post
title:  "后台启动管理（Start blocker）"
summary: "关于后台启动管理功能的一切"
date:   2018-07-15 15:58:00
categories: jekyll
---
<!-- more -->

**关联启动**这个词参考了华为EMUI的手机管家（Thanox名字为后台启动，包含了开机广播启动和关联启动），Android平台上，除了用户主动通过启动器启动之外，启动应用的进程的方式有多种，包括不限于：

* 通过注册的**广播接收器**（Broadcast receiver，Android平台和应用都可能会利用广播传播一些事件）
* 通过注册的**内容提供者**（Content provider）
* 通过**AIDL**（Android IPC）
* 通过启动**服务**（Service）

了解了以上启动方式，如果一个应用A，想要启动B（也可以理解为B想通过一些手段让自己的进程启动），他可以通过上述手段达到目的。

**用户看到的现象往往是，我打开了A应用，B/C/D应用也在后台启动了？**