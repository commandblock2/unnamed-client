package net.unnamed.utils

import net.minecraft.entity.Entity
import net.minecraft.util.{Vec3, Vector3d}


case object EntityInterpolationUtil {
  def getPartialTicksOffset(entity: Entity)(partialTicks: Float): Vec3 = {
    val scale = partialTicks - 1
    new Vec3(
      (entity.posX - entity.lastTickPosX) * scale,
      (entity.posY - entity.lastTickPosY) * scale,
      (entity.posZ - entity.lastTickPosZ) * scale)
  }
}
