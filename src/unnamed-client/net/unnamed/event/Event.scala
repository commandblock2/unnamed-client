package net.unnamed.event

import net.minecraft.network.Packet

trait Event

abstract class CancelableEvent extends Event {
  def cancel(): Unit = canceled = true

  def isCanceled: Boolean = canceled

  private var canceled = false
}
