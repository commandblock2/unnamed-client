package net.unnamed.modules

import net.minecraft.client.gui.GuiIngameMenu
import net.unnamed.event.unnamed.ActiveSettingsChangedEvent
import net.unnamed.event.vanilla.{CurrentGUIScreenUpdateEvent, WorldEvent}
import net.unnamed.event.{AlwaysActiveListener, Event, EventBus}
import net.unnamed.modules.antiexploit.AntiScanFromResourcePackConfirm
import net.unnamed.modules.exploit.PulseBlink
import net.unnamed.modules.render.{AntiBlind, Chams, BoxESP, FreeCam, FullBright, NoFOV, NoHurtVibration}
import net.unnamed.utils.ReflectionUtils

case object Modules {
  var modules: List[Module] = Nil

  def loadModuleSettings(): Unit = {
    for (module <- modules) {

      module.loadSetting()

      if (!module.enableOnJoin)
        module.off()
    }
  }


  {
    // TODO: can we do that by reflection or do we need to reference explicitly here?
    val currentPackage = getClass.getPackage
    val allModuleClass = ReflectionUtils.getAllSubClassInPackage(currentPackage, classOf[Module])
    for (moduleClass <- allModuleClass) {
      val moduleField = moduleClass.getDeclaredField("MODULE$")
      modules ::= moduleField.get(null).asInstanceOf[Module]
    }

    EventBus +=
      new AlwaysActiveListener[WorldEvent]((event: WorldEvent) => {
        if (event.worldClient != null) {
          loadModuleSettings()

          for (module <- modules) {
            if (module.isOn)
              module.onEnable()
          }
        }
        else
          for (module <- modules) {
            if (module.isOn) module.onDisable()
          }
      }) +=
      new AlwaysActiveListener[CurrentGUIScreenUpdateEvent]((event: CurrentGUIScreenUpdateEvent) => {
        if (event.newScreen.isInstanceOf[GuiIngameMenu]) {
          for (module <- modules)
            module.saveSetting()
        }
      }) +=
      new AlwaysActiveListener[ActiveSettingsChangedEvent]((_: ActiveSettingsChangedEvent) => {
        loadModuleSettings()
      })
  }

  // TODO: mutex
}
