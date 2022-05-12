package net.unnamed.event.unnamed

import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.server.{S12PacketEntityVelocity, S32PacketConfirmTransaction}
import net.unnamed.event.vanilla.{AttackedEvent, MovementCalculatePreEvent, PacketEvent}
import net.unnamed.event.{AlwaysActiveListener, EventBus}
import net.unnamed.utils.common.ifce.MinecraftInstance

case object UnnamedEventsGen extends MinecraftInstance {
  {
    EventBus +=
      new AlwaysActiveListener[PacketEvent](
        (event: PacketEvent) => event.packet match {

          case velocityPacket: S12PacketEntityVelocity =>
            if (velocityPacket.getEntityID == mc.thePlayer.getEntityId) {
              val kbEvent = PlayerGotKnockedBackEvent(velocityPacket)
              EventBus.fireEvent(kbEvent)
              if (kbEvent.isCanceled)
                event.cancel()
            }

          case transactionPacket: S32PacketConfirmTransaction =>
            EventBus.fireEvent(ProbeFromServerEvent(ServerProbes.TRANSACTION_PING,
              s"Window id: ${transactionPacket.getWindowId}" +
                s"Action Number: ${transactionPacket.getActionNumber}" +
                s"Should Send C0FPacketConfirmTransaction: ${!transactionPacket.func_148888_e()}"))

          case _ =>
        }) +=
      new AlwaysActiveListener[MovementCalculatePreEvent]((event: MovementCalculatePreEvent) =>
        if (event.entity == mc.thePlayer) {
          val strafeEvent = PlayerMovementCalculatePreEvent(event.forward, event.strafe, event.friction)
          EventBus.fireEvent(strafeEvent)

          if (strafeEvent.isCanceled)
            event.cancel()
          else {
            event.forward = strafeEvent.forward
            event.strafe = strafeEvent.strafe
            event.friction = strafeEvent.friction
          }
        }
      )
  }
}
