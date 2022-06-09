package net.unnamed.camera

import net.minecraft.entity.Entity
import net.unnamed.utils.common.ifce.MinecraftInstance

object Cameras extends MinecraftInstance {
  var cameras: Set[Entity] = Set()

  var activeCamera: Option[Entity] = None

  def clear(): Unit = {
    cameras = Set()
    activeCamera = None
  }

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
    activeCamera.isDefined &&
      !mc.thePlayer.getEntityBoundingBox.isVecInside(activeCamera.get.getPositionEyes(partialTicks))
  }
}
