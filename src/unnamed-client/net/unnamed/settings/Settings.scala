package net.unnamed.settings

import com.google.common.io.Files
import com.google.gson._
import com.google.gson.annotations.Expose
import net.minecraft.client.gui.GuiIngameMenu
import net.unnamed.Unnamed
import net.unnamed.event.unnamed.ActiveSettingsChangedEvent
import net.unnamed.event.vanilla.{ClientShutDownEvent, CurrentGUIScreenUpdateEvent}
import net.unnamed.event.{AlwaysActiveListener, EventBus}
import net.unnamed.settings.setting.{SaveThisAlso, Setting, SettingDeserializer, SettingSerializer}
import net.unnamed.utils.ReflectionUtils
import net.unnamed.utils.common.ifce.{MinecraftInstance, Nameable}

import java.io.{BufferedReader, File, FileReader, FileWriter}
import java.lang.reflect.Field
import java.util
import scala.collection.convert.ImplicitConversions._
import scala.collection.mutable

// TODO: the whole setting system could need a refactor but I don't see any better alternative
// TODO: add exception handling when loading

object Settings extends MinecraftInstance {

  {
    EventBus += new AlwaysActiveListener[ClientShutDownEvent]((_: ClientShutDownEvent) => saveSettings())
    EventBus += new AlwaysActiveListener[CurrentGUIScreenUpdateEvent]((event: CurrentGUIScreenUpdateEvent) =>
      if (event.newScreen.isInstanceOf[GuiIngameMenu])
        saveSettings()
    )
  }


  val settingsDirectory = new File(mc.mcDataDir, Unnamed.name)
  val globalSettings: mutable.Map[String, List[ISaveGlobal]] = new mutable.HashMap[String, List[ISaveGlobal]]().withDefaultValue(Nil)

  var activeDirectory = new File(settingsDirectory, "default")
  var activeSettings: mutable.Map[String, List[ISaveLocal]] = new mutable.HashMap[String, List[ISaveLocal]]().withDefaultValue(Nil)


  val serializer: Gson = {
    val builder = new GsonBuilder()

    ReflectionUtils.getAllSubClassInPackage(getClass.getPackage, classOf[Setting[_]]).foreach((value: Class[Setting[_]]) => {
      builder.registerTypeAdapter(value, SettingSerializer)
      builder.registerTypeAdapter(value, SettingDeserializer)

      //      if (classOf[NameReferencedSetting[_]].isAssignableFrom(value)) {
      //        builder.registerTypeAdapter(value, NameableSettingSerializer)
      //        builder.registerTypeAdapter(value, NameableSettingDeserializer)
      //      }
    })

    builder
      .excludeFieldsWithoutExposeAnnotation()
      .setPrettyPrinting()
      .create()
  }

  val parser = new JsonParser()

  trait ISave {
    val settingName: String = getClass.getSimpleName
    val locatedPackage: Package = getClass.getPackage

    var settings = new JsonArray

    def loadSetting(): ISave = {
      for (jsonElement <- settings) {

        val jsonObject = jsonElement.getAsJsonObject
        jsonObject.entrySet().foreach((value: util.Map.Entry[String, JsonElement]) => {

          val settingField = ReflectionUtils.getAllField(this)
            .find((field: Field) => field.getName == value.getKey)
            .orNull

          if (!settingField.isAccessible)
            settingField.setAccessible(true)

          val originalSetting = settingField.get(this)
          val deserializedSetting = serializer.fromJson(value.getValue, originalSetting.getClass)

          if (settingField.isAnnotationPresent(classOf[SaveThisAlso]))
            settingField.set(this, deserializedSetting)
          else {
            ReflectionUtils.getAllField(originalSetting).foreach((field: Field) => {

              if (field.isAnnotationPresent(classOf[Expose])) {

                if (!field.isAccessible)
                  field.setAccessible(true)

                field.set(originalSetting, field.get(deserializedSetting))
              }
            })

            originalSetting match {
              case value1: Setting[_] => value1.onChange()
              case _ =>
            }

          }
        })
      }

      this
    }

    def saveSetting(): Unit = {
      settings = new JsonArray
      ReflectionUtils.getAllField(this).filter((field: Field) => {
        if (!field.isAccessible)
          field.setAccessible(true)

        classOf[Setting[_]].isAssignableFrom(field.getType) ||
          field.isAnnotationPresent(classOf[SaveThisAlso])
      }
      )
        .foreach((field: Field) => {

          val setting = field.get(this)

          val jsonObject = new JsonObject
          jsonObject.add(field.getName, serializer.toJsonTree(setting))
          settings.add(jsonObject)
        })

    }

    def getConfigFileName: String =
      locatedPackage.getName + ".json"
  }

  trait ISaveLocal extends ISave {

    {
      activeSettings(getConfigFileName) ::= this
    }

  }

  trait ISaveGlobal extends ISave {

    {
      globalSettings(getConfigFileName) ::= this
    }

  }

  def loadSettings(): Unit = {
    if (!settingsDirectory.exists())
      settingsDirectory.mkdir()

    for (file <- settingsDirectory.listFiles() if !file.isDirectory && Files.getFileExtension(file.getName) == "json") {

      val settingFileName = file.getName

      val fileContent = parser.parse(new BufferedReader(new FileReader(file)))
      for (element <- fileContent.getAsJsonArray) {
        val jsonObject = element.getAsJsonObject

        val save = globalSettings(settingFileName).find((global: ISaveGlobal) =>
          global.settingName == jsonObject.get("settingName").getAsString)

        if (save.isDefined)
          save.get.settings = jsonObject.get("settings").getAsJsonArray

      }

    }

  }

  def changeActiveLocalSetting(settingName: String = "default"): Unit = {
    activeDirectory = new File(settingsDirectory, settingName)


    if (!activeDirectory.exists())
      activeDirectory.mkdir()

    for (file <- activeDirectory.listFiles() if Files.getFileExtension(file.getName) == "json") {
      val settingFileName = file.getName

      val fileContent = parser.parse(new BufferedReader(new FileReader(file)))
      for (element <- fileContent.getAsJsonArray) {
        val jsonObject = element.getAsJsonObject

        val save = activeSettings(settingFileName).find((global: ISaveLocal) =>
          global.settingName == jsonObject.get("settingName").getAsString)

        if (save.isDefined)
          save.get.settings = jsonObject.get("settings").getAsJsonArray

      }

    }

    EventBus.fireEvent(ActiveSettingsChangedEvent())
  }

  def saveSettings(): Unit = {

    globalSettings.foreachEntry((_: String, value: List[ISave]) =>
      for (save <- value)
        save.saveSetting())

    activeSettings.foreachEntry((_: String, value: List[ISave]) =>
      for (save <- value)
        save.saveSetting())

    if (!settingsDirectory.exists())
      settingsDirectory.mkdir()

    if (!activeDirectory.exists())
      activeDirectory.mkdir()

    globalSettings.foreachEntry((fileName: String, saves: List[ISaveGlobal]) => {
      val file = new File(settingsDirectory, fileName)
      if (!file.exists())
        file.createNewFile()

      val jsonArray = new JsonArray
      for (save <- saves) {
        val jsonObject = new JsonObject

        jsonObject.addProperty("settingName", save.settingName)
        jsonObject.add("settings", save.settings)

        jsonArray.add(jsonObject)
      }

      val writer = new FileWriter(file)
      writer.write(serializer.toJson(jsonArray))
      writer.close()
    })


    activeSettings.foreachEntry((fileName: String, saves: List[ISaveLocal]) => {
      val file = new File(activeDirectory, fileName)
      if (!file.exists())
        file.createNewFile()

      val jsonArray = new JsonArray
      for (save <- saves) {
        val jsonObject = new JsonObject
        jsonObject.addProperty("settingName", save.settingName)
        jsonObject.add("settings", save.settings)

        jsonArray.add(jsonObject)
      }


      val writer = new FileWriter(file)
      writer.write(serializer.toJson(jsonArray))
      writer.close()
    })

  }

}
