package net.unnamed.utils

import net.minecraft.entity.Entity
import net.unnamed.utils.Rotation.getGCD
import net.unnamed.utils.common.ifce.MinecraftInstance

case class Rotation(rotationYaw: Float, rotationPitch: Float) {

  def correctByGCD(GCD: Float, input: Float): Float = {
    val mod = input % GCD
    input - (if (mod < GCD / 2) mod else mod - GCD)
  }

  def add(deltaYaw: Float, deltaPitch: Float): Rotation = {
    val GCD = getGCD

    val correctedDeltaYaw = correctByGCD(GCD, deltaYaw)
    val correctedDeltaPitch = correctByGCD(GCD, deltaPitch)

    Rotation.apply(rotationYaw + correctedDeltaYaw, rotationPitch + correctedDeltaPitch)
  }

  def +(deltaYaw: Float, deltaPitch: Float): Rotation = {
    add(deltaYaw, deltaPitch)
  }

  def to(entity: Entity): Unit = {
    entity.rotationYaw = rotationYaw
    entity.rotationPitch = rotationPitch
  }
}

object Rotation extends MinecraftInstance {
  def getGCD: Float = {
    val f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F
    f * f * f * 8.0F
  }

  def from(entity: Entity): Rotation =
    Rotation.apply(entity.rotationYaw, entity.rotationPitch)
}