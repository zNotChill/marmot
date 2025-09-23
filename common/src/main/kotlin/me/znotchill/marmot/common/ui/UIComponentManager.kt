package me.znotchill.marmot.common.ui

class UIComponentManager(
    val component: UIComponent
) {
    fun move(x: Int, y: Int) {
        component.x = x
        component.y = y
    }
}