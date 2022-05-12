package net.unnamed.world.schedule

import net.minecraft.util.MovementInput
import net.unnamed.utils.Rotation


object Scheduler {


  def isPlayerControlledByScheduler: Boolean = {
    false
  }

  def getPlayerMovementInput: MovementInput = {
    new MovementInput()
  }

  def getPlayerRotation: Rotation = {
    Rotation.apply(0, 0)
  }
}
