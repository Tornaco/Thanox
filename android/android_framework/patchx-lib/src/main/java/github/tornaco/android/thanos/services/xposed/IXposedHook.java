package github.tornaco.android.thanos.services.xposed;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;

public interface IXposedHook extends IXposedHookZygoteInit, IXposedHookLoadPackage {}
