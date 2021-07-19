package github.tornaco.android.thanos.core.persist.i;

import github.tornaco.android.thanos.core.annotation.Nullable;
import github.tornaco.android.thanos.core.util.function.Predicate;
import java.util.Collection;
import java.util.Set;

/**
 * Created by guohao4 on 2017/12/11. Email: Tornaco@163.com
 */

public interface SetRepo<T> extends Repo {

  Set<T> getAll();

  boolean add(T t);

  boolean addAll(Collection<? extends T> c);

  boolean remove(T t);

  void removeAll();

  boolean has(T t);

  // Note, Array element may be null
  boolean has(T[] t);

  int size();

  @Nullable
  T find(Predicate<T> predicate);
}
