---
title: Keybinds
description: Send forced keybinds to a player.
---

In Marmot, you can send a forced keybinds packet to a player or audience.

In Kotlin, it can look something like this:
```kt
MarmotAPI.sendKeybinds(
  event.player,
  mapOf(
    "key.advancements" to "key.keyboard.y"
  )
)
```

You can send an infinite amount of keybinds, but be careful with that.
The client will automatically reject any invalid keybinds.

| ‼️ | The client expects a Keybind translation key (e.g: `key.advancements`) to be mapped to a keyboard key (e.g: `key.keyboard.y`).
|-|-|

| ℹ️ | You can find a full list of the Minecraft translation keys [here](https://gist.github.com/sppmacd/82af47c83b225d4ffd33bb0c27b0d932).
|-|-|