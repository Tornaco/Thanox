package com.anggrayudi.storage.extension

/**
 * Created on 08/09/20
 *
 * @author Anggrayudi H
 */
fun Int?.toBoolean() = this != null && this > 0

fun Boolean?.toInt() = if (this == true) 1 else 0
