package net.unnamed.modules.combat

import net.minecraft.init.Items
import net.minecraft.network.play.server.S06PacketUpdateHealth
import net.unnamed.event.EventBus
import net.unnamed.event.vanilla.PacketEvent
import net.unnamed.event.vanilla.tick.PlayerLivingUpdateBeginEvent
import net.unnamed.modules.Module
import net.unnamed.settings.setting.{BoolSetting, FloatSetting}
import net.unnamed.utils.InventoryUtils

case object AutoSoup extends Module {
  val triggerHealth = new FloatSetting(10, 0, 20)

  val eatAtNextTick = new BoolSetting(false)

  val dropBowl = new BoolSetting(false)
  val dropAtNextTick = new BoolSetting(true)

  val returnNextTick = new BoolSetting(false)

  onEvent((packetEvent: PacketEvent) => {
    packetEvent.packet match {
      case healthPacket: S06PacketUpdateHealth =>

      case _ =>
    }
  })

}
