package net.unnamed.event

import net.unnamed.event.vanilla.tick.PlayerMovementCalculatePreEvent

case object EventAlias {
  type StrafeEvent = PlayerMovementCalculatePreEvent
}
