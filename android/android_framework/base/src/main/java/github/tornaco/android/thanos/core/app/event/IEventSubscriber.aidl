package github.tornaco.android.thanos.core.app.event;

import github.tornaco.android.thanos.core.app.event.ThanosEvent;

interface IEventSubscriber {
    void onEvent(in ThanosEvent e);
}
