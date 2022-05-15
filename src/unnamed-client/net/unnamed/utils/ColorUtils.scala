package net.unnamed.utils

import java.awt.Color

case object ColorUtils {
  def mix(lhs: Color, rhs: Color)(leftPercent: Float): Color = {
    val rightPercent = 1 - leftPercent


    val r = (lhs.getRed * leftPercent + rhs.getRed * rightPercent).toInt
    val g = (lhs.getGreen * leftPercent + rhs.getGreen * rightPercent).toInt
    val b = (lhs.getBlue * leftPercent + rhs.getBlue * rightPercent).toInt
    val a = (lhs.getAlpha * leftPercent + rhs.getAlpha * rightPercent).toInt

    new Color(
      Math.max(0, Math.min(r, 255)),
      Math.max(0, Math.min(g, 255)),
      Math.max(0, Math.min(b, 255)),
      Math.max(0, Math.min(a, 255))
    )
  }
}
