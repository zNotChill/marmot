---
title: Camera
description: Adjust the player's camera.
---

In Marmot, you can adjust a player or audience's camera, to control their pitch, yaw, roll, and FOV.

In Kotlin, it looks like this:
```kt
player.adjustCamera(
  pitch = 0,
  yaw = 0,
  roll = 15, // degrees, 360 for one revolution
  fov = 90
)
```