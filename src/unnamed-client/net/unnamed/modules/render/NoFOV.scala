package net.unnamed.modules.render

import net.unnamed.modules.Module
import net.unnamed.settings.setting.{FloatSetting, IntegerSetting}

case object NoFOV extends Module {
  val fov = new FloatSetting(1)
}
