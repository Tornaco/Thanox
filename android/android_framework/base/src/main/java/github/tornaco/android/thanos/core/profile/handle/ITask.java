package github.tornaco.android.thanos.core.profile.handle;

@HandlerName("task")
public
interface ITask {

    void removeTasksForPackage(String pkgName);

    boolean hasTaskFromPackage(String pkgName);

    void clearBackgroundTasks();
}
