package net.unnamed.event.vanilla.tick

import net.unnamed.event.CancelableEvent

final case class PlayerMovementCalculatePreEvent(var forward: java.lang.Float,
                                                 var strafe: java.lang.Float,
                                                 var friction: java.lang.Float) extends CancelableEvent with TickEvent
