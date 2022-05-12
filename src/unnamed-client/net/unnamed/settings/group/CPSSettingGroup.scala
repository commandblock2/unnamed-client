package net.unnamed.settings.group

import net.unnamed.settings.setting.IntegerSetting

import scala.util.Random

trait CPSSettingGroup {
  val targetCPS = new IntegerSetting(8)
  val CPSHigherLimit = new IntegerSetting(13)

  var nextTime: Long = 0
  var lastClick: Long = System.currentTimeMillis()

  def shouldClick(): Boolean =
    nextTime < System.currentTimeMillis() - lastClick

  def clicked(): Unit = {
    val maxTime = 1000 / targetCPS.getValue
    val minTime = 1000 / CPSHigherLimit.getValue

    lastClick = System.currentTimeMillis()
    nextTime = Random.nextLong(maxTime - minTime) + minTime
  }
}
