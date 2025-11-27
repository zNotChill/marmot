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
- `overrideFov` will default to false
- `animateFov` will default to false

In Kotlin, it looks like this:
```kt
player.adjustCamera(
  pitch = 0,
  yaw = 0, 
  roll = 15, // degrees, 360 for one revolution
  fov = 90,
  lockFov = true, // currently required to change the fov
  animateFov = true // whether to interpolate the fov change or immediately apply it
)
```