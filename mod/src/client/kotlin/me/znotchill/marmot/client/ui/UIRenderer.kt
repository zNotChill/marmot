package me.znotchill.marmot.client.ui

import me.znotchill.marmot.common.ui.*
import me.znotchill.marmot.common.ui.classes.Easing
import me.znotchill.marmot.common.ui.classes.RelativePosition
import me.znotchill.marmot.common.ui.classes.Vec2
import me.znotchill.marmot.common.ui.components.Component
import me.znotchill.marmot.common.ui.components.GroupComponent
import me.znotchill.marmot.common.ui.components.SpriteComponent
import me.znotchill.marmot.common.ui.components.TextComponent
import me.znotchill.marmot.common.ui.events.DestroyEvent
import me.znotchill.marmot.common.ui.events.MoveEvent
import me.znotchill.marmot.common.ui.events.OpacityEvent
import me.znotchill.marmot.common.ui.events.PropertyAnimation
import me.znotchill.marmot.common.ui.events.UIEvent
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.resource.Resource
import net.minecraft.util.Identifier
import java.io.IOException

object UIRenderer {
    val textureCache = mutableMapOf<String, NativeImageBackedTexture>()
    private var currentWindow: UIWindow? = null
    private val activeAnimations = mutableListOf<PropertyAnimation<Any>>()
    private var lastTime: Long = System.nanoTime()

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
                window.components.values.forEach { component ->
                    component.draw(context)
                }
            }
        }
    }

    fun applyEvent(event: UIEvent) {
        val comp = currentWindow?.getComponentByIdDeep(event.targetId) ?: return

        when (event) {
            is MoveEvent -> {
                val anim = PropertyAnimation(
                    targetId = event.targetId,
                    getter = { comp.props.pos },
                    setter = { c, value -> c.props.pos = value },
                    from = null,
                    to = event.to,
                    durationSeconds = event.durationSeconds,
                    easing = event.easing.toString()
                )
                anim.window = event.window
                enqueueAnimation(anim)
            }
            is OpacityEvent -> {
                val anim = PropertyAnimation(
                    targetId = event.targetId,
                    getter = { comp.props.opacity },
                    setter = { c, value -> c.props.opacity = value },
                    from = null,
                    to = event.opacity,
                    durationSeconds = event.durationSeconds,
                    easing = event.easing.toString()
                )
                anim.window = event.window
                enqueueAnimation(anim)
            }

            is PropertyAnimation<*> -> {
                enqueueAnimation(event)
            }

            is DestroyEvent -> {
                currentWindow?.components?.remove(event.targetId)
            }

            else -> {
                println("Unknown event type: $event")
            }
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
        return when (easing) {
            Easing.LINEAR -> t
            Easing.EASE_IN -> t * t
            Easing.EASE_OUT -> 1 - (1 - t) * (1 - t)
            Easing.EASE_IN_OUT -> if (t < 0.5) 2 * t * t
                else 1 - 2 * (1 - t) * (1 - t)
            else -> t
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

private fun Component.draw(context: DrawContext) {
    when (this) {
        is TextComponent -> {
            val renderer = MinecraftClient.getInstance().textRenderer

            props.backgroundColor?.let { bg ->
                context.fill(screenX, screenY, screenX + this.width(), screenY + this.height(), bg.toArgb())
            }

            context.matrices.pushMatrix()

            val scale = props.scale

            context.matrices.scale(scale.x, scale.y)

            context.matrices.translate(
                (screenX + props.padding.left) / scale.x,
                (screenY + props.padding.top) / scale.y
            )

            context.drawText(
                renderer,
                props.text,
                0,
                0,
                props.color.copy(a = (props.opacity * 255).toInt()).toArgb(),
                props.shadow
            )

            context.matrices.popMatrix()
        }

        is SpriteComponent -> {
            val texture = UIRenderer.getIdentifier(props.texturePath)
            val texWidth = computedSize?.x?.toInt() ?: 16
            val texHeight = computedSize?.y?.toInt() ?: 16

            var drawWidth = props.size.x.toInt()
            if (drawWidth == 0)
                drawWidth = texWidth

            var drawHeight = props.size.y.toInt()
            if (drawHeight == 0)
                drawHeight = texHeight

            val alpha = props.opacity / 255f

            context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                texture,
                screenX,
                screenY,
                0f, 0f,
                drawWidth, drawHeight,
                drawWidth, drawHeight,
            )
        }

        is GroupComponent -> {
            props.backgroundColor?.let { bg ->
                val components = props.components
                val minX = components.minOfOrNull { it.screenX } ?: 0
                val maxX = components.maxOfOrNull { it.screenX + it.width() } ?: 0
                val minY = components.minOfOrNull { it.screenY } ?: 0
                val maxY = components.maxOfOrNull { it.screenY + it.height() } ?: 0

                context.fill(
                    minX - props.padding.left,
                    minY - props.padding.top,
                    maxX + props.padding.right,
                    maxY + props.padding.bottom,
                    bg.toArgb()
                )
            }

            props.components.forEach { child -> child.draw(context) }
        }

        else -> {}
    }
}
