---
title: Camera Lock
description: Lock the player's camera.
---

In Marmot, you can lock a player or audience's camera from moving.

In Kotlin, it looks like this:
```kt
player.lockCamera(true) // lock
player.lockCamera(false) // unlock

audience.lockCamera(true)
audience.lockCamera(false)
```