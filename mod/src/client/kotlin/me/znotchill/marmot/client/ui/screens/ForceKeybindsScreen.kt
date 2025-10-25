package me.znotchill.marmot.client.ui.screens

import me.znotchill.marmot.client.MarmotClient
import me.znotchill.marmot.client.packets.clientbound.payloads.ForceKeybindsPayload
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.EntryListWidget
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.Text

class ForceKeybindsScreen(
    private val payload: ForceKeybindsPayload,
    private val onAccept: () -> Unit,
    private val onDeny: () -> Unit
) : Screen(Text.literal("Server Keybinds")) {

    private lateinit var keybindList: KeybindListWidget

    override fun init() {
        val buttonWidth = 100
        val buttonHeight = 20
        val padding = 5
        val buttonY = height - buttonHeight - 20

        val listTop = 60
        val listBottom = buttonY - 10
        val rowHeight = textRenderer.fontHeight + 2
        val listHeight = listBottom - listTop

        keybindList = KeybindListWidget(
            width,
            listHeight,
            listTop,
            rowHeight
        )
        payload.binds.forEach { (bindName, keyString) ->
            val keyBinding = client!!.options.allKeys.firstOrNull { it.boundKeyTranslationKey.endsWith(bindName) }
            if (keyBinding != null) {
                keybindList.addKeybind(keyBinding, keyString)
            }
        }

        addSelectableChild(keybindList)

        addDrawableChild(
            ButtonWidget.builder(Text.literal("Accept")) {
                onAccept()
                close()
            }.dimensions(width / 2 - buttonWidth - padding, buttonY, buttonWidth, buttonHeight).build()
        )

        addDrawableChild(
            ButtonWidget.builder(Text.literal("Deny")) {
                onDeny()
                close()
            }.dimensions(width / 2 + padding, buttonY, buttonWidth, buttonHeight).build()
        )
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(context, mouseX, mouseY, delta)

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("This server wants to override your keybinds!"),
            width / 2,
            20,
            0xFFFFFFFF.toInt()
        )
        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("Forced keybinds are only kept while you are on this server."),
            width / 2,
            40,
            0xFFD1D1D1.toInt()
        )

        keybindList.render(context, mouseX, mouseY, delta)
    }

    private inner class KeybindListWidget(
        width: Int,
        height: Int,
        y: Int,
        itemHeight: Int
    ) : EntryListWidget<KeybindListWidget.KeybindEntry>(client, width, height, y, itemHeight) {
        fun addKeybind(bind: KeyBinding, key: String) = addEntry(KeybindEntry(bind, key))

        override fun appendClickableNarrations(builder: NarrationMessageBuilder) {}

        inner class KeybindEntry(val bind: KeyBinding, val key: String) : Entry<KeybindEntry>() {
            override fun render(
                context: DrawContext?,
                mouseX: Int,
                mouseY: Int,
                hovered: Boolean,
                deltaTicks: Float
            ) {
                try {
                    val fontHeight = textRenderer.fontHeight
                    val padding = 5
                    // @TODO test
                    val entryWidth = width - padding * 2
                    val entryHeight = height + padding * 2

                    val bindText = Text.translatable(bind.id)

                    val keyText = InputUtil.fromTranslationKey(key)?.localizedText
                        ?: Text.literal(key)

                    val keyWidth = textRenderer.getWidth(keyText) + padding * 2
                    val keyHeight = fontHeight + padding

                    val keyX = x + entryWidth - keyWidth - 5
                    val keyY = y + (entryHeight - keyHeight) / 2

                    context?.fill(keyX, keyY, keyX + keyWidth, keyY + keyHeight, 0xFF555555.toInt())

                    context?.drawText(
                        textRenderer,
                        keyText,
                        keyX + (keyWidth - textRenderer.getWidth(keyText)) / 2,
                        keyY + (keyHeight - fontHeight) / 2,
                        0xFFFFFFFF.toInt(),
                        false
                    )

                    context?.drawText(
                        textRenderer,
                        bindText,
                        x + 5,
                        y + (entryHeight - fontHeight) / 2,
                        0xFFFFFFFF.toInt(),
                        false
                    )
                } catch (e: NumberFormatException) {
                    MarmotClient.LOGGER.error("Server sent an invalid keybind! ${bind.id} -> $key")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}