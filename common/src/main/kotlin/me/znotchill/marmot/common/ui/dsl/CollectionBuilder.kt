package me.znotchill.marmot.common.ui.dsl

import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.classes.UIColor
import me.znotchill.marmot.common.ui.components.*
import me.znotchill.marmot.common.ui.components.props.*

@UIDsl
class CollectionBuilder<P : CollectionProps>(
    val window: UIWindow,
    val props: P
) {
    val children = mutableListOf<Component>()

    var backgroundColor: UIColor?
        get() = props.backgroundColor
        set(value) { props.backgroundColor = value }

    var padding: Spacing
        get() = props.padding
        set(value) { props.padding = value }

    fun text(
        id: String = "",
        block: (TextProps.() -> Unit)
    ): Text {
        val props = TextProps().apply(block)
        val comp = Text(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun sprite(
        id: String = "",
        block: (SpriteProps.() -> Unit)
    ): Sprite {
        val props = SpriteProps().apply(block)
        val comp = Sprite(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun line(
        id: String = "",
        block: (LineProps.() -> Unit)
    ): Line {
        val props = LineProps().apply(block)
        val comp = Line(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun gradient(
        id: String = "",
        block: (GradientProps.() -> Unit)
    ): Gradient {
        val props = GradientProps().apply(block)
        val comp = Gradient(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun box(
        id: String = "",
        block: (BoxProps.() -> Unit)
    ): Box {
        val props = BoxProps().apply(block)
        val comp = Box(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun progressBar(
        id: String = "",
        block: (ProgressBarProps.() -> Unit)
    ): ProgressBar {
        val props = ProgressBarProps().apply(block)
        val comp = ProgressBar(
            props = props
        )
        comp.window = window
        comp.name = id
        children += comp
        return comp
    }

    fun CollectionBuilder<CollectionProps>.group(
        id: String = "",
        init: CollectionBuilder<CollectionProps>.() -> Unit
    ) {
        val builder = CollectionBuilder(window, CollectionProps())
        builder.init()
        val comp = builder.buildComponent(::Group)
        comp.name = id
        children += comp
    }

    fun CollectionBuilder<FlowProps>.flow(
        id: String = "",
        init: CollectionBuilder<FlowProps>.() -> Unit
    ) {
        val builder = CollectionBuilder(window, props)
        builder.init()
        val comp = builder.buildComponent(::FlowContainer)
        comp.name = id
        children += comp
    }

    fun build(): List<Component> = children

    fun <C : Component> buildComponent(factory: (P) -> C): C {
        props.components.addAll(children)
        return factory(props)
    }
}