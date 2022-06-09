package net.unnamed.camera

import net.minecraft.client.settings.GameSettings
import net.minecraft.util.MovementInputFromOptions
import net.unnamed.Unnamed
import net.unnamed.schedule.Scheduler
import net.unnamed.schedule.resource.PlayerControllableAction

class PlayerMovementControl(val gameSettings: GameSettings) extends MovementInputFromOptions(gameSettings) {

  override def updatePlayerMoveState(): Unit = {
    if (Scheduler.isManagedByScheduler(PlayerControllableAction.MOVEMENT_INPUT)) {
      val movementInput = Scheduler.getPlayerMovementInput
      moveForward = movementInput.moveForward
      moveStrafe = movementInput.moveStrafe
      jump = movementInput.jump
      sneak = movementInput.sneak
      return
    }
    else if (Cameras.activeCamera.isDefined)
      return

    super.updatePlayerMoveState()
  }
}
