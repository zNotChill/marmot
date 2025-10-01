package me.znotchill.marmot.common

enum class ClientPerspective(
    val firstPerson: Boolean,
    val frontView: Boolean
) {
    FIRST_PERSON(true, false),
    THIRD_PERSON_BACK(false, false),
    THIRD_PERSON_FRONT(false, true)
}