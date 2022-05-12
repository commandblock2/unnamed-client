package net.unnamed.event

import fuck.you.scala.Utils.removeASingleElementFromList

import scala.collection.mutable
import scala.reflect.ClassTag


case object EventBus {

  private val registry = new mutable.LinkedHashMap[ClassTag[Event], List[Listener[Event]]]
    .withDefaultValue(Nil)

  private val oneShot = new mutable.LinkedHashMap[ClassTag[Event], List[OneShotListener[Event]]]
    .withDefaultValue(Nil)


  def registerListener[SubEvent <: Event](listener: Listener[SubEvent]): EventBus.type = {
    registry(listener.tag.asInstanceOf[ClassTag[Event]]) ::= listener.asInstanceOf[Listener[Event]]
    this
  }

  def unRegisterListener[SubEvent <: Event](listener: Listener[SubEvent]): EventBus.type = {
    val clazz = listener.tag.asInstanceOf[ClassTag[Event]]
    registry(clazz) = removeASingleElementFromList(listener.asInstanceOf[Listener[Event]], registry(clazz))
    this
  }

  def next[SubEvent <: Event](timesToCall: Int = 1)(callback: SubEvent => Unit)(implicit tag: ClassTag[SubEvent]): EventBus.type = {
    next(OneShotListener(timesToCall)(callback)(tag))
    this
  }

  def next[SubEvent <: Event](oneShotListener: OneShotListener[SubEvent]): EventBus.type = {
    oneShot(oneShotListener.tag.asInstanceOf[ClassTag[Event]]) ::= oneShotListener.asInstanceOf[OneShotListener[Event]]
    this
  }

  def fireEventJ[SubEvent <: Event](event: SubEvent): Unit = {
    fireEvent(event)(ClassTag(event.getClass))
  }

  def fireEvent[SubEvent <: Event](event: SubEvent)(implicit tag: ClassTag[SubEvent]): Unit = {
    registry(tag.asInstanceOf[ClassTag[Event]])
      .filter((value: Listener[Event]) => value.isActive)
      .foreach((listener: Listener[Event]) => listener.callback(event))

    oneShot(tag.asInstanceOf[ClassTag[Event]]) = oneShot(tag.asInstanceOf[ClassTag[Event]]).filter((listener: OneShotListener[Event]) => {
      listener.timesToCall -= 1
      if (listener.timesToCall == 0) {
        listener.callback(event)
        false
      } else {
        if (listener.callEveryTime)
          listener.callback(event)
        true
      }
    })
  }

  def +=[SubEvent <: Event](listener: Listener[SubEvent]): EventBus.type =
    registerListener(listener)

  def -=[SubEvent <: Event](listener: Listener[SubEvent]): EventBus.type =
    unRegisterListener(listener)
}
