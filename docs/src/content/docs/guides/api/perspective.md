---
title: Perspective
description: Lock or force a player's perspective.
---

In Marmot, you can choose to lock a player's perspective or force set it.

This method is subject to change in the future.

If you want to lock their current perspective, you can do:
```kt
player.lockPerspective(true)
```
If you want to unlock their perspective, you can do:
```kt
player.lockPerspective(false)
```

If you want to force their perspective, you can do:
```kt
player.setPerspective(ClientPerspective.FIRST_PERSON)
player.setPerspective(ClientPerspective.THIRD_PERSON_BACK)
player.setPerspective(ClientPerspective.THIRD_PERSON_FRONT)
```

You can chain events to set their perspective and then lock it, as shown:
```kt
player.setPerspective(ClientPerspective.THIRD_PERSON_BACK)
player.lockPerspective(true)
```