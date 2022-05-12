package net.unnamed.utils.common.ifce

import net.unnamed.Unnamed
import net.unnamed.world.ClientSideWorld

trait ClientInstance {
  def clientSideWorld: ClientSideWorld = Unnamed.clientSideWorld.get
}
