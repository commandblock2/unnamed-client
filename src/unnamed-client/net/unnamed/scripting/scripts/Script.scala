package net.unnamed.scripting.scripts

import net.unnamed.scripting.context.{GraalJSScriptingContext, ScalaScriptingContext, ScriptingContext}
import net.unnamed.utils.common.ifce.Nameable
import org.apache.commons.io.FilenameUtils

import java.io.{BufferedReader, File}
import java.nio.file.Files


abstract class Script(val file: File) extends Nameable {
  val scriptText = new String(Files.readAllBytes(file.toPath))

  {
    name = file.getName + this.toString
  }

  def load(): Unit = {
    context().interpret(scriptText)
  }

  def `import`(anotherScript: File): Any =
    context().eval(new String(Files.readAllBytes(anotherScript.toPath)))

  def context(): ScriptingContext
}

object Script {
  def loadFromFile(file: File): Script = {

    if (!file.exists())
      file.createNewFile()

    FilenameUtils.getExtension(file.getName) match {
      case "js" => new Script(file) {
        val jsContext: GraalJSScriptingContext = new GraalJSScriptingContext {
          name = s"FromScript[${file.getName}], ${this.toString}"
        }

        override def context(): ScriptingContext = jsContext
      }
      case "scala" => new Script(file) {
        val scalaContext: ScalaScriptingContext = new ScalaScriptingContext {
          name = s"FromScript[${file.getName}], ${this.toString}"
        }

        override def context(): ScriptingContext = scalaContext
      }
    }
  }
}