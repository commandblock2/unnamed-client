package net.unnamed.utils

import net.minecraft.client.main.Main

object REPLHelper {
  def startMinecraft(args: Array[String] = Array("--username", "UnnamedPlayer")): Unit = {

    new Thread() {
      override def run(): Unit = {
        Main.main(Array(
          "--version", "mcp",
          "--accessToken", "0",
          "--assetsDir", "assets",
          "--assetIndex", "1.8",
          "--userProperties", "{}",
        ) ++ args)
      }
    }.start()
  }
}
