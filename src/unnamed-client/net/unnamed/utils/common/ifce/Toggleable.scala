package net.unnamed.utils.common.ifce

trait Toggleable {
  private var isActive: Boolean = false

  def isOn: Boolean = isActive

  protected def onToggle(): Unit = {}

  protected def on(): Unit = {
    if (!isActive)
      toggle()
  }

  protected def off(): Unit = {
    if (isActive)
      toggle()
  }

  protected def toggle(): Unit = {
    isActive = !isActive
    onToggle()
  }
}
