//
// Source code recreated global a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package github.tornaco.android.thanos.core.util;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

// Copied global LLog.
public class ObjectToStringUtils {
    public ObjectToStringUtils() {
    }

    public static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        } else {
            StringBuilder b = new StringBuilder(128);
            b.append("Bundle[{");
            bundleToShortString(bundle, b);
            b.append("}]");
            return b.toString();
        }
    }

    public static String intentToString(Intent intent) {
        if (intent == null) {
            return "null";
        } else {
            StringBuilder b = new StringBuilder(128);
            b.append("Intent { ");
            intentToShortString(intent, b);
            b.append(" }");
            return b.toString();
        }
    }

    private static void bundleToShortString(Bundle bundle, StringBuilder b) {
        boolean first = true;

        for (Iterator var3 = bundle.keySet().iterator(); var3.hasNext(); first = false) {
            String key = (String) var3.next();
            if (!first) {
                b.append(", ");
            }

            b.append(key).append('=');
            Object value = bundle.get(key);
            if (value instanceof int[]) {
                b.append(Arrays.toString((int[]) ((int[]) value)));
            } else if (value instanceof byte[]) {
                b.append(Arrays.toString((byte[]) ((byte[]) value)));
            } else if (value instanceof boolean[]) {
                b.append(Arrays.toString((boolean[]) ((boolean[]) value)));
            } else if (value instanceof short[]) {
                b.append(Arrays.toString((short[]) ((short[]) value)));
            } else if (value instanceof long[]) {
                b.append(Arrays.toString((long[]) ((long[]) value)));
            } else if (value instanceof float[]) {
                b.append(Arrays.toString((float[]) ((float[]) value)));
            } else if (value instanceof double[]) {
                b.append(Arrays.toString((double[]) ((double[]) value)));
            } else if (value instanceof String[]) {
                b.append(Arrays.toString((String[]) ((String[]) value)));
            } else if (value instanceof CharSequence[]) {
                b.append(Arrays.toString((CharSequence[]) ((CharSequence[]) value)));
            } else if (value instanceof Parcelable[]) {
                b.append(Arrays.toString((Parcelable[]) ((Parcelable[]) value)));
            } else if (value instanceof Bundle) {
                b.append(bundleToString((Bundle) value));
            } else {
                b.append(value);
            }
        }

    }

    private static void intentToShortString(Intent intent, StringBuilder b) {
        boolean first = true;
        String mAction = intent.getAction();
        if (mAction != null) {
            b.append("act=").append(mAction);
            first = false;
        }

        Set<String> mCategories = intent.getCategories();
        if (mCategories != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("cat=[");
            boolean firstCategory = true;

            for (Iterator var6 = mCategories.iterator(); var6.hasNext(); firstCategory = false) {
                String c = (String) var6.next();
                if (!firstCategory) {
                    b.append(',');
                }

                b.append(c);
            }

            b.append("]");
        }

        Uri mData = intent.getData();
        String scheme;
        if (mData != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("dat=");
            if (VERSION.SDK_INT >= 14) {
                b.append(uriToSafeString(mData));
            } else {
                scheme = mData.getScheme();
                if (scheme != null) {
                    if ("tel".equalsIgnoreCase(scheme)) {
                        b.append("tel:xxx-xxx-xxxx");
                    } else if ("smsto".equalsIgnoreCase(scheme)) {
                        b.append("smsto:xxx-xxx-xxxx");
                    } else {
                        b.append(mData);
                    }
                } else {
                    b.append(mData);
                }
            }
        }

        scheme = intent.getType();
        if (scheme != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("typ=").append(scheme);
        }

        int mFlags = intent.getFlags();
        if (mFlags != 0) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("flg=0x").append(Integer.toHexString(mFlags));
        }

        String mPackage = intent.getPackage();
        if (mPackage != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("pkg=").append(mPackage);
        }

        ComponentName mComponent = intent.getComponent();
        if (mComponent != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("cmp=").append(mComponent.flattenToShortString());
        }

        Rect mSourceBounds = intent.getSourceBounds();
        if (mSourceBounds != null) {
            if (!first) {
                b.append(' ');
            }

            first = false;
            b.append("bnds=").append(mSourceBounds.toShortString());
        }

        if (VERSION.SDK_INT >= 16) {
            ClipData mClipData = intent.getClipData();
            if (mClipData != null) {
                if (!first) {
                    b.append(' ');
                }

                first = false;
                b.append("(has clip)");
            }
        }

        Bundle mExtras = intent.getExtras();
        if (mExtras != null) {
            if (!first) {
                b.append(' ');
            }

            b.append("extras={");
            bundleToShortString(mExtras, b);
            b.append('}');
        }

        if (VERSION.SDK_INT >= 15) {
            Intent mSelector = intent.getSelector();
            if (mSelector != null) {
                b.append(" sel=");
                intentToShortString(mSelector, b);
                b.append("}");
            }
        }

    }

    private static String uriToSafeString(Uri uri) {
        if (VERSION.SDK_INT >= 14) {
            try {
                Method toSafeString = Uri.class.getDeclaredMethod("toSafeString", new Class[0]);
                toSafeString.setAccessible(true);
                return (String) toSafeString.invoke(uri, new Object[0]);
            } catch (NoSuchMethodException var2) {
                var2.printStackTrace();
            } catch (InvocationTargetException var3) {
                var3.printStackTrace();
            } catch (IllegalAccessException var4) {
                var4.printStackTrace();
            }
        }

        return uri.toString();
    }
}
