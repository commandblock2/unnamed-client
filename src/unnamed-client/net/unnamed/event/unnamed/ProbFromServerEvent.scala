package net.unnamed.event.unnamed

import net.unnamed.event.Event
import net.unnamed.event.unnamed.ServerProbes.Probe

object ServerProbes extends Enumeration {
  type Probe = Value

  val TRANSACTION_PING, FILE_PROB_OUT_OF_RCPACK_FOLDER = Value
}

final case class ProbeFromServerEvent(probeType: Probe, message: String = "") extends Event
