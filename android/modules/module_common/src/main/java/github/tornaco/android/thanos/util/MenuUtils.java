package github.tornaco.android.thanos.util;

import androidx.appcompat.widget.PopupMenu;

import com.elvishew.xlog.XLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

public class MenuUtils {
    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(Objects.requireNonNull(menuPopupHelper).getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    XLog.i("menuPopupHelper: " + menuPopupHelper);
                    break;
                }
            }
        } catch (Throwable e) {
            XLog.e("setForceShowIcon", e);
        }
    }
}
