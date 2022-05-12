package net.unnamed.event.vanilla

import net.minecraft.entity.Entity
import net.unnamed.event.CancelableEvent

case class RenderEntityEvent(entity: Entity, partialTicks: Float) extends CancelableEvent
