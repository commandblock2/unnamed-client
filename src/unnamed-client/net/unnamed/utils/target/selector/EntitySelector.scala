package net.unnamed.utils.target.selector

import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer

class EntitySelector(
                      var targetPlayer: Boolean = true,
                      var targetMobs: Boolean = false,
                      var targetAnimal: Boolean = false,
                      var noTargetInvisible: Boolean = false,
                      var noTargetDead: Boolean = true,
)  {
  def isTarget(entity: Entity): Boolean = {

    if (noTargetInvisible && entity.isInvisible)
      return false

    if (noTargetDead && entity.isDead)
      return false

    if (targetPlayer && entity.isInstanceOf[EntityPlayer])
      return true

    if (targetAnimal && entity.isInstanceOf[EntityAnimal])
      return true

    if (targetMobs && entity.isInstanceOf[EntityMob])
      return true

    false
  }
}
