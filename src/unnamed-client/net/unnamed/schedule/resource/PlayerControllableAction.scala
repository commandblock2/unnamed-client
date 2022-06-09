package net.unnamed.schedule.resource

case object PlayerControllableAction extends Enumeration {
  type PlayerAction = Value

  val
  MOVEMENT_INPUT, AIM, HOT_BAR_ITEM, ITEM_USE,
  INVENTORY_OPEN
  = Value
}
