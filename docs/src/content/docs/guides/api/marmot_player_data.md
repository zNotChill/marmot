---
title: Marmot Player Data
description: Check out a player's Marmot data.
---

In Marmot, the server-side API provides an automatically handled object detailing the player's client status.

For example:
```kt
// returns true if the handshake packet was successfully returned upon joining
player.marmot?.isMarmot

// returns true if the player is holding left click
player.marmot?.holdingLeftClick

// returns true if the player is holding right click
player.marmot?.holdingRightClick

// returns the player's currently rendered UI, or null
player.marmot?.currentWindow

// returns the player's last rendered UI, or null
player.marmot?.previousWindow
```
All of these properties are handled automatically by the server, and should only be gotten, never set.

The player's Marmot object **CAN** be null, and will be if:
- the player is not using Marmot.
- the player's client does not respond to a handshake packet.
- the server does not send a handshake packet.

In most cases, the server handles the last two as long as you set it up according to the Get Started guide for your respective server platform.
The client (if running Marmot) will always do its best to respond to the handshake packet.
