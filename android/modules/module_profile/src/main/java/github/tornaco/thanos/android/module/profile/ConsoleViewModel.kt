/*
 * (C) Copyright 2022 Thanox
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package github.tornaco.thanos.android.module.profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.elvishew.xlog.XLog
import com.wakaztahir.codeeditor.model.CodeLang
import com.wakaztahir.codeeditor.prettify.PrettifyParser
import com.wakaztahir.codeeditor.theme.CodeThemeType
import com.wakaztahir.codeeditor.utils.parseCodeAsAnnotatedString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import github.tornaco.android.thanos.core.app.ThanosManager
import github.tornaco.android.thanos.core.profile.LogSink
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ConsoleState(
    val textFieldValue: TextFieldValue,
    val consoleLogOutput: String,
)

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ConsoleViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {
    // Step 1. Declare Language & Code
    private val language = CodeLang.Kotlin

    // Step 2. Create Parser & Theme
    private val parser = PrettifyParser()
    private val themeState = CodeThemeType.Default
    private val theme = themeState.getTheme()

    private val _state =
        MutableStateFlow(
            ConsoleState(
                textFieldValue = TextFieldValue(
                    text = "ui.showShortToast(\"Running apps: \" + thanos.getActivityManager().getRunningAppsCount());"
                ).toCodeStyledTextFieldValue(),
                consoleLogOutput = ""
            )
        )
    val state = _state.asStateFlow()

    private val thanox by lazy { ThanosManager.from(context) }

    private val logSink = object : LogSink() {
        override fun log(message: String?) {
            super.log(message)
            _state.value = _state.value.copy(consoleLogOutput = message ?: "null")
        }
    }

    init {
        thanox.profileManager.addConsoleLogSink(logSink)
    }

    override fun onCleared() {
        super.onCleared()
        thanox.profileManager.removeConsoleLogSink(logSink)
    }

    fun input(input: TextFieldValue) {
        val updatedValue = input.toCodeStyledTextFieldValue()
        _state.value = _state.value.copy(textFieldValue = updatedValue)
    }

    fun insertSymbol(symbol: String) {
        val selection = state.value.textFieldValue.selection
        XLog.d("insertSymbol: $selection")
        val s = selection.start
        val e = selection.end

        val text = state.value.textFieldValue.text

        val textWithSymbol = text.substring(0, s) + symbol + text.substring(s)

        input(
            state.value.textFieldValue.copy(
                text = textWithSymbol,
                selection = TextRange(e + symbol.length)
            )
        )
    }

    fun execute() {
        _state.value = _state.value.copy(consoleLogOutput = "")
        thanox.profileManager.executeAction(state.value.textFieldValue.text)
    }

    private fun TextFieldValue.toCodeStyledTextFieldValue() = this.copy(
        annotatedString = parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = this.text
        )
    )
}