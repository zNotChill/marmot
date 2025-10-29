package me.znotchill.marmot.common.ui.components.props

import kotlinx.serialization.Serializable
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.components.Component

@Serializable
open class CollectionProps(
    var components: MutableList<Component> = mutableListOf(),
    var backgroundColor: UIColor? = null
) : BaseProps()