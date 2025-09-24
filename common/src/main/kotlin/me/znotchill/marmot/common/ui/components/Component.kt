package me.znotchill.marmot.common.ui.components

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import me.znotchill.marmot.common.ui.classes.CompType
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.components.props.BaseProps

@Serializable
sealed class Component {
    var id: String = ""
    var relativeTo: String? = null
    var relativePosition: RelativePosition? = null
    abstract val compType: CompType
    abstract val props: BaseProps

    abstract fun width(): Int
    abstract fun height(): Int

    @Transient
    var screenX: Int = 0
    @Transient
    var screenY: Int = 0
}

infix fun Component.relative(component: Component): Component {
    this.relativeTo = component.id
    return this
}

infix fun Component.rightOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.RIGHT_OF
    return this
}

infix fun Component.leftOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.LEFT_OF
    return this
}

infix fun Component.topOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.ABOVE
    return this
}

infix fun Component.bottomOf(component: Component): Component {
    this.relativeTo = component.id
    this.relativePosition = RelativePosition.BELOW
    return this
}