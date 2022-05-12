package net.unnamed.scripting.context

import org.graalvm.polyglot.{Context, HostAccess, Source}

class GraalJSScriptingContext extends net.unnamed.scripting.context.ScriptingContext {

  val graalContext: Context = {
    val context = Context
      .newBuilder("js")
      .allowAllAccess(true)
      .allowExperimentalOptions(true)
      .allowHostAccess(HostAccess.ALL)
      .allowHostClassLoading(true)
      .allowIO(true)
      .allowCreateThread(true)
      .allowNativeAccess(true)
      .option("js.nashorn-compat", "true")
      .option("js.ecmascript-version", "2022")
      .option("engine.WarnInterpreterOnly", "false")
      .build()

    context.eval("js", """const Unnamed = Java.type("net.unnamed.Unnamed")""")
    context.eval("js", """const Minecraft = Java.type("net.minecraft.client.Minecraft")""")
    context.eval("js", """const mc = Minecraft.getMinecraft()""")

    context
  }


  override def eval(code: String) = {
    if (debug)
      try
        graalContext.eval("js", code).as(classOf[Any])
      catch {
        case err: Throwable => onError(err)
      }
    else
      graalContext.eval("js", code).as(classOf[Any])
  }

  override def dispose(): Unit =
    graalContext.close()

  override def interpret(code: String): Unit =
    eval(code)
}
