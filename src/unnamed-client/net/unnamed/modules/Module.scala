package net.unnamed.modules

import net.unnamed.event.unnamed.ModuleToggleEvent
import net.unnamed.event.{Event, EventBus, Listener}
import net.unnamed.settings.Settings.ISaveLocal
import net.unnamed.settings.setting.SaveThisAlso
import net.unnamed.utils.common.ifce.{ClientInstance, MinecraftInstance, Nameable, Toggleable}

import scala.reflect.ClassTag


class Module(
              val description: String = "As the name suggests"
            ) extends Toggleable with ClientInstance with MinecraftInstance with ISaveLocal with Nameable {

  {
    name = getClass.getSimpleName
  }

  @SaveThisAlso
  var state: Boolean = false

  override def on(): Unit = {
    if (!state)
      toggle()
  }

  override def off(): Unit = {
    if (state)
      toggle()
  }

  override def toggle(): Unit = {
    state = !state
    onToggle()
  }

  override def isOn: Boolean = state

  def onEnable(): Unit = {}

  def onDisable(): Unit = {}


  val enableOnJoin: Boolean = true

  override def onToggle(): Unit = {
    if (mc.theWorld == null)
      return

    if (isOn)
      onEnable()
    else
      onDisable()

    EventBus.fireEvent(ModuleToggleEvent(this))
  }

  def onEvent[SubEvent <: Event](callbackFunc: SubEvent => Unit)(implicit tag: ClassTag[SubEvent]): Listener[SubEvent] = {
    val listener = new Listener[SubEvent] {
      override def isActive: Boolean = isOn && mc.theWorld != null

      override def callback(event: SubEvent): Unit
      = callbackFunc(event)
    }

    EventBus += listener

    listener
  }
}