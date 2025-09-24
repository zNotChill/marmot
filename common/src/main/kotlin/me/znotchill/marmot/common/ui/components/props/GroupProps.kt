package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.components.Component

@Serializable
data class GroupProps(
    var components: List<Component> = listOf(),
    var backgroundColor: UIColor? = null
) : BaseProps()