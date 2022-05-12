package net.unnamed.utils.common.ifce

import net.minecraft.client.Minecraft

trait MinecraftInstance {
  val mc: Minecraft = Minecraft.getMinecraft
}
