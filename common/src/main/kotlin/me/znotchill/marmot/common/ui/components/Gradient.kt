package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.components.props.GradientProps

@Serializable
@SerialName("gradient")
open class Gradient(
    override val props: GradientProps
) : Component() {
    override val compType: CompType = CompType.GRADIENT

    override fun width(): Float {
        return 1f
    }

    override fun height(): Float {
        return 1f
    }
}