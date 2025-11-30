package me.znotchill.marmot.common.classes

enum class FovOp(val id: Byte) {
    SET(0),
    ADD(1),
    MUL(2),
    DIV(3),
    SUB(4),
    RESET(5);

    companion object {
        private val map = entries.associateBy(FovOp::id)
        fun fromId(id: Byte) = map[id] ?: SET
    }
}
