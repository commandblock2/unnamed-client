package net.unnamed.utils

import java.io.File
import java.lang.reflect.Field

case object ReflectionUtils {
  def getAllField(obj: Object): List[Field] = {
    var fields: List[Field] = Nil

    var clazz: Class[_] = obj.getClass

    while (clazz != classOf[Object]) {
      fields ++= clazz.getDeclaredFields
      clazz = clazz.getSuperclass
    }

    fields
  }

  def getAllSubClassInPackage[T](pack: Package, clazz: Class[T]): List[Class[T]] = {
    val clazzs = getClasses(pack.getName)
    clazzs.filter((value: Class[_]) => clazz.isAssignableFrom(value) && clazz != value).asInstanceOf[List[Class[T]]]
  }


  private def getClasses(packageName: String) = {
    val classLoader = Thread.currentThread.getContextClassLoader
    assert(classLoader != null)
    val path = packageName.replace('.', '/')
    val resources = classLoader.getResources(path)
    var dirs: List[File] = Nil
    while ( {
      resources.hasMoreElements
    }) {
      val resource = resources.nextElement
      dirs ::= new File(resource.getFile)
    }
    var classes: List[Class[_]] = Nil
    for (directory <- dirs) {
      classes ++= findClasses(directory, packageName)
    }
    classes
  }

  private def findClasses(directory: File, packageName: String):List[Class[_]] = {
    var classes: List[Class[_]] = Nil
    if (directory.exists) {

      val files = directory.listFiles
      for (file <- files) {
        if (file.isDirectory) {
          assert(!file.getName.contains("."))
          classes ++= findClasses(file, packageName + "." + file.getName)
        }
        else if (file.getName.endsWith(".class"))
          classes ::= (Class.forName(packageName + '.' + file.getName.substring(0, file.getName.length - 6)))
      }
    }
    classes
  }
}
