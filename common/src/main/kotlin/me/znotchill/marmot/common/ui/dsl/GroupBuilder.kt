package me.znotchill.marmot.common.ui.dsl

import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.components.SpriteComponent
import me.znotchill.marmot.common.ui.components.TextComponent
import me.znotchill.marmot.common.ui.components.props.BaseProps
import me.znotchill.marmot.common.ui.components.props.GroupProps
import me.znotchill.marmot.common.ui.components.props.SpriteProps
import me.znotchill.marmot.common.ui.components.props.TextProps

@UIDsl
class GroupBuilder(
    val window: UIWindow,
    val baseProps: BaseProps = BaseProps(
        Vec2(0f, 0f),
        Vec2(100f, 100f)
    )
) {
    var children = mutableListOf<Component>()
    var groupProps: GroupProps = GroupProps()

    var backgroundColor: UIColor?
        get() = groupProps.backgroundColor
        set(value) {
            groupProps.backgroundColor = value
        }

    var padding: Spacing
        get() = groupProps.padding
        set(value) {
            groupProps.padding = value
        }

    fun text(
        id: String,
        block: (TextProps.() -> Unit)
    ): TextComponent {
        val props = TextProps().apply(block)
        val comp = TextComponent(
            props = props
        )
        comp.window = window
        comp.id = id
        children += comp
        return comp
    }

    fun sprite(
        id: String,
        block: (SpriteProps.() -> Unit)
    ): SpriteComponent {
        val props = SpriteProps().apply(block)
        val comp = SpriteComponent(
            props = props
        )
        comp.window = window
        comp.id = id
        children += comp
        return comp
    }

    fun group(
        id: String,
        init: GroupBuilder.() -> Unit
    ) {
        val subGroup = GroupBuilder(window, baseProps)
        subGroup.init()
        val comp = GroupComponent(
            props = subGroup.groupProps,
        )
        comp.id = id
        children += comp
    }

    fun build(): List<Component> = children
}