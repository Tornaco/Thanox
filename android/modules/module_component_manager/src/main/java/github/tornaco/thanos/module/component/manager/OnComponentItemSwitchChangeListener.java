package github.tornaco.thanos.module.component.manager;

import github.tornaco.thanos.module.component.manager.model.ComponentModel;

public interface OnComponentItemSwitchChangeListener {
    void onComponentItemSwitchChange(ComponentModel componentModel, boolean checked);
}
