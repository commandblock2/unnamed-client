package net.unnamed.event.vanilla

import net.unnamed.event.Event

final case class Render3DEvent(partialTicks: Float) extends Event
final case class Render2DEvent(partialTicks: Float) extends Event
