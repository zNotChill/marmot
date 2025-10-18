---
title: Lines
description: Render UI lines!
---

## Lines

In Marmot, you can create a UI Line, which displays a straight line.
As of now, UI lines are **only** straight lines from one point to another. At some point, we may add multipoint paths.

Example:
```kt
line("line") {
    from = Vec2(-20f, 0f)
    to = Vec2(20f, 0f)
    pointSize = Vec2(2f, 2f) // the size of each sample point in the line
    pos = Vec2(0f, 20f)
    anchor = Anchor.BOTTOM_CENTER
    color = UIColor(255, 0, 0)
}
```

This should result in a red line directly above the hotbar.