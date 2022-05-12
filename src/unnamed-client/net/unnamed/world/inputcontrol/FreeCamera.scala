package net.unnamed.world.inputcontrol

import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.MovementInputFromOptions
import net.unnamed.Unnamed
import net.unnamed.utils.common.ifce.{ClientInstance, MinecraftInstance}

class FreeCamera
  extends EntityPlayer(Minecraft.getMinecraft.theWorld,
    Minecraft.getMinecraft.thePlayer.getGameProfile) with MinecraftInstance with ClientInstance {

  {
    clonePlayer(mc.thePlayer, true)
    copyLocationAndAnglesFrom(mc.thePlayer)

    Unnamed.clientSideWorld.get.addClientEntity(this)

    capabilities.isFlying = true

  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
   * use this to react to sunlight and start to burn.
   */
  override def onLivingUpdate(): Unit = {
    moveFlying(moveStrafing, moveForward, 0.1F)
    moveEntity(motionX, motionY, motionZ)
    motionX *= .8
    motionY *= .8
    motionZ *= .8
  }

  /**
   * Returns whether the entity is in a server world
   */


  override def moveFlying(strafe: Float, forward: Float, friction: Float): Unit = {

    var inputStrafe = strafe
    var inputForward = forward

    if (clientSideWorld.cameras.activeCamera.isDefined && clientSideWorld.cameras.activeCamera.get == this) {
      val inputControl = new MovementInputFromOptions(mc.gameSettings)
      inputControl.updatePlayerMoveState()

      inputStrafe = inputControl.moveStrafe
      inputForward = inputControl.moveForward


      if (inputControl.sneak)
        motionY -= capabilities.getFlySpeed / 2

      if (inputControl.jump)
        motionY += capabilities.getFlySpeed / 2
    }

    super.moveFlying(inputStrafe, inputForward, capabilities.getFlySpeed)
  }

  /**
   * Returns true if the player is in spectator mode.
   */
  override def isSpectator: Boolean = true
}
