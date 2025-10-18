---
title: Gradients
description: Render UI gradients!
---

## Gradients

In Marmot, you can create a UI Gradient using two UIColors.
At this moment, gradients do not support custom angles.

Example:
```kt
gradient("gradient") {
    fillScreen = true // size the component to the client's viewport
    anchor = Anchor.TOP_LEFT // anchor the component to the TOP_LEFT, required for fillScreen components
    from = UIColor(255, 255, 255)
    to = UIColor(0, 0, 0)
}
```