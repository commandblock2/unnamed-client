package net.unnamed

import net.unnamed.event.unnamed.UnnamedEventsGen
import net.unnamed.event.vanilla.WorldEvent
import net.unnamed.event.{AlwaysActiveListener, EventBus}
import net.unnamed.modules.Modules
import net.unnamed.render.RenderEngine
import net.unnamed.scripting.ScriptingContexts
import net.unnamed.settings.Settings
import net.unnamed.world.ClientSideWorld

case object Unnamed {
  val name = "unnamed"

  val eventBus: EventBus.type = EventBus

  var clientSideWorld: Option[ClientSideWorld] = None

  val launch: Unit = {
    RenderEngine
    ClientSideWorld
    UnnamedEventsGen
    Settings
    ScriptingContexts

    Modules


    Settings.loadSettings()
    Settings.changeActiveLocalSetting()
  }
}
