---
layout: default
title:  "后台启动"
summary: "面向喜欢折腾的用户"
date:   2018-07-14 15:58:00
nav_order: 6
---
<!-- more -->

## 功能介绍
Thanox的后台启动功能可以有效的拦截app在后台被偷偷启动（通常称为：关联启动）的问题。


## 允许与阻止的原则
Thanox会根据用户配置、App运行状态、规则等综合因素来决定是否要拦截某个app的关联启动。如果要查看一个app为什么被阻止或允许了启动，可以进入后台启动功能，右上角菜单查看启动记录，点击chart中间的大数字，可以进入启动记录详情页面。

图。


图中可以看到每条启动记录的处理结果（允许或阻止）和原因，其中`BYPASS_XXX`代表允许的原因，`BLOCKED_XXX`代表阻止原因。



下面是`BYPASS`和`BLOCK`的对照表：

@JvmField
        val BY_PASS_WHITE_LISTED
        val BY_PASS_CALLER_WHITE_LISTED
        val BY_PASS_DEFAULT = StartResult(true, "BY_PASS_DEFAULT")
        @JvmField
        val BY_PASS_ACCESSIBILITY_SERVICE = StartResult(true, "BY_PASS_ACCESSIBILITY_SERVICE")
        @JvmField
        val BY_PASS_DEFAULT_THANOS_ERROR = StartResult(true, "BY_PASS_DEFAULT_THANOS_ERROR")
        @JvmField
        val BY_PASS_DEFAULT_THANOS_TIMEOUT = StartResult(true, "BY_PASS_DEFAULT_THANOS_TIMEOUT")
        @JvmField
        val BY_PASS_BAD_ARGS = StartResult(true, "BY_PASS_BAD_ARGS")
        @JvmField
        val BY_PASS_START_BLOCKED_DISABLED = StartResult(true, "BY_PASS_START_BLOCKED_DISABLED")
        @JvmField
        val BY_PASS_UI_PRESENT = StartResult(true, "BY_PASS_UI_PRESENT")
        @JvmField
        val BY_PASS_DEFAULT_IME_SERVICE = StartResult(true, "BY_PASS_DEFAULT_IME_SERVICE")
        @JvmField
        val BY_PASS_PROCESS_RUNNING = StartResult(true, "BY_PASS_PROCESS_RUNNING")
        @JvmField
        val BY_PASS_SAME_CALLING_UID = StartResult(true, "BY_PASS_SAME_CALLING_UID")
        @JvmField
        val BY_PASS_SMS_APP = StartResult(true, "BY_PASS_SMS_APP")
        @JvmField
        val BLOCKED_STRUGGLE = StartResult(false, "BLOCKED_STRUGGLE")
        @JvmField
        val BLOCKED_IN_BLOCK_LIST = StartResult(false, "BLOCKED_IN_BLOCK_LIST")
        @JvmField
        val BLOCKED_COMPONENT_IS_DISABLED = StartResult(false, "BLOCKED_COMPONENT_IS_DISABLED")
        @JvmField
        val BLOCKED_IN_SMART_STAND_BY_LIST = StartResult(false, "BLOCKED_IN_SMART_STAND_BY_LIST")

        @JvmField
        val BLOCKED_USER_RULE = StartResult(false, "BLOCKED_USER_RULE")
        @JvmField
        val BLOCKED_STANDBY = StartResult(false, "BLOCKED_STANDBY")
        @JvmField
        val BYPASS_USER_RULE = StartResult(true, "BYPASS_USER_RULE")

        @JvmField
        val BLOCKED_BLOCK_API = StartResult(false, "BLOCKED_BLOCK_API")



val BY_PASS_WHITE_LISTED = StartResult(true, "BY_PASS_WHITE_LISTED")
       
        val BY_PASS_CALLER_WHITE_LISTED = StartResult(true, "BY_PASS_CALLER_WHITE_LISTED")
       
        val BY_PASS_DEFAULT = StartResult(true, "BY_PASS_DEFAULT")
       
        val BY_PASS_ACCESSIBILITY_SERVICE = StartResult(true, "BY_PASS_ACCESSIBILITY_SERVICE")
       
        val BY_PASS_DEFAULT_THANOS_ERROR = StartResult(true, "BY_PASS_DEFAULT_THANOS_ERROR")
       
        val BY_PASS_DEFAULT_THANOS_TIMEOUT = StartResult(true, "BY_PASS_DEFAULT_THANOS_TIMEOUT")
       
        val BY_PASS_BAD_ARGS = StartResult(true, "BY_PASS_BAD_ARGS")
       
        val BY_PASS_START_BLOCKED_DISABLED = StartResult(true, "BY_PASS_START_BLOCKED_DISABLED")
       
        val BY_PASS_UI_PRESENT = StartResult(true, "BY_PASS_UI_PRESENT")
       
        val BY_PASS_DEFAULT_IME_SERVICE = StartResult(true, "BY_PASS_DEFAULT_IME_SERVICE")
       
        val BY_PASS_PROCESS_RUNNING = StartResult(true, "BY_PASS_PROCESS_RUNNING")
       
        val BY_PASS_SAME_CALLING_UID = StartResult(true, "BY_PASS_SAME_CALLING_UID")
       
        val BY_PASS_SMS_APP = StartResult(true, "BY_PASS_SMS_APP")
       
        val BLOCKED_STRUGGLE = StartResult(false, "BLOCKED_STRUGGLE")
       
        val BLOCKED_IN_BLOCK_LIST = StartResult(false, "BLOCKED_IN_BLOCK_LIST")
       
        val BLOCKED_COMPONENT_IS_DISABLED = StartResult(false, "BLOCKED_COMPONENT_IS_DISABLED")
       
        val BLOCKED_IN_SMART_STAND_BY_LIST = StartResult(false, "BLOCKED_IN_SMART_STAND_BY_LIST")

       
        val BLOCKED_USER_RULE = StartResult(false, "BLOCKED_USER_RULE")
       
        val BLOCKED_STANDBY = StartResult(false, "BLOCKED_STANDBY")
       
        val BYPASS_USER_RULE = StartResult(true, "BYPASS_USER_RULE")

       
        val BLOCKED_BLOCK_API = StartResult(false, "BLOCKED_BLOCK_API")


### 规则
后台启动规则可以更精确的控制app的后台启动行为，例如；通过规则，可以只允许微信在后台被QQ启动而禁止被其他app启动。

### 规则语法
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
