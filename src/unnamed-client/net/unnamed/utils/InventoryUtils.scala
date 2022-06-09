package net.unnamed.utils

import net.minecraft.item.Item
import net.unnamed.utils.common.ifce.MinecraftInstance

case object InventoryUtils extends MinecraftInstance {

  val hotBar: Range = 36 until 45

  def findItem(searchRange: Range, item: Item): Option[Int] = {
    for (i <- searchRange) {
      val stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack
      if (stack != null && (stack.getItem eq item)) return Some(i)
    }
    None
  }
}
