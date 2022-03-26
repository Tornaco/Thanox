package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("task")
interface ITask {

    void removeTasksForPackage(String pkgName);

    boolean hasTaskFromPackage(String pkgName);

    void clearBackgroundTasks();
}
