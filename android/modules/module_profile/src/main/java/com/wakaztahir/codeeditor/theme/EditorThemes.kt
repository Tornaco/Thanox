package com.wakaztahir.codeeditor.theme

import androidx.compose.ui.graphics.Color

class DefaultTheme : CodeTheme(
    colors = SyntaxColors(
        type = Color(0xFF859900),
        keyword = Color(0xFF268BD2),
        literal = Color(0xFF269186),
        comment = Color(0xFF93A1A1),
        string = Color(0xFF269186),
        punctuation = Color(0xFF586E75),
        plain = Color(0xFF586E75),
        tag = Color(0xFF859900),
        declaration = Color(0xFF268BD2),
        source = Color(0xFF586E75),
        attrName = Color(0xFF268BD2),
        attrValue = Color(0xFF269186),
        nocode = Color(0xFF586E75),
    )
)

class MonokaiTheme : CodeTheme(
    colors = SyntaxColors(
        type = Color(0xFFA7E22E),
        keyword = Color(0xFFFA2772),
        literal = Color(0xFF66D9EE),
        comment = Color(0xFF76715E),
        string = Color(0xFFE6DB74),
        punctuation = Color(0xFFC1C1C1),
        plain = Color(0xFFF8F8F0),
        tag = Color(0xFFF92672),
        declaration = Color(0xFFFA2772),
        source = Color(0xFFF8F8F0),
        attrName = Color(0xFFA6E22E),
        attrValue = Color(0xFFE6DB74),
        nocode = Color(0xFFF8F8F0),
    )
)