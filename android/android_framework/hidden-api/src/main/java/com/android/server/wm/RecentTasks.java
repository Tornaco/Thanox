package com.android.server.wm;

class RecentTasks {
  public RecentTasks(ActivityTaskManagerService service, ActivityStackSupervisor stackSupervisor) {}

  void remove(TaskRecord task) {}

  /** Callbacks made when manipulating the list. */
  interface Callbacks {
    /** Called when a task is added to the recent tasks list. */
    void onRecentTaskAdded(TaskRecord task);

    /** Called when a task is removed from the recent tasks list. */
    void onRecentTaskRemoved(TaskRecord task, boolean wasTrimmed, boolean killProcess);
  }
}
