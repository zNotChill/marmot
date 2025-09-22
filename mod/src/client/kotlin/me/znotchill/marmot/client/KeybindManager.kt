package me.znotchill.marmot.client

import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

object KeybindManager {
    private val originalKeyMap = mutableMapOf<KeyBinding, InputUtil.Key>()

    fun overrideKeybind(keyBinding: KeyBinding, forcedKey: InputUtil.Key) {
        if (!originalKeyMap.containsKey(keyBinding)) {
            originalKeyMap[keyBinding] = keyBinding.defaultKey as InputUtil.Key
        }

        keyBinding.setBoundKey(forcedKey)
        keyBinding.isPressed = false
    }

    fun restoreAll() {
        originalKeyMap.forEach { (keyBinding, originalKey) ->
            keyBinding.setBoundKey(originalKey)
            keyBinding.isPressed = false
            println("restored $keyBinding back to $originalKey")
        }
        originalKeyMap.clear()
        KeyBinding.updateKeysByCode()
    }
}