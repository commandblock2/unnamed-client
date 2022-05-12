package net.unnamed.modules.render

import net.unnamed.modules.Module
import net.unnamed.settings.setting.FloatSetting

case object AntiBlind extends Module {
  val alpha = new FloatSetting(.2f)
}
