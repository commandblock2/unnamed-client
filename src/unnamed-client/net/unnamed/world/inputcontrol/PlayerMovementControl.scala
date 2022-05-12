package net.unnamed.world.inputcontrol

import net.minecraft.client.settings.GameSettings
import net.minecraft.util.MovementInputFromOptions
import net.unnamed.Unnamed
import net.unnamed.world.schedule.Scheduler

class PlayerMovementControl(val gameSettings: GameSettings) extends MovementInputFromOptions(gameSettings) {

  override def updatePlayerMoveState(): Unit = {
    if (Scheduler.isPlayerControlledByScheduler) {
      val movementInput = Scheduler.getPlayerMovementInput
      moveForward = movementInput.moveForward
      moveStrafe = movementInput.moveStrafe
      jump = movementInput.jump
      sneak = movementInput.sneak
      return
    }
    else if (Unnamed.clientSideWorld.get.cameras.activeCamera.isDefined)
      return

    super.updatePlayerMoveState()
  }
}
