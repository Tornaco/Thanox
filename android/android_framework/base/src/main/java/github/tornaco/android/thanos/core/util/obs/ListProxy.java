package github.tornaco.android.thanos.core.util.obs;

import android.annotation.SuppressLint;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@SuppressLint("NewApi")
public class ListProxy<T> extends ArrayList<T> {
    @Delegate
    private List<T> orig;
}
