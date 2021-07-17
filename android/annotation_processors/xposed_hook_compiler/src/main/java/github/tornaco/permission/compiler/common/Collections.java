package github.tornaco.permission.compiler.common;


import com.google.common.base.Preconditions;

import java.util.Collection;

/**
 * Created by Nick@NewStand.org on 2017/3/8 13:49
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

public abstract class Collections {

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
}
