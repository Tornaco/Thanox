package github.tornaco.permission.requester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by guohao4 on 2017/9/8.
 * Email: Tornaco@163.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RuntimePermissions {
    String classNameSubFix() default "PermissionRequester";
}
