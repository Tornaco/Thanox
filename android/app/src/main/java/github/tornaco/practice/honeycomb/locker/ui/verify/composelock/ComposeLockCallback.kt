package github.tornaco.practice.honeycomb.locker.ui.verify.composelock

interface ComposeLockCallback {
    fun onStart(dot: Dot)
    fun onDotConnected(dot: Dot)
    fun onResult(result: List<Dot>)
}