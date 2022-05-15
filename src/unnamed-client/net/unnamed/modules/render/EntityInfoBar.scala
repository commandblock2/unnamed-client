package net.unnamed.modules.render

import net.minecraft.entity.{Entity, EntityLivingBase}
import net.unnamed.Unnamed

import net.unnamed.event.vanilla.Render3DEvent
import net.unnamed.modules.Module
import net.unnamed.settings.setting.{BoolSetting, ColorSetting, EntitySelectorSetting, FloatSetting, IntegerSetting}
import net.unnamed.utils.RenderUtils.WorldToScreen.{getBoxVertices, getMatrix, worldToScreen}
import net.unnamed.utils.{EntityInterpolationUtil, RenderUtils}
import org.lwjgl.opengl.GL11
import org.lwjglx.util.vector.Vector3f

case object EntityInfoBar extends Module {
  val renderHealth = new BoolSetting(true)
  val renderHurttime = new BoolSetting(true)
  val renderArmor = new BoolSetting(true)

  val barWidth = new IntegerSetting(5)
  val sparse = new FloatSetting(1f)

  val hurttimeColor = new ColorSetting()
  val armorColor = new ColorSetting()

  val entitySelector = new EntitySelectorSetting()

  onEvent((event: Render3DEvent) => {


    val mvMatrix = getMatrix(GL11.GL_MODELVIEW_MATRIX)
    val projectionMatrix = getMatrix(GL11.GL_PROJECTION_MATRIX)

    val renderManager = mc.getRenderManager

    RenderUtils.MVPSetup()

    mc.theWorld.loadedEntityList
      .stream()
      .filter((entity: Entity) =>
        entitySelector.getValue.isTarget(entity) &&
          (entity != mc.getRenderViewEntity || Unnamed.clientSideWorld.get.cameras.shouldRenderThePlayer(event.partialTicks)))
      .forEach((entity: Entity) => {
        val entityOffset = EntityInterpolationUtil.getPartialTicksOffset(entity)(event.partialTicks)
        val entityBox = entity.getEntityBoundingBox
          .offset(entityOffset.xCoord, entityOffset.yCoord, entityOffset.zCoord)
          .offset(-(renderManager.renderPosX), -(renderManager.renderPosY), -(renderManager.renderPosZ))

        val boxVert = getBoxVertices(entityBox)

        var minX = Float.MaxValue
        var minY = Float.MaxValue

        var maxX = -1f
        var maxY = -1f

        for (vert <- boxVert) {
          val screenPos = worldToScreen(
            new Vector3f(vert(0).floatValue(), vert(1).floatValue(), vert(2).floatValue()),
            mvMatrix, projectionMatrix, mc.displayWidth, mc.displayHeight)

          if (screenPos != null) {

            val (screenX, screenY) = screenPos

            minX = Math.min(screenX, minX)
            minY = Math.min(screenY, minY)

            maxX = Math.max(screenX, maxX)
            maxY = Math.max(screenY, maxY)
          }
        }

        val middleX = (maxX + minX) / 2
        val middleY = (maxY + minY) / 2

        val height = (maxY - minY).toInt
        val width = (barWidth.getValue * height / 100f).toInt

        entity match {
          case living: EntityLivingBase =>
            RenderUtils.drawVerticalFilledBar(
              1 - living.hurtTime / 10f
            )(
              width = width,
              horizontalCenter = (middleX.toInt + height * sparse.getValue / 1.2).toInt
            )(
              height = height,
              verticalCenter = middleY.toInt
            )(hurttimeColor.getValue)

            RenderUtils.drawVerticalFilledBar(
              living.getHealth / living.getMaxHealth
            )(
              width = width,
              horizontalCenter = (middleX.toInt + height * sparse.getValue).toInt
            )(
              height = height,
              verticalCenter = middleY.toInt
            )()

            RenderUtils.drawVerticalFilledBar(
              living.getTotalArmorValue / 20f
            )(
              width = width,
              horizontalCenter = (middleX.toInt - height * sparse.getValue / 1.2).toInt
            )(
              height = height,
              verticalCenter = middleY.toInt
            )(armorColor.getValue)

          case _ =>

        }
      })

    RenderUtils.MVPEnd()
  })
}
