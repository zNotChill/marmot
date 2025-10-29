package me.znotchill.marmot.client.ui

import me.znotchill.marmot.client.MarmotClient
import me.znotchill.marmot.client.ui.components.BoxRenderer
import me.znotchill.marmot.client.ui.components.FlowContainerRenderer
import me.znotchill.marmot.client.ui.components.GradientRenderer
import me.znotchill.marmot.client.ui.components.GroupRenderer
import me.znotchill.marmot.client.ui.components.LineRenderer
import me.znotchill.marmot.client.ui.components.ProgressBarRenderer
import me.znotchill.marmot.client.ui.components.SpriteRenderer
import me.znotchill.marmot.client.ui.components.TextRenderer
import me.znotchill.marmot.client.ui.events.DestroyEventHandler
import me.znotchill.marmot.client.ui.events.MoveEventHandler
import me.znotchill.marmot.client.ui.events.OpacityEventHandler
import me.znotchill.marmot.client.ui.events.PaddingEventHandler
import me.znotchill.marmot.client.ui.events.ProgressEventHandler
import me.znotchill.marmot.client.ui.events.RotateEventHandler
import me.znotchill.marmot.client.ui.events.UIEventContext
import me.znotchill.marmot.common.classes.Vec2
import me.znotchill.marmot.common.ui.*
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.FlowDirection
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Spacing
import me.znotchill.marmot.common.ui.components.Box
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.FlowContainer
import me.znotchill.marmot.common.ui.components.Gradient
import me.znotchill.marmot.common.ui.components.Group
import me.znotchill.marmot.common.ui.components.Line
import me.znotchill.marmot.common.ui.components.ProgressBar
import me.znotchill.marmot.common.ui.components.Sprite
import me.znotchill.marmot.common.ui.components.Text
import me.znotchill.marmot.common.ui.events.DestroyEvent
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.OpacityEvent
import me.znotchill.marmot.common.ui.events.PaddingEvent
import me.znotchill.marmot.common.ui.events.ProgressEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation
import me.znotchill.marmot.common.ui.events.RotateEvent
import me.znotchill.marmot.common.ui.events.UIEvent
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.resource.Resource
import net.minecraft.util.Identifier
import org.joml.Matrix3x2f
import java.io.IOException
import kotlin.to

object UIRenderer {
    private var currentWindow: UIWindow? = null
    private var activeAnimations = mutableListOf<PropertyAnimation<*, *>>()
    private var lastTime: Long = System.nanoTime()

    // TODO: improve this system somehow
    // if it cannot be done, find a way to remove the ::class.java
    val eventDispatcher = UIEventDispatcher(
        handlers = mapOf(
            MoveEvent::class.java to MoveEventHandler(),
            OpacityEvent::class.java to OpacityEventHandler(),
            DestroyEvent::class.java to DestroyEventHandler(),
            RotateEvent::class.java to RotateEventHandler(),
            PaddingEvent::class.java to PaddingEventHandler(),
            ProgressEvent::class.java to ProgressEventHandler()
        ),
        context = UIEventContext(
            currentWindow = { currentWindow },
            enqueueAnimation = { anim -> enqueueAnimation(anim) }
        )
    )

    val componentRenderer = UIComponentRenderer(
        handlers = mapOf(
            Text::class.java to TextRenderer(),
            Sprite::class.java to SpriteRenderer(),
            Group::class.java to GroupRenderer(),
            Line::class.java to LineRenderer(),
            Gradient::class.java to GradientRenderer(),
            Box::class.java to BoxRenderer(),
            ProgressBar::class.java to ProgressBarRenderer(),
            FlowContainer::class.java to FlowContainerRenderer()
        )
    )

    fun setWindow(window: UIWindow?) {
        currentWindow = window
        activeAnimations = mutableListOf()
    }

    fun register() {
        HudRenderCallback.EVENT.register { context, _ ->
            val now = System.nanoTime()
            val deltaSeconds = (now - lastTime) / 1_000_000_000.0
            lastTime = now

            tickAnimations(deltaSeconds)

            currentWindow?.let { window ->
                layout(window)

                window.components.values
                    .sortedBy { it.props.zIndex }
                    .forEach { component ->
                        component.draw(context)
                    }
            }
        }
    }

    /**
     * Handles the first render or a re-render of a UI.
     */
    fun handleFreshRender(window: UIWindow) {
        setWindow(window)
    }

    /**
     * Handles UI updates for an already rendered UI.
     * Handles animations.
     */
    fun handleUpdateRender(events: List<UIEvent>) {
        events.forEach { event ->
            MarmotClient.LOGGER.info("Dispatching UI event ${event.javaClass}")
            eventDispatcher.applyEvent(event)
        }
    }

    fun <C : Component, T> enqueueAnimation(event: PropertyAnimation<C, T>) {
        val comp = currentWindow?.getComponentByIdDeep(event.targetId)
        if (comp != null && event.from == null) {
            @Suppress("UNCHECKED_CAST")
            event.from = (event.getter as (Component) -> T)(comp)
        }
        @Suppress("UNCHECKED_CAST")
        activeAnimations += event as PropertyAnimation<*, *>
    }

    fun tickAnimations(deltaSeconds: Double) {
        val iterator = activeAnimations.iterator()
        while (iterator.hasNext()) {
            val anim = iterator.next()

            val comp = currentWindow?.getComponentByIdDeep(anim.targetId) ?: continue
            anim.elapsed += deltaSeconds

            val t = (anim.elapsed / anim.durationSeconds).coerceIn(0.0, 1.0)
            val easedT = Easing.valueOf(anim.easing).getValue(t)

            @Suppress("UNCHECKED_CAST")
            val typedGetter = anim.getter as (Component) -> Any?
            @Suppress("UNCHECKED_CAST")
            val typedSetter = anim.setter as (Component, Any?) -> Unit

//            MarmotClient.LOGGER.info("deltaSeconds=$deltaSeconds elapsed=${anim.elapsed} t=$t easedT=$easedT")

            val start = anim.from ?: typedGetter(comp)
            val end = anim.to
            val result = when (start) {
                is Float -> lerp(start, end as Float, easedT)
                is Int -> lerp(start.toFloat(), (end as Int).toFloat(), easedT).toInt()
                is Vec2 -> Vec2(
                    lerp(start.x, (end as Vec2).x, easedT),
                    lerp(start.y, end.y, easedT)
                )
                is Spacing -> {
                    val target = anim.to as Spacing
                    Spacing(
                        x = lerp(start.x, target.x, easedT),
                        y = lerp(start.y, target.y, easedT)
                    )
                }

                else -> end
            }

            typedSetter(comp, result)
            if (t >= 1.0) iterator.remove()
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float {
        return start + ((end - start) * t).toFloat()
    }

    /**
     * Performs a layout pass, resolving every component's position.
     */
    private fun layout(window: UIWindow) {
        window.components.forEach { component ->
            layoutComponent(component.value, window)
        }
    }

    /**
     * Layout a component and all its children.
     */
    private fun layoutComponent(
        component: Component,
        window: UIWindow,
        parentScale: Vec2 = Vec2(1f, 1f)
    ) {
        // handle textures early so width and height are accurate
        if (component is Sprite) component.resolveTexture()

        when (component) {
            is Group -> {
                component.props.components.forEach { child ->
                    layoutComponent(child, window, component.computedScale)
                }
                return
            }

            is FlowContainer -> {
                val props = component.props
                var cursorX = props.padding.left
                var cursorY = props.padding.top

                // apply inherited scale (very important for nested layouts)
                val effectiveScale = Vec2(
                    props.scale.x * parentScale.x,
                    props.scale.y * parentScale.y
                )
                component.computedScale = effectiveScale

                props.components.forEach { child ->
                    val margin = child.props.margin
                    val x = component.screenX.toFloat()
                    val y = component.screenY.toFloat()

                    when (props.direction) {
                        FlowDirection.HORIZONTAL -> {
                            val childX = x + cursorX + margin.left
                            val childY = y + props.padding.top + margin.top

                            cursorX += child.width() + margin.left + margin.right + props.gap
                            child.screenX = cursorX.toInt()
                            child.screenY = cursorY.toInt()
                        }

                        FlowDirection.VERTICAL -> {
                            val childX = x + props.padding.left + margin.left
                            val childY = y + cursorY + margin.top

                            cursorY += child.height() + margin.top + margin.bottom + props.gap
                            child.screenX = cursorX.toInt()
                            child.screenY = cursorY.toInt()
                        }
                    }

                    layoutComponent(child, window, effectiveScale)
                }
                return
            }

            else -> {
                val effectiveScale = Vec2(
                    component.props.scale.x * parentScale.x,
                    component.props.scale.y * parentScale.y
                )
                component.computedScale = effectiveScale

                resolvePosition(component, window)
            }
        }
    }


    /**
     * Manipulate the matrices of the component to apply scale and rotation.
     */
    fun applyComponentMatrices(context: DrawContext, component: Component) {
        context.matrices.pushMatrix()
        val props = component.props
        val scale = props.scale
        val rotation = props.rotation

        val width = component.width()
        val height = component.height()
        val pivotX = width / 2f
        val pivotY = height / 2f

        context.matrices.translate(component.screenX.toFloat(), component.screenY.toFloat())
        context.matrices.translate(pivotX, pivotY)
        context.matrices.scale(scale.x, scale.y)
        context.matrices.mul(Matrix3x2f().rotation(rotation.toFloat()))
        context.matrices.translate(-pivotX, -pivotY)
    }

    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: Component, window: UIWindow) {
        // TODO: cleanup
        val screenWidth = MinecraftClient.getInstance().window.scaledWidth
        val screenHeight = MinecraftClient.getInstance().window.scaledHeight

        val width = component.width().takeIf { it > 0 } ?: component.props.size.x
        val height = component.height().takeIf { it > 0 } ?: component.props.size.y

        // default relative positioning: the screen
        var baseX = component.props.pos.x
        var baseY = component.props.pos.y
        var baseWidth = screenWidth.toFloat()
        var baseHeight = screenHeight.toFloat()

        // if the component is relative to another, use that component's bounds as the base
        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId)
            if (relativeTo != null) {
                baseX = relativeTo.screenX.toFloat()
                baseY = relativeTo.screenY.toFloat()
                baseWidth = relativeTo.width()
                baseHeight = relativeTo.height()
            }
        }

        // base anchor logic (works for screen or relative component)
        var resolvedX = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT ->
                baseX + component.props.pos.x
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER ->
                baseX + baseWidth / 2f + component.props.pos.x - width / 2f
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT ->
                baseX + baseWidth - component.props.pos.x - width
        }

        var resolvedY = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT ->
                baseY + component.props.pos.y
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT ->
                baseY + baseHeight / 2f + component.props.pos.y - height / 2f
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT ->
                baseY + baseHeight - component.props.pos.y - height
        }

        // apply relative positioning (overrides anchor)
        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId) ?: return@let
            when (component.relativePosition) {
                RelativePosition.RIGHT_OF -> {
                    resolvedX = relativeTo.screenX + relativeTo.width() + component.props.pos.x + relativeTo.props.margin.x
                    resolvedY = relativeTo.screenY + component.props.pos.y + (component.height() / 2)
                }
                RelativePosition.LEFT_OF -> {
                    resolvedX = relativeTo.screenX - width + component.props.pos.x - relativeTo.props.margin.x
                    resolvedY = relativeTo.screenY + component.props.pos.y + (component.height() / 2)
                }
                RelativePosition.BELOW -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY + relativeTo.height() + component.props.pos.y + relativeTo.props.margin.y
                }
                RelativePosition.ABOVE -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY - height + component.props.pos.y - relativeTo.props.margin.y
                }
                else -> {}
            }
        }

        // apply margins (inward or outward depending on anchor)
        val margin = component.props.margin

        when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT ->
                resolvedX += margin.left
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER ->
                resolvedX += margin.x
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT ->
                resolvedX -= margin.right
        }

        when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT ->
                resolvedY += margin.top
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT ->
                resolvedY += margin.y
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT ->
                resolvedY -= margin.bottom
        }


        component.screenX = resolvedX.toInt()
        component.screenY = resolvedY.toInt()
    }

    fun getTexture(path: String): NativeImageBackedTexture? {
        val correctedPath = if (path.endsWith(".png")) path else "$path.png"
        val id = if (':' in correctedPath) {
            Identifier.of(correctedPath)
        } else {
            Identifier.of("minecraft", correctedPath)
        }

        return try {
            val resourceManager = MinecraftClient.getInstance().resourceManager
            val resourceOptional = resourceManager.getResource(id)

            if (resourceOptional.isEmpty) return null

            val resource: Resource = resourceOptional.get()
            val image = NativeImage.read(resource.inputStream)
            NativeImageBackedTexture({ "texture_$correctedPath" }, image)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getIdentifier(path: String): Identifier {
        return if (':' in path) {
            Identifier.of(path)
        } else {
            Identifier.of("minecraft", path)
        }
    }
}

/**
 * Resolve the sprite's texture before we run any logic.
 * This resolves relative positioning issues, since the server does
 * not know what the height and width of the image is, since we do not
 * know what texture pack the player is using.
 */
fun Sprite.resolveTexture() {
    if (props.texturePath != "") {
        val texture = UIRenderer.getTexture(props.texturePath)
        texture?.let {
            computedSize = Vec2(
                it.image?.width?.toFloat() ?: 0f,
                it.image?.height?.toFloat() ?: 0f
            )
        }
    }
}

fun Component.draw(context: DrawContext) {
    UIRenderer.componentRenderer.draw(
        this,
        context,
        MinecraftClient.getInstance()
    )
}
