package github.tornaco.android.thanos.util

fun <T> sortByIndex(dataList: List<T>, sortedIndexes: List<Int>): List<T> {
    val sortedList = MutableList(dataList.size) { dataList[0] }

    for (i in sortedIndexes.indices) {
        sortedList[i] = dataList[sortedIndexes[i]]
    }

    return sortedList
}