package me.znotchill.marmot.minestom

import me.znotchill.blossom.command.command
import me.znotchill.blossom.component.component
import me.znotchill.blossom.extensions.addListener
import me.znotchill.blossom.extensions.seconds
import me.znotchill.blossom.scheduler.task
import me.znotchill.blossom.server.BlossomServer
import me.znotchill.marmot.common.ClientPerspective
import me.znotchill.marmot.common.api.MarmotEvent
import me.znotchill.marmot.common.classes.FovOp
import me.znotchill.marmot.minestom.api.MarmotAPI
import me.znotchill.marmot.minestom.api.extensions.adjustCamera
import me.znotchill.marmot.minestom.api.extensions.adjustCameraOffset
import me.znotchill.marmot.minestom.api.extensions.configureMouse
import me.znotchill.marmot.minestom.api.extensions.handshake
import me.znotchill.marmot.minestom.api.extensions.lockCamera
import me.znotchill.marmot.minestom.api.extensions.lockPerspective
import me.znotchill.marmot.minestom.api.extensions.openUI
import me.znotchill.marmot.minestom.api.extensions.setPerspective
import me.znotchill.marmot.minestom.uis.MapChangeUI
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.event.player.PlayerLoadedEvent
import net.minestom.server.instance.InstanceContainer

private class Server : BlossomServer(
    "Marmot-Server",
    auth = false
) {
    lateinit var instanceContainer: InstanceContainer

    override fun preLoad() {
        MarmotAPI.registerEvents(eventHandler)
        MarmotAPI.registerTasks(scheduler)
        MarmotAPI.debuggingEnabled = true

        MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_BEGIN) { player ->
            player.sendMessage("began left click")
        }
        MarmotAPI.addEvent(MarmotEvent.LEFT_CLICK_END) { player ->
            player.sendMessage("stopped left click")
        }
        MarmotAPI.addEvent(MarmotEvent.RIGHT_CLICK_BEGIN) { player ->
            player.sendMessage("began right click")
        }
        MarmotAPI.addEvent(MarmotEvent.RIGHT_CLICK_END) { player ->
            player.sendMessage("stopped right click")
        }

        instanceContainer = BaseInstance().createInstance(instanceManager)

        eventHandler.addListener<PlayerLoadedEvent> { event ->
            MarmotAPI.sendKeybinds(
                event.player,
                mapOf(
                    "key.advancements" to "key.keyboard.y",
                )
            )
        }

        eventHandler.addListener<AsyncPlayerConfigurationEvent> { event ->
            val player = event.player
            event.spawningInstance = instanceContainer
            player.gameMode = GameMode.ADVENTURE
            player.permissionLevel = 4
        }

        registerCommand(
            command("lockcamera") {
                val lock = argument<Boolean>("lock")
                syntax(lock) { lockBool ->
                    lockCamera(lockBool)
                }
            }
        )

        registerCommand(
            command("lockmouse") {
                val lock = argument<Boolean>("lock")
                val emit = argument<Boolean>("emit")
                syntax(lock, emit) { lockBool, emitBool ->
                    configureMouse(lockBool, emitBool)
                }
            }
        )

        registerCommand(
            command("camera") {
                val pitch = argument<Float>("pitch")
                val yaw = argument<Float>("yaw")
                val roll = argument<Float>("roll")
                val fov = argument<Float>("fov")
                syntax(pitch, yaw, roll, fov) { pitchFloat, yawFloat, rollFloat, fovFloat ->
                    adjustCamera(
                        pitchFloat,
                        yawFloat,
                        rollFloat,
                        fovFloat,
                        lockFov = true,
                        animateFov = true,
                        fovOp = FovOp.MUL,
                        fovAnimTicks = 20
                    )
                    scheduler.task {
                        run = { task ->
                            adjustCamera(
                                pitchFloat,
                                yawFloat,
                                rollFloat,
                                fovFloat,
                                lockFov = true,
                                animateFov = true,
                                fovOp = FovOp.RESET,
                                fovAnimTicks = 20
                            )
                        }
                        delay = 2.seconds
                    }
                }
            }
        )

        registerCommand(
            command("fov") {
                val lockBool = argument<Boolean>("lock")
                val animateBool = argument<Boolean>("animate")
                syntax(lockBool, animateBool) { lock, animate ->
                    adjustCamera(lockFov = lock, animateFov = animate)
                }
            }
        )

        registerCommand(
            command("camera_offset") {
                val xArg = argument<Float>("x")
                val yArg = argument<Float>("y")
                val zArg = argument<Float>("z")
                syntax(xArg, yArg, zArg) { x, y, z ->
                    adjustCameraOffset(x, y, z)
                }
            }
        )

        registerCommand(
            command("sendhealth") {
                syntax {
                    handshake()
                }
            }
        )

        registerCommand(
            command("perspective") {
                syntax {
                    setPerspective(ClientPerspective.THIRD_PERSON_BACK)
                }
            }
        )

        registerCommand(
            command("lockperspective") {
                val lock = argument<Boolean>("lock")
                syntax(lock) { lockBool ->
                    lockPerspective(lockBool)
                }
            }
        )

        registerCommand(
            command("ismarmot") {
                val playerName = argument<String>("player")
                syntax(playerName) { nameStr ->
                    val player = players.find { it.username == nameStr }
                    if (player == null) return@syntax

                    val isMarmot = MarmotAPI.isUsingMarmot(player)

                    if (isMarmot) {
                        val message = component {
                            text("${player.username} is using Marmot.")
                        }
                        sendMessage(message)
                    } else {
                        val message = component {
                            text("${player.username} is not using Marmot.")
                        }
                        sendMessage(message)
                    }
                }
            }
        )

        val ui = MapChangeUI()
        registerCommand(
            command("ui") {
                syntax {
                    ui.new(players = players.toList())
                    players.forEach { player ->
                        player.openUI(ui)
                    }
                }
            }
        )
    }

}

fun main() {
    Server().start()
}