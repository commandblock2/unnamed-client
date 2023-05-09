package net.unnamed.modules.combat

import net.unnamed.event.vanilla.Render3DEvent
import net.unnamed.modules.Module
import net.unnamed.settings.group.CPSSettingGroup
import net.unnamed.settings.setting.EntitySelectorSetting
import org.lwjgl.glfw.GLFW
import org.lwjglx.input.Mouse

case object TriggerBot
  extends Module with CPSSettingGroup {
  val entitySelector = new EntitySelectorSetting()

  onEvent((_: Render3DEvent) => {
    // TODO: better logic for clicking
    if (
      mc.currentScreen == null &&
      mc.objectMouseOver != null &&
      mc.objectMouseOver.entityHit != null &&
      shouldClick()) {
      Mouse.addButtonEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT, true)
      Mouse.addButtonEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT, false)
      clicked()
    }
  })
}
