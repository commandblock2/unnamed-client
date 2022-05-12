package net.unnamed.event.vanilla

import net.minecraft.client.multiplayer.WorldClient
import net.unnamed.event.Event

final case class WorldEvent(worldClient: WorldClient) extends Event