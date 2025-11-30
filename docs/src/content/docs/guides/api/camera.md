---
title: Camera
description: Adjust the player's camera.
---

In Marmot, you can adjust a player or audience's camera, to control their pitch, yaw, roll, and FOV.

If any fields are left empty:
- `pitch` will default to -1 (don't change from the client's current pitch)
- `yaw` will default to -1 (don't change from the client's current yaw)
- `roll` will default to -1 (don't change from the client's current roll)
- `fov` will default to -1 (don't change from the client's current fov)
- `fovOp` will default to `0` (`FovOp.SET`)
  - Options: `SET`, `ADD`, `DIV`, `MUL`, `RESET`
- `fovAnimTicks` will default to `5`
- `lockFov` will default to false
- `animateFov` will default to false

In Kotlin, it looks like this:
```kt
player.adjustCamera(
  pitch = 0,
  yaw = 0, 
  roll = 15, // degrees, 360 for one revolution
  fov = 90,
  fovOp = FovOp.SET,
  fovAnimTicks = 2, // how many ticks to spend interpolating/animating the FOV
  lockFov = true, // false grants control back to vanilla/zoom mods
  animateFov = true // whether to interpolate the FOV change or immediately apply it
)
```