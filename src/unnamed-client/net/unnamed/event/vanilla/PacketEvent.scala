package net.unnamed.event.vanilla

import net.minecraft.network.Packet
import net.unnamed.event.CancelableEvent

final case class PacketEvent(packet: Packet[_], clientBound: Boolean = true) extends CancelableEvent