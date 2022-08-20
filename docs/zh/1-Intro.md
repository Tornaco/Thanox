---
layout: default
title:  "应用介绍"
permalink: /docs/intro
summary: "Thanox工作原理"
date:   2018-07-20 15:50:00
lang: zh
nav_order: 2
---

<!-- more -->



## 开源计划

目前Thanox代码约80%已经公开在[Github](https://github.com/Tornaco/Thanox/tree/master/android)上。

&nbsp;

## 版本发布

Thanox目前在酷安和Google play商店发布。

* [酷安](https://www.coolapk.com/) 发布的版本为国内稳定版。
* [Github-Release](https://github.com/Tornaco/Thanox/releases) 国内稳定版，如果要体验最新测试版，可以进入[CI](国内稳定版)下载。
* [Google play](https://play.google.com/store/apps/details?id=github.tornaco.android.thanos.pro&hl=en&gl=US) 发布的版本为pro版本，功能与国内版本基本一致；得益于Google play的便利性，你可以选择加入Beta、Alpha计划体验pro的测试版本



## Android版本支持

* Android 6.0 - Android 11（**Thanox 2.x** 版本均支持）
* Android 12（从**Thanox 3.x**开始支持）
* Android 13（从**Thanox 4.x**开始支持）

&nbsp;
后续将主力维护Android最新三个大版本。

&nbsp;

## 工作原理

**Thanox**整体架构分两层，分别是**App**层，**Framework**层。

* **Framework**层运行于`system_server`进程，负责核心管理逻辑，拥有system级别的权限。
* **App**层仅是一个普通的应用，负责为用户提供UI交互。

&nbsp;

### 架构图



![thanox-arch](assets/images/thanox-arch.png)



&nbsp;

## 数据存储

由于**Thanox**整体架构分两层，其数据也分两部分存储。

* **Framework**层的数据存储在`/data/system/thanos${16位随机字母}`下，Thanox各个功能的数据也存在此处。
* **App**层仅仅存储一些简单的UI配置数据，使用系统设置清除数据并不会清除Thanox各个功能的数据。
