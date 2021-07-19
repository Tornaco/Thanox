package github.tornaco.android.thanos.core.persist.i;

import java.util.Map;

/**
 * Created by guohao4 on 2017/12/11.
 * Email: Tornaco@163.com
 */

public interface MapRepo<K, V> extends Map<K, V> , Repo{

    Map<K, V> snapshot();

    boolean hasNoneNullValue(K k);
}
