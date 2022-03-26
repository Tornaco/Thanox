package github.tornaco.android.thanos.core.profile.handle;

import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.su.SuRes;

@HandlerName("su")
public interface ISu {

    @Nullable
    SuRes exe(String command);
}
