package net.unnamed.cheats.packet.modification

import net.unnamed.event.unnamed.PlayerGotKnockedBackEvent
import net.unnamed.utils.common.ifce.MinecraftInstance

import javax.vecmath.Vector2d


case object Velocity extends MinecraftInstance {

  def cancelPlayerKnockBackEvent(event: PlayerGotKnockedBackEvent): Unit = {
    event.cancel()
  }

  def modifyKnockBackBy(event: PlayerGotKnockedBackEvent)(byVertical: Double)(byHorizontal: Vector2d): Unit = {
    val packet = event.packet

    packet.motionY = (packet.motionY * byVertical).toInt

    packet.motionX = (packet.motionX * byHorizontal.x).toInt
    packet.motionZ = (packet.motionZ * byHorizontal.y).toInt
  }

}
