package net.unnamed.schedule

import net.minecraft.util.MovementInput
import net.unnamed.schedule.resource.PlayerControllableAction.PlayerAction
import net.unnamed.utils.Rotation


object Scheduler {


  


  def isManagedByScheduler(playerAction: PlayerAction): Boolean = {
    false
  }

  def getInventoryOpen: Boolean = {
    false
  }

  def getPlayerItemInUse: Boolean = {
    false
  }

  def getPlayerHotBar: Int = {
    0
  }

  def getPlayerMovementInput: MovementInput = {
    new MovementInput()
  }

  def getPlayerRotation: Rotation = {
    Rotation.apply(0, 0)
  }
}
