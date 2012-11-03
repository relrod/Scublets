package me.elrod.scublets
import org.joda.time.DateTime
import org.apache.commons.io.FileUtils

case class EvaluationOutput()

trait Language {
  def compile: Option[String] = None
  def run: String
  val aliases: List[String] = List()
  
}

/** An encapsulation for an evaluation to take place.
  *
  * Here, we set up a sandbox environment and perform an actual evaluation
  * as per the information we're given by the language, which we're passed
  * as a paramater.
  *
  * The language specifies how to run the code, but does **not** specify
  * anything sandbox-specific. Everything sandbox-specific goes in this class,
  * and is done before/after the evaluation. For example, the command to
  * evaluate a script might be appended to a `sandbox` command, but this is done
  * internally in this class.
  *
  * @todo Swap out the 'home' parameter with a 'config' parameter which contains
  *   information on how, specifically, to set up the sandbox environment.
  * @param homeRoot The directory which contains the home directories of any and
  *   all evaluations.
  * @param code The code which we are evaluating.
  * @param language An object containing the methods we'll use to perform an
  *   actual evaluation.
  */
class Sandbox(home: String, code: String, language: Language) {
  
  /** An exception that gets thrown when needed binaries don't exist in $PATH. */
  private case class MissingBinariesException(message: String) extends RuntimeException

  // TODO: Move these to config file.
  if (List("sandbox", "timeout").exists(program => !ScrubletsUtils.fileIsInPath(program))) {
    throw MissingBinariesException(
      "The programs 'sandbox' and 'timeout' are required and should be in $PATH.")
  }
    
}
