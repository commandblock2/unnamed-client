package net.unnamed.modules.movement

import net.unnamed.event.vanilla.tick.PlayerLivingUpdateBeginEvent
import net.unnamed.modules.Module
import net.unnamed.settings.setting.BoolSetting

case object Sprint extends Module {
  val allDirection = new BoolSetting(false)

  onEvent((_: PlayerLivingUpdateBeginEvent) =>
    if (allDirection.getValue)
      mc.thePlayer.setSprinting(true))
}
