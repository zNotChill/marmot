package me.znotchill.marmot.client.ui

import me.znotchill.marmot.client.MarmotClient
import me.znotchill.marmot.client.ui.components.GradientRenderer
import me.znotchill.marmot.client.ui.components.GroupRenderer
import me.znotchill.marmot.client.ui.components.LineRenderer
import me.znotchill.marmot.client.ui.components.SpriteRenderer
import me.znotchill.marmot.client.ui.components.TextRenderer
import me.znotchill.marmot.client.ui.events.DestroyEventHandler
import me.znotchill.marmot.client.ui.events.MoveEventHandler
import me.znotchill.marmot.client.ui.events.OpacityEventHandler
import me.znotchill.marmot.client.ui.events.RotateEventHandler
import me.znotchill.marmot.client.ui.events.UIEventContext
import me.znotchill.marmot.common.ui.*
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GradientComponent
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.components.LineComponent
import me.znotchill.marmot.common.ui.components.SpriteComponent
import me.znotchill.marmot.common.ui.components.TextComponent
import me.znotchill.marmot.common.ui.events.DestroyEvent
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.OpacityEvent
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
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object UIRenderer {
    private var currentWindow: UIWindow? = null
    private val activeAnimations = mutableListOf<PropertyAnimation<Any>>()
    private var lastTime: Long = System.nanoTime()

    // TODO: improve this system somehow
    // if it cannot be done, find a way to remove the ::class.java
    val eventDispatcher = UIEventDispatcher(
        handlers = mapOf(
            MoveEvent::class.java to MoveEventHandler(),
            OpacityEvent::class.java to OpacityEventHandler(),
            DestroyEvent::class.java to DestroyEventHandler(),
            RotateEvent::class.java to RotateEventHandler(),
        ),
        context = UIEventContext(
            currentWindow = { currentWindow },
            enqueueAnimation = { anim -> enqueueAnimation(anim) }
        )
    )

    val componentRenderer = UIComponentRenderer(
        handlers = mapOf(
            TextComponent::class.java to TextRenderer(),
            SpriteComponent::class.java to SpriteRenderer(),
            GroupComponent::class.java to GroupRenderer(),
            LineComponent::class.java to LineRenderer(),
            GradientComponent::class.java to GradientRenderer()
        )
    )

    fun setWindow(window: UIWindow?) {
        currentWindow = window
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

    fun <T> enqueueAnimation(event: PropertyAnimation<T>) {
        val comp = currentWindow?.getComponentByIdDeep(event.targetId)
        if (comp != null && event.from == null) {
            @Suppress("UNCHECKED_CAST")
            event.from = event.getter(comp)
        }
        @Suppress("UNCHECKED_CAST")
        activeAnimations += event as PropertyAnimation<Any>
    }

    fun tickAnimations(deltaSeconds: Double) {
        val iterator = activeAnimations.iterator()
        while (iterator.hasNext()) {
            val anim = iterator.next()

            val comp = currentWindow?.getComponentByIdDeep(anim.targetId) ?: continue
            val start = anim.from ?: anim.getter(comp)

            anim.elapsed += deltaSeconds
            val t = (anim.elapsed / anim.durationSeconds).coerceIn(0.0, 1.0)
            val easedT = applyEasing(t, Easing.valueOf(anim.easing))

            @Suppress("UNCHECKED_CAST")
            when (start) {
                is Float -> (anim as PropertyAnimation<Float>).setter(comp, lerp(start, anim.to, easedT))
                is Int -> (anim as PropertyAnimation<Int>).setter(comp, lerp(start.toFloat(), anim.to.toFloat(), easedT).toInt())
                is Vec2 -> {
                    val target = anim.to as Vec2
                    val result = Vec2(
                        lerp(start.x, target.x, easedT),
                        lerp(start.y, target.y, easedT)
                    )
                    (anim as PropertyAnimation<Vec2>).setter(comp, result)
                }
                else -> anim.setter(comp, anim.to)
            }

            if (t >= 1.0) iterator.remove()
        }
    }

    fun lerp(start: Float, end: Float, t: Double): Float {
        return start + ((end - start) * t).toFloat()
    }

    fun applyEasing(t: Double, easing: Easing): Double {
        val c1 = 1.70158
        val c2 = c1 * 1.525
        val c3 = c1 + 1
        val c4 = (2 * Math.PI) / 3
        val c5 = (2 * Math.PI) / 4.5

        return when (easing) {
            Easing.LINEAR -> t

            Easing.EASE_IN -> t * t
            Easing.EASE_OUT -> 1 - (1 - t) * (1 - t)
            Easing.EASE_IN_OUT -> if (t < 0.5) 2 * t * t else 1 - (-2 * t + 2).pow(2.0) / 2

            Easing.EASE_IN_CUBIC -> t * t * t
            Easing.EASE_OUT_CUBIC -> 1 - (1 - t).pow(3.0)
            Easing.EASE_IN_OUT_CUBIC ->
                if (t < 0.5) 4 * t * t * t else 1 - (-2 * t + 2).pow(3.0) / 2

            Easing.EASE_IN_QUINT -> t * t * t * t * t
            Easing.EASE_OUT_QUINT -> 1 - (1 - t).pow(5.0)
            Easing.EASE_IN_OUT_QUINT ->
                if (t < 0.5) 16 * t * t * t * t * t else 1 - (-2 * t + 2).pow(5.0) / 2

            Easing.EASE_IN_BACK -> c3 * t * t * t - c1 * t * t
            Easing.EASE_OUT_BACK -> 1 + c3 * (t - 1).pow(3.0) + c1 * (t - 1).pow(2.0)
            Easing.EASE_IN_OUT_BACK ->
                if (t < 0.5)
                    ((2 * t).pow(2.0) * ((c2 + 1) * 2 * t - c2)) / 2
                else
                    ((2 * t - 2).pow(2.0) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2

            Easing.EASE_IN_ELASTIC ->
                when (t) {
                    0.0 -> 0.0
                    1.0 -> 1.0
                    else -> (-2.0).pow(10 * t - 10) * sin((t * 10 - 10.75) * c4)
                }
            Easing.EASE_OUT_ELASTIC ->
                when (t) {
                    0.0 -> 0.0
                    1.0 -> 1.0
                    else -> 2.0.pow(-10 * t) * sin((t * 10 - 0.75) * c4) + 1
                }
            Easing.EASE_IN_OUT_ELASTIC ->
                if (t == 0.0) 0.0 else if (t == 1.0) 1.0
                else if (t < 0.5)
                    -(2.0.pow(20 * t - 10) * sin((20 * t - 11.125) * c5)) / 2
                else
                    (2.0.pow(-20 * t + 10) * sin((20 * t - 11.125) * c5)) / 2 + 1

            Easing.EASE_OUT_BOUNCE -> {
                val n1 = 7.5625
                val d1 = 2.75
                when {
                    t < 1 / d1 -> n1 * t * t
                    t < 2 / d1 -> n1 * (t - 1.5 / d1) * (t - 1.5 / d1) + 0.75
                    t < 2.5 / d1 -> n1 * (t - 2.25 / d1) * (t - 2.25 / d1) + 0.9375
                    else -> n1 * (t - 2.625 / d1) * (t - 2.625 / d1) + 0.984375
                }
            }
            Easing.EASE_IN_BOUNCE -> 1 - applyEasing(1 - t, Easing.EASE_OUT_BOUNCE)
            Easing.EASE_IN_OUT_BOUNCE ->
                if (t < 0.5)
                    (1 - applyEasing(1 - 2 * t, Easing.EASE_OUT_BOUNCE)) / 2
                else
                    (1 + applyEasing(2 * t - 1, Easing.EASE_OUT_BOUNCE)) / 2

            Easing.EASE_IN_SINE -> 1 - cos((t * Math.PI) / 2)
            Easing.EASE_OUT_SINE -> sin((t * Math.PI) / 2)
            Easing.EASE_IN_OUT_SINE -> -(cos(Math.PI * t) - 1) / 2

            Easing.EASE_IN_EXPO -> if (t == 0.0) 0.0 else 2.0.pow(10 * t - 10)
            Easing.EASE_OUT_EXPO -> if (t == 1.0) 1.0 else 1 - 2.0.pow(-10 * t)
            Easing.EASE_IN_OUT_EXPO ->
                when {
                    t == 0.0 -> 0.0
                    t == 1.0 -> 1.0
                    t < 0.5 -> 2.0.pow(20 * t - 10) / 2
                    else -> (2 - 2.0.pow(-20 * t + 10)) / 2
                }
        }
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
     * Layout a component.
     */
    private fun layoutComponent(component: Component, window: UIWindow, parentScale: Vec2 = Vec2(1f, 1f)) {
        if (component is SpriteComponent) component.resolveTexture()

        val effectiveScale = Vec2(
            component.props.scale.x * parentScale.x,
            component.props.scale.y * parentScale.y
        )

        component.computedScale = effectiveScale

        resolvePosition(component, window)

        if (component is GroupComponent) {
            component.props.components.forEach { child ->
                layoutComponent(child, window, effectiveScale)
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
        val rotation = component.props.rotation

        val width = component.width().toFloat()
        val height = component.height().toFloat()

        val pivotX = width / 2f
        val pivotY = height / 2f

        context.matrices.translate(
            (component.screenX + props.padding.left).toFloat(),
            (component.screenY + props.padding.top).toFloat(),
        )

        context.matrices.scale(scale.x, scale.y)

        context.matrices.translate(pivotX, pivotY)
        context.matrices.mul(Matrix3x2f().rotation(Math.toRadians(rotation.toDouble()).toFloat()))
        context.matrices.translate(-pivotX, -pivotY)
    }

    /**
     * Resolve absolute screen position for a component.
     */
    private fun resolvePosition(component: Component, window: UIWindow) {
        val screenWidth = MinecraftClient.getInstance().window.scaledWidth
        val screenHeight = MinecraftClient.getInstance().window.scaledHeight

        val width = component.width()
        val height = component.height()

        var resolvedX = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.CENTER_LEFT, Anchor.BOTTOM_LEFT -> component.props.pos.x
            Anchor.TOP_CENTER, Anchor.CENTER_CENTER, Anchor.BOTTOM_CENTER -> (screenWidth / 2f + component.props.pos.x - width / 2f)
            Anchor.TOP_RIGHT, Anchor.CENTER_RIGHT, Anchor.BOTTOM_RIGHT -> (screenWidth - component.props.pos.x - width)
        }

        var resolvedY = when (component.props.anchor) {
            Anchor.TOP_LEFT, Anchor.TOP_CENTER, Anchor.TOP_RIGHT -> component.props.pos.y
            Anchor.CENTER_LEFT, Anchor.CENTER_CENTER, Anchor.CENTER_RIGHT -> (screenHeight / 2f + component.props.pos.y - height / 2f)
            Anchor.BOTTOM_LEFT, Anchor.BOTTOM_CENTER, Anchor.BOTTOM_RIGHT -> (screenHeight - component.props.pos.y - height)
        }

        component.relativeTo?.let { relId ->
            val relativeTo = window.getComponentByIdDeep(relId) ?: return@let
            when (component.relativePosition) {
                RelativePosition.RIGHT_OF -> {
                    resolvedX = relativeTo.screenX + relativeTo.width() + component.props.pos.x
                    resolvedY = relativeTo.screenY + component.props.pos.y
                }
                RelativePosition.LEFT_OF -> {
                    resolvedX = relativeTo.screenX - width + component.props.pos.x
                    resolvedY = relativeTo.screenY + component.props.pos.y
                }
                RelativePosition.BELOW -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY + relativeTo.height() + component.props.pos.y
                }
                RelativePosition.ABOVE -> {
                    resolvedX = relativeTo.screenX + component.props.pos.x
                    resolvedY = relativeTo.screenY - height + component.props.pos.y
                }
                else -> {}
            }
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
fun SpriteComponent.resolveTexture() {
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
