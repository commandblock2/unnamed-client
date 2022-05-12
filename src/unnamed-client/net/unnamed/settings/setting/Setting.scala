package net.unnamed.settings.setting

import com.google.gson
import com.google.gson.annotations.Expose
import com.google.gson._
import net.unnamed.Unnamed
import net.unnamed.utils.target.selector.EntitySelector

import java.awt.Color
import java.lang.reflect.Type

trait Setting[T] {
  @(Expose@annotation.meta.field)
  var expr: String = null

  protected def valueFromSetting: T

  def getValue: T = {
    if (expr != null)
      Unnamed.clientSideWorld.get.
        moduleEvalContext.context().
        eval(expr).asInstanceOf[T]
    else
      valueFromSetting
  }

  def onChange(): Unit = {}

  def set(value: T): Unit

  val suggestedValue: String = "Fuck you"
}

object SettingSerializer extends JsonSerializer[Setting[_]] {

  val serializer: Gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

  override def serialize(src: Setting[_], `type`: Type, jsonSerializationContext: JsonSerializationContext): JsonElement = {
    val jsonObject = serializer.toJsonTree(src).getAsJsonObject
    jsonObject.addProperty("expr", src.expr)
    jsonObject
  }
}

object SettingDeserializer extends JsonDeserializer[Setting[_]] {

  val serializer: Gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

  override def deserialize(jsonElement: JsonElement, `type`: Type, jsonDeserializationContext: JsonDeserializationContext): Setting[_] = {
    val setting = serializer.fromJson(jsonElement, `type`).asInstanceOf[Setting[_]]
    val expr = jsonElement.getAsJsonObject.get("expr")
    if (expr != null)
      setting.expr = expr.getAsString
    setting
  }
}


final class BoolSetting(
                         @(Expose@annotation.meta.field)
                         private var on: Boolean,
                         override val suggestedValue: String = "Fuck you") extends Setting[Boolean] {
  override def set(value: Boolean): Unit =
    on = value

  override def valueFromSetting: Boolean = on
}

final class IntegerSetting(
                            @(Expose@annotation.meta.field)
                            private var number: Int,
                            private val suggestedLower: Int = 0,
                            private val suggestedHigher: Int = 1) extends Setting[Int] {
  override def valueFromSetting: Int = number

  override def set(value: Int): Unit =
    number = value

  override val suggestedValue: String = s"[$suggestedLower, $suggestedHigher]"
}


final class FloatSetting(
                          @(Expose@annotation.meta.field)
                          private var number: Float,
                          private val suggestedLower: Float = 0,
                          private val suggestedHigher: Float = 1) extends Setting[Float] {
  override def valueFromSetting: Float = number

  override def set(value: Float): Unit =
    number = value

  override val suggestedValue: String = s"[$suggestedLower, $suggestedHigher]"
}

final class ListSetting(
                         @(Expose@annotation.meta.field)
                         private var choice: String,
                         private val suggestedList: List[String]) extends Setting[String] {
  override def valueFromSetting: String = choice

  override def set(value: String): Unit =
    choice = value

  override val suggestedValue: String = suggestedList.toString()
}

final class EntitySelectorSetting(
                                   @(Expose@annotation.meta.field)
                                   var targetPlayer: Boolean = true,
                                   @(Expose@annotation.meta.field)
                                   var targetMobs: Boolean = false,
                                   @(Expose@annotation.meta.field)
                                   var targetAnimal: Boolean = false,
                                   @(Expose@annotation.meta.field)
                                   var noTargetInvisible: Boolean = false,
                                   @(Expose@annotation.meta.field)
                                   var noTargetDead: Boolean = true,
                                 ) extends Setting[EntitySelector] {

  private var selector = new EntitySelector(targetPlayer = targetPlayer,
    targetMobs = targetMobs,
    targetAnimal = targetAnimal,
    noTargetDead = noTargetDead,
    noTargetInvisible = noTargetInvisible)


  override def onChange(): Unit = {
    selector = new EntitySelector(targetPlayer = targetPlayer,
      targetMobs = targetMobs,
      targetAnimal = targetAnimal,
      noTargetDead = noTargetDead,
      noTargetInvisible = noTargetInvisible)
  }

  override protected def valueFromSetting: EntitySelector = selector

  override def set(value: EntitySelector): Unit = {
    selector = value
    targetMobs = selector.targetMobs
    targetPlayer = selector.targetPlayer
    targetAnimal = selector.targetAnimal
    noTargetDead = selector.noTargetDead
    noTargetInvisible = selector.noTargetInvisible
  }

}

final class ColorSetting(
                          @(Expose@annotation.meta.field)
                          private var red: Int = 100,
                          @(Expose@annotation.meta.field)
                          private var green: Int = 100,
                          @(Expose@annotation.meta.field)
                          private var blue: Int = 100,
                          @(Expose@annotation.meta.field)
                          private var alpha: Int = 128,
                        ) extends Setting[Color] {

  private var color = new Color(red, green, blue, alpha)

  override protected def valueFromSetting: Color = color

  override def set(value: Color): Unit = {
    color = value
    red = color.getRed
    green = color.getGreen
    blue = color.getBlue
    alpha = color.getAlpha
  }

  override def onChange(): Unit = {
    color = new Color(red, green, blue, alpha)
  }
}