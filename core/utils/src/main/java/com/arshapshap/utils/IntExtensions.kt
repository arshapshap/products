package com.arshapshap.utils

import java.text.DecimalFormat

fun Int.toStringWithSpaces(): String {
    return DecimalFormat("###,###").format(this).replace(',', ' ')
}

fun Double.toStringWithCommas(): String {
    return this.toString().replace('.', ',')
}