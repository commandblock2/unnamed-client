package net.unnamed.gui

import net.minecraft.client.shader.Framebuffer
import net.unnamed.utils.common.ifce.MinecraftInstance

case object RootPanel extends MinecraftInstance {
  // TODO: test if false could work
  val frameBuffer: Framebuffer = {
    val frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true)
    frameBuffer.unbindFramebuffer()
    frameBuffer
  }

  def render(): Unit = {
    // TODO: what does it mean?
    frameBuffer.bindFramebuffer(false)

    frameBuffer.unbindFramebuffer()
  }



}
