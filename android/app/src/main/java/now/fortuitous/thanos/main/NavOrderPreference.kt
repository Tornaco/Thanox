package now.fortuitous.thanos.main

import android.content.Context
import androidx.preference.PreferenceManager

object NavOrderPreference {
    private const val KEY_GROUP_ORDER = "NAV_GROUP_ORDER"
    private const val KEY_FEATURE_ORDER_PREFIX = "NAV_FEATURE_ORDER_"

    fun getGroupOrder(context: Context): List<String>? {
        val str = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_GROUP_ORDER, null) ?: return null
        return str.split(",").filter { it.isNotEmpty() }
    }

    fun setGroupOrder(context: Context, order: List<String>) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(KEY_GROUP_ORDER, order.joinToString(","))
            .apply()
    }

    fun getFeatureOrder(context: Context, groupKey: String): List<Int>? {
        val str = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_FEATURE_ORDER_PREFIX + groupKey, null) ?: return null
        return str.split(",").filter { it.isNotEmpty() }.mapNotNull { it.toIntOrNull() }
    }

    fun setFeatureOrder(context: Context, groupKey: String, order: List<Int>) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(KEY_FEATURE_ORDER_PREFIX + groupKey, order.joinToString(","))
            .apply()
    }

    fun resetAll(context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.remove(KEY_GROUP_ORDER)
        prefs.all.keys.filter { it.startsWith(KEY_FEATURE_ORDER_PREFIX) }.forEach {
            editor.remove(it)
        }
        editor.apply()
    }

    fun applyOrder(context: Context, groups: List<FeatureItemGroup>): List<FeatureItemGroup> {
        val groupOrder = getGroupOrder(context)
        val orderedGroups = if (groupOrder != null) {
            val groupMap = groups.associateBy { it.key }
            val ordered = groupOrder.mapNotNull { groupMap[it] }
            val remaining = groups.filter { it.key !in groupOrder }
            ordered + remaining
        } else {
            groups
        }

        return orderedGroups.map { group ->
            val featureOrder = getFeatureOrder(context, group.key)
            if (featureOrder != null) {
                val itemMap = group.items.associateBy { it.id }
                val ordered = featureOrder.mapNotNull { itemMap[it] }
                val remaining = group.items.filter { it.id !in featureOrder }
                group.copy(items = ordered + remaining)
            } else {
                group
            }
        }
    }
}
