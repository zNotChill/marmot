package me.znotchill.marmot.common.api

import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.ui.MarmotUI
import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.UIEvent
import net.kyori.adventure.audience.Audience
import java.util.*

/**
 * A base interface for the Marmot API to provide a unified experience
 * across all platforms, whether it be Paper or Minestom.
 */
interface BaseMarmotAPI<P, M : MarmotPlayer<P>> {
    val players: MutableMap<UUID, M>

    /**
     * Adds an event handler for a [MarmotEvent].
     */
    fun addEvent(event: MarmotEvent, run: (P) -> Unit)

    /**
     * Gets the callback for a given [MarmotEvent].
     * Will return an empty callback object if the [MarmotEvent] does not
     * have any callback registered to it.
     */
    fun getEvent(event: MarmotEvent): (P) -> Unit

    /**
     * Check if an online [P] is using Marmot.
     */
    fun isUsingMarmot(player: P): Boolean
    /**
     * Get the [M] (MarmotPlayer) for a [P] (Player) if they are using Marmot.
     */
    fun getMarmotPlayer(player: P): M?

    fun handshake(player: P)
    fun sendKeybinds(audience: Audience, binds: Map<String, String>)
    fun adjustCamera(audience: Audience, pitch: Float, yaw: Float, roll: Float, fov: Float, lockFov: Boolean, animateFov: Boolean)
    fun adjustCameraOffset(audience: Audience, x: Float, y: Float, z: Float)
    fun lockCamera(audience: Audience, locked: Boolean)
    fun configureMouse(audience: Audience, locked: Boolean, emitEvents: Boolean)
    fun openUI(audience: Audience, marmotUI: MarmotUI)
    fun openUI(audience: Audience, uiWindow: UIWindow)
    fun updateUI(audience: Audience, events: List<UIEvent>)
    fun clearUI(audience: Audience, id: String)
    fun clearUI(audience: Audience, marmotUI: MarmotUI)
    fun clearUI(audience: Audience, uiWindow: UIWindow)
    fun lockPerspective(audience: Audience, locked: Boolean)
    fun setPerspective(audience: Audience, perspective: ClientPerspective)
}