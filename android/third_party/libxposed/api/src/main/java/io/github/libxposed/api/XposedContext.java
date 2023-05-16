package io.github.libxposed.api;

import android.content.Context;

/**
 * Independent {@link Context} for each Xposed module loaded into the target process.<br/>
 * This class should be extended by the Xposed framework as the implementation of Xposed interfaces.<br/>
 * Modules should not use this class directly.
 */
public abstract class XposedContext extends Context implements XposedInterface {

}
