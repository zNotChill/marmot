package me.znotchill.marmot.common.ui

fun String.mcWidth(): Int {
    val widths = mapOf(
        ' ' to 4, '!' to 2, '"' to 5, '#' to 6, '$' to 6, '%' to 6, '&' to 6,
        '\'' to 3, '(' to 5, ')' to 5, '*' to 6, '+' to 6, ',' to 2, '-' to 6,
        '.' to 2, '/' to 6, '0' to 6, '1' to 6, '2' to 6, '3' to 6, '4' to 6,
        '5' to 6, '6' to 6, '7' to 6, '8' to 6, '9' to 6, ':' to 2, ';' to 2,
        '<' to 5, '=' to 6, '>' to 5, '?' to 6, '@' to 7, 'A' to 6, 'B' to 6,
        'C' to 6, 'D' to 6, 'E' to 6, 'F' to 6, 'G' to 6, 'H' to 6, 'I' to 4,
        'J' to 6, 'K' to 6, 'L' to 6, 'M' to 6, 'N' to 6, 'O' to 6, 'P' to 6,
        'Q' to 6, 'R' to 6, 'S' to 6, 'T' to 6, 'U' to 6, 'V' to 6, 'W' to 6,
        'X' to 6, 'Y' to 6, 'Z' to 6, '[' to 4, '\\' to 6, ']' to 4, '^' to 6,
        '_' to 6, '`' to 3, 'a' to 6, 'b' to 6, 'c' to 6, 'd' to 6, 'e' to 6,
        'f' to 4, 'g' to 6, 'h' to 6, 'i' to 2, 'j' to 4, 'k' to 5, 'l' to 3,
        'm' to 6, 'n' to 6, 'o' to 6, 'p' to 6, 'q' to 6, 'r' to 5, 's' to 6,
        't' to 4, 'u' to 6, 'v' to 6, 'w' to 6, 'x' to 6, 'y' to 6, 'z' to 6,
        '{' to 5, '|' to 2, '}' to 5, '~' to 6
    )

    var width = 0
    for (c in this) {
        width += widths[c] ?: 6
    }
    return width
}