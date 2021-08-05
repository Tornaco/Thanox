---
layout: post
title: 安装与激活
---



## 0.1. 安装

1. Thanox是一个独立的apk，只需要下载apk安装到设备上即可。
2. 目前仅支持**Android 6.0及其以上**版本的ROM。
3. 开发基于**AOSP**开发，理论上兼容所有类原生以及其他大部分国产ROM。



## 0.2. 激活

Thanox中大部分的功能都是在系统进程中完成的，因此Thanox会对Android系统打补丁。

目前支持Xposed模式（只需要安装apk即可）与Magisk模式（需要安装apk，同时安装magisk补丁）。

### 0.2.1 Xposed模式

1. 你的设备必须已经正确安装了**Xposed框架**（或者其他类似支持Xposed API框架）。
2. 安装Thanox apk后，进入**Xposed installer**应用，勾选Thanox。
3. 勾选完成，重启设备。

### 0.2.2 Magisk补丁模式

1. 你的设备必须已经正确安装了**Magisk框架**。
2. 你的设备必须已经正确安装了**riru**框架。
3. 下载安装thanox的magisk模块（riru-thanox-xxx.zip），通常在[github release](https://github.com/Tornaco/Thanox/releases)发布。



## 0.3. 更新

1. 安装新版本后，请按照**Thanox**或**Xposed installer**的提示进行重启。
2. 基于Xposed模式时，如果未收到**Xposed installer**的更新提示，请进入**Xposed installer**手动重新勾选后重新启动设备。



## 0.4. 问题排查

### 0.4.1. 无法激活？
Thanox激活与否的判断条件，与其工作原理息息相关。
如果无法激活，请尝试重新进入**Xposed installer**进行勾选。

### 0.4.2. 更新后重启显示未激活？
当模块更新完成后，**Xposed installer**会通过广播接收的方式监听到应用路径的变化，如果你通过一些手段，禁止了**Xposed installer**的广播监听，则会出现该问题。因此最好不要通过任何手段禁止**Xposed installer**的广播接收，或者每次更新后，重新进入**Xposed installer**进行勾选，重启。



### 0.4.3. 尝试上述手段依然未激活

请截取如下日志，前往github新建issue，并把日志附上：
1. 打开**Xposed installer**，点击**日志**（log）菜单，保存日志文件。
2. 点击这里 [新建issue](https://github.com/Tornaco/Thanox/issues/new/choose)

