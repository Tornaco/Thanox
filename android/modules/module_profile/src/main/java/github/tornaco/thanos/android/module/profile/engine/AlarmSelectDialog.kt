package github.tornaco.thanos.android.module.profile.engine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import github.tornaco.android.thanos.core.alarm.Alarm
import github.tornaco.android.thanos.core.alarm.Repeat
import github.tornaco.android.thanos.core.alarm.TimeOfADay
import github.tornaco.android.thanos.core.alarm.WeekDay
import github.tornaco.android.thanos.module.compose.common.theme.ColorDefaults
import github.tornaco.android.thanos.module.compose.common.widget.StandardSpacer
import github.tornaco.android.thanos.module.compose.common.widget.ThanoxDialog
import github.tornaco.thanos.android.module.profile.R
import github.tornaco.thanos.android.module.profile.engine.timepicker.BottomTimePicker
import java.time.LocalTime

class AlarmSelectorState(
    initialTime: LocalTime,
    initialRepeat: Array<WeekDay>,
    val selected: (Alarm) -> Unit
) {
    private var _isShow by mutableStateOf(false)
    val isShow get() = _isShow

    var time: LocalTime = initialTime

    var repeat = mutableStateOf(initialRepeat.toList())

    var tag by mutableStateOf("")

    fun addRepeat(weekDay: WeekDay) {
        repeat.value = repeat.value.toMutableList().apply {
            add(weekDay)
        }
    }

    fun removeRepeat(weekDay: WeekDay) {
        repeat.value = repeat.value.toMutableList().apply {
            remove(weekDay)
        }
    }

    fun show() {
        _isShow = true
    }

    fun dismiss() {
        _isShow = false
    }
}

@Composable
fun rememberAlarmSelectorState(
    initialTime: LocalTime = LocalTime.now(),
    initialRepeat: Array<WeekDay> = emptyArray(),
    selected: (Alarm) -> Unit
): AlarmSelectorState {
    return remember {
        AlarmSelectorState(initialTime, initialRepeat, selected)
    }
}

@Composable
fun AlarmSelector(state: AlarmSelectorState) {
    if (state.isShow) {
        ThanoxDialog(onDismissRequest = { state.dismiss() }, title = {
            DialogTitle(text = stringResource(id = github.tornaco.android.thanos.res.R.string.module_profile_date_time_alarm))
        }, buttons = {
            TextButton(onClick = {
                state.selected(
                    Alarm(
                        label = state.tag,
                        triggerAt = TimeOfADay(
                            hour = state.time.hour,
                            minutes = state.time.minute,
                            seconds = state.time.second
                        ),
                        repeat = Repeat(state.repeat.value)
                    )
                )
                state.dismiss()
            }) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        }, content = {
            AlarmContent(state)
        })
    }
}

@Composable
private fun AlarmContent(state: AlarmSelectorState) {
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Tag(state = state)

        BottomTimePicker(is24TimeFormat = true, currentTime = state.time) {
            state.time = it
        }
        RepeatSelector(state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Tag(state: AlarmSelectorState) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {
        TextField(
            label = {
                Text(text = "Tag")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Tag,
                    contentDescription = "tag"
                )
            },
            value = state.tag, onValueChange = {
                state.tag = it
            })
        Text(
            text = stringResource(
                id = github.tornaco.android.thanos.res.R.string.module_profile_date_time_tag,
                "\ncondition: \"timeTick && tag == \"${state.tag}\"\""
            ), style = MaterialTheme.typography.labelSmall
        )
    }
    StandardSpacer()
}

@Composable
private fun RepeatSelector(state: AlarmSelectorState) {
    Column(
        modifier = Modifier.padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Repeat",
            style = MaterialTheme.typography.bodySmall
        )
        StandardSpacer()
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            arrayOf(
                WeekDay.SUNDAY,
                WeekDay.MONDAY,
                WeekDay.TUESDAY,
                WeekDay.WEDNESDAY,
                WeekDay.THURSDAY,
                WeekDay.FRIDAY,
                WeekDay.SATURDAY
            ).forEach { weekDay ->
                val isSelected = state.repeat.value.contains(weekDay)
                Column(
                    modifier = Modifier
                        .width(36.dp)
                        .height(36.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else ColorDefaults.backgroundSurfaceColor())
                        .clickable {
                            if (isSelected) {
                                state.removeRepeat(weekDay)
                            } else {
                                state.addRepeat(weekDay)
                            }
                        }
                        .padding(4.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weekDay.name.first().toString().uppercase(),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = W600)
                    )
                }
            }
        }
    }
}


@Composable
fun DialogTitle(text: String) {
    Text(
        modifier = Modifier.padding(8.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge
    )
}