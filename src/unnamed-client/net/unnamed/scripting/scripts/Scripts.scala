package net.unnamed.scripting.scripts

import net.unnamed.event.EventBus
import net.unnamed.event.unnamed.ScriptsLoadedEvent
import net.unnamed.settings.Settings

import java.io.File


case object Scripts {

  var scripts: List[Script] = Nil

  def loadAll(): Unit = {
    val globalScriptsDir = new File(Settings.settingsDirectory, "scripts")

    if (!globalScriptsDir.exists())
      globalScriptsDir.mkdir()

    globalScriptsDir.listFiles().foreach((file: File) =>
      scripts ::= Script.loadFromFile(file)
    )


    val activeScriptsDir = new File(Settings.activeDirectory, "scripts")

    if (!activeScriptsDir.exists())
      activeScriptsDir.mkdir()

    activeScriptsDir.listFiles().foreach((file: File) =>
      scripts ::= Script.loadFromFile(file)
    )

    EventBus.fireEvent(ScriptsLoadedEvent(unload = false))
  }

  def unLoadAll(): Unit = {
    for (script <- scripts)
      script.context().dispose()

    scripts = Nil
    EventBus.fireEvent(ScriptsLoadedEvent(unload = true))
  }
}
