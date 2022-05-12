package net.unnamed.world

import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.entity.Entity
import net.unnamed.Unnamed
import net.unnamed.Unnamed.clientSideWorld
import net.unnamed.event.unnamed.ActiveSettingsChangedEvent
import net.unnamed.event.vanilla.WorldEvent
import net.unnamed.event.{AlwaysActiveListener, Event, EventBus}
import net.unnamed.scripting.scripts.{Script, Scripts}
import net.unnamed.settings.Settings
import net.unnamed.utils.common.ifce.MinecraftInstance
import net.unnamed.world.inputcontrol.Cameras

import java.io.File

class ClientSideWorld(val world: WorldClient) extends MinecraftInstance {

  val cameras = new Cameras

  var moduleEvalContext: Script = null


  {
    reloadScripts()
  }

  private def reloadSettings(): Unit = {
    reloadScripts()
  }

  private def reloadScripts(): Unit = {

    if (moduleEvalContext != null)
      moduleEvalContext.context().dispose()

    moduleEvalContext = Script.loadFromFile(new File(Settings.activeDirectory,
      "module-eval-context.js"))
    moduleEvalContext.load()
    Scripts.unLoadAll()
    Scripts.loadAll()
  }


  var clientSideEntityId: Int = ClientSideWorld.clientSideEntityIdBegin

  // TODO: find other solution that is not so hacky
  def addClientEntity(entity: Entity): Unit = {
    mc.theWorld.addEntityToWorld(clientSideEntityId, entity)
    clientSideEntityId -= 1
  }


}

case object ClientSideWorld {

  val clientSideEntityIdBegin: Int = -10000000

  def isClientSideEntity(entity: Entity): Boolean = {
    entity.getEntityId <= clientSideEntityIdBegin
  }

  {
    EventBus += new AlwaysActiveListener[WorldEvent]((event: WorldEvent) =>
      if (event.worldClient == null)
        Unnamed.clientSideWorld = None
      else
        Unnamed.clientSideWorld = Some(new ClientSideWorld(event.worldClient))
    ) += new AlwaysActiveListener[ActiveSettingsChangedEvent]((_: ActiveSettingsChangedEvent) => {
      if (Unnamed.clientSideWorld.isDefined)
        Unnamed.clientSideWorld.get.reloadSettings()
    })
  }
}