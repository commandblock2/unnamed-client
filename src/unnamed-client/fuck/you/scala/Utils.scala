package fuck.you.scala

object Utils {
  def removeASingleElementFromList[T](element: T, list: List[T]): List[T] =
    list diff List(element)

}
