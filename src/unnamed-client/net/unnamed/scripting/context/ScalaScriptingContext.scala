package net.unnamed.scripting.context

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.shell.ReplReporterImpl
import scala.tools.nsc.interpreter.{IMain, ReplReporter, Results}


class ScalaScriptingContext extends ScriptingContext {

  val settings: Settings = {
    val settings = new Settings()
    settings.usejavacp.value = true
    settings
  }

  val interpreter: IMain = {
    val interpreter = new IMain(settings, new ReplReporterImpl(settings))

    interpreter.interpret(
      "import net.minecraft.client.Minecraft \n" +
        "val mc = Minecraft.getMinecraft()"
      , synthetic = true)

    interpreter
  }

  private def evalNoExceptionHandling(code: String): AnyRef = {
    val compiled = interpreter.compile(code, synthetic = true)

    compiled match {
      case Right(value) =>
        val result = value.eval

        result match {
          case Right(retValue) => retValue
          case Left(retValue) => None
        }
      case Left(value) => None
    }
  }

  override def eval(code: String): Any = {
    if (debug)
      try
        evalNoExceptionHandling(code)
      catch {
        case err: Throwable => onError(err)
      }
    else
      evalNoExceptionHandling(code)
  }

  override def dispose(): Unit = interpreter.close()

  override def interpret(code: String): Unit = {
    if (debug)
      try
        interpreter.interpret(code, synthetic = true)
      catch {
        case err: Throwable => onError(err)
      }
    else
      interpreter.interpret(code, synthetic = true)
  }
}
