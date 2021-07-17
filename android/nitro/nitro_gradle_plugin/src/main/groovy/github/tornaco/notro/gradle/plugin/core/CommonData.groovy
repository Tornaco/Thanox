package github.tornaco.notro.gradle.plugin.core

class CommonData {

    def static classAndPath = [:]

    def static putClassAndPath(def className, def classFilePath) {
        classAndPath.put(className, classFilePath)
    }

    def static getClassPath(def className) {
        return classAndPath.get(className)
    }
}
