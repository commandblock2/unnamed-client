package net.unnamed.utils

import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.{GlStateManager, Tessellator}
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.unnamed.utils.common.ifce.MinecraftInstance
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11._
import org.lwjglx.util.vector.{Matrix4f, Vector3f, Vector4f}

import java.awt.Color

case object RenderUtils extends MinecraftInstance {

  // Skid from LB

  def MVPSetup(): Unit = {
    GL11.glPushAttrib(GL11.GL_ENABLE_BIT)

    GL11.glEnable(GL11.GL_BLEND)
    GL11.glDisable(GL11.GL_TEXTURE_2D)
    GL11.glDisable(GL11.GL_DEPTH_TEST)

    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glPushMatrix()
    GL11.glLoadIdentity()
    GL11.glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1.0f, 1.0)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glPushMatrix()
    GL11.glLoadIdentity()

    glDisable(GL_DEPTH_TEST)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    GlStateManager.enableTexture2D()
    GlStateManager.depthMask(true)
  }

  def MVPEnd():Unit = {
    glEnable(GL_DEPTH_TEST)

    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glPopMatrix()

    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glPopMatrix()

    GL11.glPopAttrib()
  }

  def alphaBlendWidth2FNo2DNoDepthSetup(): Unit = {
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    glEnable(GL_BLEND)
    glLineWidth(2F)
    glDisable(GL_TEXTURE_2D)
    glDisable(GL_DEPTH_TEST)
    glDepthMask(false)
  }

  def alphaBlendWidth2FNo2DNoDepthEnd(): Unit = {
    glEnable(GL_TEXTURE_2D)
    glEnable(GL_DEPTH_TEST)
    glDepthMask(true)
    glDisable(GL_BLEND)
  }

  // assumed to be filled from bottom
  def drawVerticalFilledBar(percentage: Float)
                           (width: Int, horizontalCenter: Int)
                           (height: Int, verticalCenter: Int)
                           (color: Color = ColorUtils.mix(Color.green, Color.red)(percentage)): Unit = {
    val halfWidth = width / 2
    val halfHeight = height / 2 * percentage

    val newVerticalCenter = verticalCenter + height / 2 * (1 - percentage)

    glColor(color)
    glBegin(GL_QUADS)

    glVertex2d(horizontalCenter + halfWidth, newVerticalCenter - halfHeight)
    glVertex2d(horizontalCenter - halfWidth, newVerticalCenter - halfHeight)
    glVertex2d(horizontalCenter - halfWidth, newVerticalCenter + halfHeight)
    glVertex2d(horizontalCenter + halfWidth, newVerticalCenter + halfHeight)

    glEnd()
  }

  def glColor(color: Color): Unit = {
    val red = color.getRed / 255F
    val green = color.getGreen / 255F
    val blue = color.getBlue / 255F
    val alpha = color.getAlpha / 255F
    GlStateManager.color(red, green, blue, alpha)
  }

  def drawEntityBB(entity: Entity)(partialTicks: Float)(color: Color): Unit = {
    val box = entity.getEntityBoundingBox.expand(.1, .1, .1)
    val offset = EntityInterpolationUtil.getPartialTicksOffset(entity)(partialTicks)
    drawAxisAlignedBB(box.offset(offset.xCoord, offset.yCoord, offset.zCoord))(color)
  }

  def drawAxisAlignedBB(axisAlignedBB: AxisAlignedBB)(color: Color): Unit = {
    alphaBlendWidth2FNo2DNoDepthSetup()
    glColor(color)
    drawFilledBox(axisAlignedBB)
    GlStateManager.resetColor()
    alphaBlendWidth2FNo2DNoDepthEnd()
  }

  // Imagine skidding from fucking MOJANG
  // src/minecraft/net/minecraft/client/renderer/entity/Render.java
  def drawFilledBox(aabb: AxisAlignedBB): Unit = {
    val renderManager = mc.getRenderManager
    val axisAlignedBB: AxisAlignedBB = aabb.offset(
      -renderManager.renderPosX,
      -renderManager.renderPosY,
      -renderManager.renderPosZ
    )

    val tessellator = Tessellator.getInstance
    val worldRenderer = tessellator.getWorldRenderer

    worldRenderer.begin(7, DefaultVertexFormats.POSITION)

    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()

    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()

    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()

    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()

    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()

    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()

    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()

    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
    worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()

    tessellator.draw()
  }


  // TODO: RenderEngine
  case object WorldToScreen {
    def getMatrix(matrix: Int): Matrix4f = {
      val floatBuffer = BufferUtils.createFloatBuffer(16)
      GL11.glGetFloatv(matrix, floatBuffer)
      new Matrix4f().load(floatBuffer).asInstanceOf[Matrix4f]
    }

    def worldToScreen(pointInWorld: Vector3f, screenWidth: Int, screenHeight: Int): (Float, Float) =
      worldToScreen(pointInWorld, getMatrix(GL11.GL_MODELVIEW_MATRIX), getMatrix(GL11.GL_PROJECTION_MATRIX), screenWidth, screenHeight)

    def worldToScreen(pointInWorld: Vector3f, view: Matrix4f, projection: Matrix4f, screenWidth: Int, screenHeight: Int): (Float, Float) = {
      val clipSpacePos = multiply(
        multiply(
          new Vector4f(
            pointInWorld.x, pointInWorld.y, pointInWorld.z, 1.0f
          ), view
        ), projection
      )

      val ndcSpacePos = new Vector3f(clipSpacePos.x / clipSpacePos.w, clipSpacePos.y / clipSpacePos.w, clipSpacePos.z / clipSpacePos.w)

      val screenX = ((ndcSpacePos.x + 1.0f) / 2.0f) * screenWidth
      val screenY = ((1.0f - ndcSpacePos.y) / 2.0f) * screenHeight

      if (ndcSpacePos.z < -1.0 || ndcSpacePos.z > 1.0)
        return null

      (screenX, screenY)
    }

    def multiply(vec: Vector4f, mat: Matrix4f) =
      new Vector4f(vec.x * mat.m00 + vec.y * mat.m10 + vec.z * mat.m20 + vec.w * mat.m30,
        vec.x * mat.m01 + vec.y * mat.m11 + vec.z * mat.m21 + vec.w * mat.m31,
        vec.x * mat.m02 + vec.y * mat.m12 + vec.z * mat.m22 + vec.w * mat.m32,
        vec.x * mat.m03 + vec.y * mat.m13 + vec.z * mat.m23 + vec.w * mat.m33)

    def getBoxVertices(bb: AxisAlignedBB): Array[Array[Double]] = {
      Array(
        Array(bb.minX, bb.minY, bb.minZ),
        Array(bb.minX, bb.maxY, bb.minZ),
        Array(bb.maxX, bb.maxY, bb.minZ),
        Array(bb.maxX, bb.minY, bb.minZ),
        Array(bb.minX, bb.minY, bb.maxZ),
        Array(bb.minX, bb.maxY, bb.maxZ),
        Array(bb.maxX, bb.maxY, bb.maxZ),
        Array(bb.maxX, bb.minY, bb.maxZ)
      )
    }
  }
}

