package net.unnamed.scripting

import net.unnamed.scripting.context.ScriptingContext
import net.unnamed.scripting.scripts.{Script, Scripts}

case object ScriptingContexts {
  private var contexts: List[ScriptingContext] = Nil

  def addContext(context: ScriptingContext): Unit =
    contexts ::= context

  def removeContext(context: ScriptingContext): Unit =
    contexts = contexts diff List(context)

  def +=(context: ScriptingContext): Unit =
    addContext(context)

  def -=(context: ScriptingContext): Unit =
    removeContext(context)

  def getContexts: List[ScriptingContext] =
    (contexts :: Scripts.scripts.map((script: Script) => script.context())).asInstanceOf[List[ScriptingContext]]
}
