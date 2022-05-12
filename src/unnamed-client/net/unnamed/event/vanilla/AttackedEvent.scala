package net.unnamed.event.vanilla

import net.minecraft.entity.Entity
import net.unnamed.event.Event

case class AttackedEvent(entity: Entity) extends Event
