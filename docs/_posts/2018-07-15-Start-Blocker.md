---
layout: post
title:  "关联启动限制"
summary: "关于关联启动限制功能的一切"
date:   2018-07-15 15:58:00
categories: jekyll
---
<!-- more -->

## 用处
**关联启动**这个词参考了华为EMUI的手机管家，Android平台上，除了用户主动通过启动器启动之外，启动应用的进程的方式有多种，包括不限于：

* 通过注册的**广播接收器**（Broadcast receiver，Android平台和应用都可能会利用广播传播一些事件）
* 通过注册的**内容提供者**（Content provider）
* 通过**AIDL**（Android IPC）
* 通过启动**服务**（Service）

了解了以上启动方式，如果一个应用A，想要启动B（也可以理解为B想通过一些手段让自己的进程启动），他可以通过上述手段达到目的。

**用户看到的现象往往是，我打开了A应用，B/C/D应用也在后台启动了？**

## 如何使用
将需要限制的应用加入**关联启动限制**列表中即可，加入列表的应用，会被禁止任何其他应用的关联启动。

如下图，就禁止了`百度地图`的关联启动。
![xposed_installer_main](/X-APM/assets/post-start-blocker/Start-Blocker-Sample.png)

## 如何查看启动记录
查看启动记录，可以了解有哪些应用一直想要被启动。
1. 关闭节电模式（更多-策略与优化-节电模式）
2. 打开关联启动限制页，点击上方菜单，查看详细记录。

如下图，是启动记录的概览：
![xposed_installer_main](/X-APM/assets/post-start-blocker/Start-Record-Overview.png)

点击一条记录，可以查看该应用的详细启动记录：
![xposed_installer_main](/X-APM/assets/post-start-blocker/Start-Record-Detail.png)

上图显示了`百度文库`，通过注册的广播，尝试启动了多次，均被X-APM拦截。