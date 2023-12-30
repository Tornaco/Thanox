package now.fortuitous.thanos.apps;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class AppComponentBackup {
    public final List<String> disabledComponents;

    public AppComponentBackup(List<String> disabledComponents) {
        this.disabledComponents = disabledComponents;
    }
}
