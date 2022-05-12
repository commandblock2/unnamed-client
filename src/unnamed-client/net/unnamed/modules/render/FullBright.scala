package net.unnamed.modules.render

import net.unnamed.modules.Module

case object FullBright extends Module {

  var originalGama = 0f

  override def onEnable(): Unit = {
    originalGama = mc.gameSettings.gammaSetting
    mc.gameSettings.gammaSetting = 100F
  }

  override def onDisable(): Unit = {
    mc.gameSettings.gammaSetting = originalGama
  }
}
