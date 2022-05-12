package net.unnamed.scripting.context

import net.unnamed.utils.common.ifce.Nameable

trait ScriptingContext extends Nameable {
  var debug = false

  var print: String => Unit = (string: String) => {}
  var onError: Throwable => Unit = (err: Throwable) => {}

  def interpret(code: String): Unit
  def eval(code: String): Any
  def dispose(): Unit
}
