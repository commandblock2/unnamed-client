package net.unnamed.event

import net.unnamed.utils.common.ifce.MinecraftInstance

import scala.reflect.ClassTag

abstract class Listener[SubEvent <: Event](implicit val tag: ClassTag[SubEvent]) {
  def isActive: Boolean

  def callback(event: SubEvent): Unit

  var shouldUnRegister: Boolean = false
}

class AlwaysActiveListener[SubEvent <: Event]
(val callbackFunc: SubEvent => Unit)
(implicit override val tag: ClassTag[SubEvent]) extends Listener[SubEvent] {
  override def isActive = true

  override def callback(event: SubEvent): Unit = callbackFunc(event)
}

class InGameActiveListener[SubEvent <: Event]
(val callbackFunc: SubEvent => Unit)
(implicit override val tag: ClassTag[SubEvent])
  extends Listener[SubEvent] with MinecraftInstance{
  override def isActive: Boolean =
    mc.thePlayer != null && mc.theWorld != null

  override def callback(event: SubEvent): Unit = callbackFunc(event)
}

case class OneShotListener[SubEvent <: Event](var timesToCall: Int = 1, callEveryTime:Boolean = false)(val callback: SubEvent => Unit)(implicit val tag: ClassTag[SubEvent]) {
  def cancel(): Unit =
    timesToCall = 0
}