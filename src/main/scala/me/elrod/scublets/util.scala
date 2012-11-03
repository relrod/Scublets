package me.elrod.scublets
import java.io.File

object ScrubletsUtils {
  def fileIsInPath(
    file: String,
    path: Array[String] = scala.util.Properties.envOrElse("PATH", "").split(":")): Boolean = {
      path.exists(dir => new File(dir + "/" + file).isFile)
    }
}
