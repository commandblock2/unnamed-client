package net.unnamed.event.vanilla

import net.minecraft.entity.Entity
import net.unnamed.event.CancelableEvent

final case class MovementCalculatePreEvent(entity: Entity,
                                     var forward: java.lang.Float,
                                     var strafe: java.lang.Float,
                                     var friction: java.lang.Float) extends CancelableEvent

