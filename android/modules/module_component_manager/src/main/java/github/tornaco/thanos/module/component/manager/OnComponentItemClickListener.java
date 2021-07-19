package github.tornaco.thanos.module.component.manager;

import android.view.View;

import github.tornaco.thanos.module.component.manager.model.ComponentModel;

public interface OnComponentItemClickListener {
    void onComponentItemClick(View anchor, ComponentModel componentModel);
}
