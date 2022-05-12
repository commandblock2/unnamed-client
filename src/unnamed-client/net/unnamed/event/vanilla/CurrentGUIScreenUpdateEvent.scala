package net.unnamed.event.vanilla

import net.minecraft.client.gui.GuiScreen
import net.unnamed.event.CancelableEvent

final case class CurrentGUIScreenUpdateEvent(newScreen: GuiScreen) extends CancelableEvent
