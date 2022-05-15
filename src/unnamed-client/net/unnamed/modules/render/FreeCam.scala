package net.unnamed.modules.render

import net.unnamed.event.EventBus
import net.unnamed.event.vanilla.PlayerLivingUpdateBeginEvent
import net.unnamed.modules.Module
import net.unnamed.settings.setting.FloatSetting
import net.unnamed.world.inputcontrol.{Cameras, FreeCamera}

case object FreeCam extends Module {

  val flySpeed = new FloatSetting(.5f)

  override val enableOnJoin: Boolean = false

  override def onEnable(): Unit = {

    EventBus.next()((_: PlayerLivingUpdateBeginEvent) => {
      val freeCam = new FreeCamera()
      freeCam.capabilities.setFlySpeed(flySpeed.getValue)
      clientSideWorld.cameras.setActiveCamera(Some(freeCam))
    })
  }

  override def onDisable(): Unit = {
    clientSideWorld.cameras.activeCamera = None
  }
}