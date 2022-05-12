package net.unnamed.event.unnamed

import net.minecraft.network.play.server.S12PacketEntityVelocity
import net.unnamed.event.CancelableEvent

final case class PlayerGotKnockedBackEvent(packet: S12PacketEntityVelocity) extends CancelableEvent
