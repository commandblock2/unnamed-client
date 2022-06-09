package net.unnamed.modules.render

import net.minecraft.entity.Entity
import net.unnamed.Unnamed
import net.unnamed.camera.Cameras
import net.unnamed.event.vanilla.Render3DEvent
import net.unnamed.modules.Module
import net.unnamed.settings.setting.{ColorSetting, EntitySelectorSetting}
import net.unnamed.utils.RenderUtils
import net.unnamed.world.ClientSideWorld


case object BoxESP extends Module {

  val color = new ColorSetting(86, 156, 214)
  val entitySelector = new EntitySelectorSetting()

  onEvent((event: Render3DEvent) =>
    mc.theWorld.loadedEntityList
      .stream()
      .filter((entity: Entity) =>
        entitySelector.getValue.isTarget(entity) &&
          !ClientSideWorld.isClientSideEntity(entity) &&
          entity.getEntityBoundingBox != Entity.ZERO_AABB &&
          (entity != mc.getRenderViewEntity ||
            Cameras.shouldRenderThePlayer(event.partialTicks)))
      .forEach((entity: Entity) =>
        RenderUtils.drawEntityBB(entity)(partialTicks = event.partialTicks)(color.getValue))
  )
}
