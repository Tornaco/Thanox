//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class CollectionUtils {
  public static <C> void consumeRemaining(Collection<C> collection, Consumer<C> consumer) {
    Preconditions.checkNotNull(collection);
    Preconditions.checkNotNull(consumer);
    for (C c : collection) {
      consumer.accept(c);
    }
  }

  public static <C> void consumeRemaining(C[] dataArr, Consumer<C> consumer) {
    Preconditions.checkNotNull(dataArr);
    Preconditions.checkNotNull(consumer);
    for (C c : dataArr) {
      consumer.accept(c);
    }
  }

  public static <C> void consumeRemaining(Iterable<C> collection, Consumer<C> consumer) {
    Preconditions.checkNotNull(collection);
    Preconditions.checkNotNull(consumer);
    for (C c : collection) {
      consumer.accept(c);
    }
  }

  public static boolean isNullOrEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isNullOrEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  public static int sizeOf(Collection<?> collection) {
    return isNullOrEmpty(collection) ? 0 : collection.size();
  }

  public static <T> void remove(final Collection<T> source, final Collection<T> matches) {
    consumeRemaining(
        matches,
        new Consumer<T>() {
          @Override
          public void accept(T t) {
            source.remove(t);
          }
        });
  }

  public static <X> List<String> mappingAsString(List<X> x, Function<X, String> f) {
    List<String> res = new ArrayList<>();
    consumeRemaining(
        x,
        new Consumer<X>() {
          @Override
          public void accept(X x) {
            res.add(f.apply(x));
          }
        });
    return res;
  }
}
