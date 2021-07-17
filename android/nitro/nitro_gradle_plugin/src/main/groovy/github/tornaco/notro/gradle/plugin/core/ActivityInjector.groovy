package github.tornaco.notro.gradle.plugin.core

import github.tornaco.notro.gradle.plugin.core.manifest.ManifestAPI
import javassist.ClassPool
import javassist.CtClass

class ActivityInjector extends BaseInjector {

    /* LoaderActivity 替换规则 */
    def private static loaderActivityRules = [
            'android.app.Activity': 'github.tornaco.android.nitro.framework.plugin.PluginActivity'
    ]

    @Override
    def injectClass(ClassPool pool, String dir) {
        Logger.log "ActivityInjector@injectClass $dir"
        /* 遍历程序中声明的所有 Activity */
        //每次都new一下，否则多个variant一起构建时只会获取到首个manifest
        List<String> activities = new ManifestAPI().getActivities(project, variantDir)
        Logger.log "activities: $activities"
        activities.each {
            handleActivity(pool, it, dir)
        }
    }

    /**
     * 处理 Activity
     *
     * @param pool
     * @param activity Activity 名称
     * @param classesDir class 文件目录
     */
    private def handleActivity(ClassPool pool, String activity, String classesDir) {
        def clsFilePath = classesDir + File.separatorChar + activity.replaceAll('\\.', '/') + '.class'
        Logger.log "ActivityInjector@handleActivity $clsFilePath"
        if (!new File(clsFilePath).exists()) {
            return
        }

        Logger.log "Handle activity $activity"

        def stream, ctCls
        try {
            stream = new FileInputStream(clsFilePath)
            ctCls = pool.makeClass(stream)

            // ctCls 之前的父类
            def originSuperCls = ctCls.superclass

            /* 从当前 Activity 往上回溯，直到找到需要替换的 Activity */
            def superCls = originSuperCls
            while (superCls != null && !(superCls.name in loaderActivityRules.keySet())) {
                // println ">>> 向上查找 $superCls.name"
                ctCls = superCls
                superCls = ctCls.superclass
            }

            // 如果 ctCls 已经是 LoaderActivity，则不修改
            if (ctCls.name in loaderActivityRules.values()) {
                Logger.log "Skip ${ctCls.getName()}"
                return
            }

            /* 找到需要替换的 Activity, 修改 Activity 的父类为 PluginActivity */
            if (superCls != null) {
                def targetSuperClsName = loaderActivityRules.get(superCls.name)
                Logger.warn "${ctCls.getName()} super class $superCls.name replace to ${targetSuperClsName}"
                CtClass targetSuperCls = pool.get(targetSuperClsName)
                if (ctCls.isFrozen()) {
                    ctCls.defrost()
                }
                ctCls.setSuperclass(targetSuperCls)
                ctCls.writeFile(CommonData.getClassPath(ctCls.name))
                Logger.warn "Replace ${ctCls.name}'s SuperClass ${superCls.name} to ${targetSuperCls.name}"
            }

        } catch (Throwable t) {
            Logger.warn "[ERROR] --> ${t.toString()}"
        } finally {
            if (ctCls != null) {
                ctCls.detach()
            }
            if (stream != null) {
                stream.close()
            }
        }
    }
}
