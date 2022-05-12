package net.unnamed.world.inputcontrol

import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.unnamed.utils.common.ifce.{ClientInstance, MinecraftInstance}

case class CameraFixedOnEntity(entity: Entity) extends EntityPlayer(Minecraft.getMinecraft.theWorld,
Minecraft.getMinecraft.thePlayer.getGameProfile) with MinecraftInstance with ClientInstance {

  override def onLivingUpdate(): Unit = {
    super.onLivingUpdate()
    setPosition(entity.posX, entity.posY, entity.posZ)
  }

  override def isSpectator: Boolean = true
}
