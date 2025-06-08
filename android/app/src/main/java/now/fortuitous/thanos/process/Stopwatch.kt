package now.fortuitous.thanos.process

import com.elvishew.xlog.XLog

class Stopwatch(private val tag: String = "Stopwatch") {

    private data class Step(val name: String, val timestamp: Long)

    private val steps = mutableListOf<Step>()
    private val startTime = System.currentTimeMillis()

    init {
        steps.add(Step("Start", startTime))
    }

    /** 记录一个步骤 */
    fun step(name: String) {
        val now = System.currentTimeMillis()
        steps.add(Step(name, now))
    }

    /** 打印所有步骤及耗时 */
    fun log() {
        val builder = StringBuilder()
        builder.append("⏱️ Stopwatch [$tag] steps:\n")

        for (i in 1 until steps.size) {
            val prev = steps[i - 1]
            val current = steps[i]
            val delta = current.timestamp - prev.timestamp
            builder.append("  ${i}. ${current.name} +${delta}ms\n")
        }

        val total = steps.last().timestamp - startTime
        builder.append("🕒 Total: ${total}ms")
        XLog.d(builder.toString())
    }
}
