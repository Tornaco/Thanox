package github.tornaco.permission.requester;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Nick@NewStand.org on 2017/4/12 13:12
 * E-Mail: NewStand@163.com
 * All right reserved.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface RequiresPermission {
    String[] value();

    String methodSubFix() default "Checked";

    @interface Before {
        String value();
    }

    @interface OnDenied {
        String value();
    }
}
