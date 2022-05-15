package net.unnamed.world.inputcontrol

import net.minecraft.entity.Entity
import net.unnamed.utils.common.ifce.MinecraftInstance

class Cameras extends MinecraftInstance {
  var cameras: Set[Entity] = Set()

  var activeCamera: Option[Entity] = None

  def setActiveCamera(entity: Option[Entity]): Unit = {
    if (entity.isDefined) {
      cameras += entity.get
    }
    activeCamera = entity
  }

  def getPlayerOrSupposedToBeViewEntity: Entity = {
    if (activeCamera.isDefined)
      mc.thePlayer
    else
      this.mc.getRenderViewEntity
  }

  def shouldRenderThePlayer(partialTicks: Float): Boolean = {
    activeCamera.isDefined && !mc.thePlayer.getEntityBoundingBox.isVecInside(activeCamera.get.getPositionEyes(partialTicks))
  }
}
