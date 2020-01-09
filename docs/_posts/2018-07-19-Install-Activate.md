---
layout: post
title:  "安装与激活"
summary: "介绍了如何正确安装与激活X-APM模块以及无法激活的问题解决方案"
date:   2018-07-19 15:58:00
categories: jekyll
---
<!-- more -->

## 安装
1. **X-APM**是一个独立的apk，只需要下载apk安装到设备上即可。
2. 目前仅支持**Android 5.0及其以上**版本的ROM。
3. 开发基于**AOSP**开发，理论上兼容所有类原生以及其他大部分国产ROM。

## 激活
1. **X-APM**依赖与**Xposed框架**，因此你的设备必须已经正确安装了**Xposed框架**（包括System-less版本）。
2. 安装**X-APM** apk后，进入**Xposed installer**应用，依次进行如下操作，勾选**X-APM**模块：
![xposed_installer_main](/X-APM/assets/post-install-activate/xposed-installer-main-reboot.png)
3. 勾选完成，重启设备。


## 更新
由于**APP**层的逻辑是在Android初始化的阶段进行初始化，因此每次更新后，建议重新启动设备，因此安装新版本后，请按照**Xposed installer**的提示进行重启。


## 问题排查

### 无法激活？
**X-APM**激活与否的判断条件，与其工作原理息息相关。
如果无法激活，请尝试重新进入**Xposed installer**进行勾选。

### 更新后重启显示未激活？
当模块更新完成后，**Xposed installer**会通过广播接收的方式监听到应用路径的变化，如果你通过一些手段，禁止了**Xposed installer**的广播监听，则会出现该问题。
因此最好不要通过任何手段禁止**Xposed installer**的广播接收，或者每次更新后，重新进入**Xposed installer**进行勾选，重启。

### 尝试上述手段依然未激活
请截取如下日志，前往github新建issue，并把日志附上：
1. 打开**Xposed installer**，点击**日志**（log）菜单，保存日志文件。
2. 点击这里[新建issue](https://github.com/Tornaco/X-APM/issues/new/choose)