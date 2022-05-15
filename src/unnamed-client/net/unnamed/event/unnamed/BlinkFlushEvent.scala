package net.unnamed.event.unnamed

import net.minecraft.network.Packet
import net.unnamed.event.Event

case class BlinkFlushEvent(packetList: List[Packet[_]]) extends Event
