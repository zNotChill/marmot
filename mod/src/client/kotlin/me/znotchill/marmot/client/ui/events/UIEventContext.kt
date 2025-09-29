package me.znotchill.marmot.client.ui.events

import me.znotchill.marmot.common.ui.UIWindow
import me.znotchill.marmot.common.ui.events.PropertyAnimation

class UIEventContext(
    val currentWindow: () -> UIWindow?,
    val enqueueAnimation: (PropertyAnimation<*>) -> Unit
)