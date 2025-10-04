package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.LineProps

@Serializable
@SerialName("line")
open class LineComponent(
    override val props: LineProps
) : Component() {
    override val compType: CompType = CompType.LINE

    override fun width(): Int {
        return 1
    }

    override fun height(): Int {
        return 1
    }
}