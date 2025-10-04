package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.GradientProps

@Serializable
@SerialName("gradient")
open class GradientComponent(
    override val props: GradientProps
) : Component() {
    override val compType: CompType = CompType.GRADIENT

    override fun width(): Int {
        return 1
    }

    override fun height(): Int {
        return 1
    }
}