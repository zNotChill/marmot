package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.BoxProps

@Serializable
@SerialName("box")
open class BoxComponent(
    override val props: BoxProps
) : Component() {
    override val compType: CompType = CompType.BOX

    override fun width(): Float {
        return props.size.x
    }

    override fun height(): Float {
        return props.size.y
    }
}