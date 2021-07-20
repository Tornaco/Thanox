package com.android.server.wm;

public class RecentTasksCallbackSpy implements RecentTasks.Callbacks {
  @Override
  public final void onRecentTaskAdded(TaskRecord task) {
    onRecentTaskAddedSpy(task);
  }

  @Override
  public final void onRecentTaskRemoved(TaskRecord task, boolean wasTrimmed, boolean killProcess) {
    onRecentTaskRemovedSpy(task, wasTrimmed, killProcess);
  }

  public void onRecentTaskAddedSpy(Object task) {}

  public void onRecentTaskRemovedSpy(Object task, boolean wasTrimmed, boolean killProcess) {}
}
