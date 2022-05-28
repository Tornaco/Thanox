package com.wakaztahir.codeeditor.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import com.wakaztahir.codeeditor.model.CodeLang
import com.wakaztahir.codeeditor.parser.ParseResult
import com.wakaztahir.codeeditor.prettify.PrettifyParser
import com.wakaztahir.codeeditor.theme.CodeTheme

fun List<ParseResult>.toAnnotatedString(theme: CodeTheme, source: String): AnnotatedString = AnnotatedString(
    text = source,
    spanStyles = map {
        AnnotatedString.Range(theme toSpanStyle it, it.offset, it.offset + it.length)
    }
)

fun parseCodeAsAnnotatedString(
    parser: PrettifyParser,
    theme: CodeTheme,
    lang: String,
    code: String
): AnnotatedString = parser.parse(lang, code).toAnnotatedString(theme, code)

fun parseCodeAsAnnotatedString(
    parser: PrettifyParser,
    theme: CodeTheme,
    lang: CodeLang,
    code: String
): AnnotatedString = lang.langProvider?.let {
    parser.parse(it, code).toAnnotatedString(theme, code)
} ?: parseCodeAsAnnotatedString(
    parser = parser,
    theme = theme,
    lang = lang.value.first(),
    code = code,
)