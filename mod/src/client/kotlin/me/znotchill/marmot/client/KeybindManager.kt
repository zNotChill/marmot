package me.znotchill.marmot.client

import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import java.lang.reflect.Field

object KeybindManager {
    private val originalKeyMap = mutableMapOf<KeyBinding, InputUtil.Key>()
    private val boundKeyField: Field = KeyBinding::class.java.getDeclaredField("boundKey").apply {
        isAccessible = true
    }

    fun overrideKeybind(keyBinding: KeyBinding, forcedKey: InputUtil.Key) {
        if (!originalKeyMap.containsKey(keyBinding)) {
            originalKeyMap[keyBinding] = boundKeyField.get(keyBinding) as InputUtil.Key
        }

        boundKeyField.set(keyBinding, forcedKey)
        keyBinding.isPressed = false
    }

    fun restoreAll() {
        originalKeyMap.forEach { (keyBinding, originalKey) ->
            boundKeyField.set(keyBinding, originalKey)
            keyBinding.isPressed = false
            println("restored $keyBinding back to $originalKey")
        }
        originalKeyMap.clear()
        KeyBinding.updateKeysByCode()
    }
}