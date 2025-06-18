package github.tornaco.android.thanos.module.compose.common.infra

import android.annotation.SuppressLint
import android.content.Context
import github.tornaco.android.thanos.core.Logger

@SuppressLint("StaticFieldLeak")
abstract class ContextViewModel<T> constructor(val context: Context, initState: () -> T) :
    StateViewModel<T>(initState) {
    protected val logger = Logger(javaClass.simpleName)
}