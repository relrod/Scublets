package me.elrod.scublets
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.apache.commons.io.FileUtils
import java.io.File
import scala.util.Random

case class EvaluationOutput()

trait Language {
  val uid: String = s"${time.getMillis}_${Math.abs(Random.nextInt)}"
  def compile: Option[String] = None
  def run: String
  val owner: String
  val extension: String
  val filename: String = s"$uid.$extension"
  val aliases: List[String] = List()
  val requiredBinaries: List[String] = List()
  val time: DateTime = DateTime.now
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
class Sandbox(homeRoot: String, code: String, language: Language) {

  /** The current time in milliseconds, used as a uid. */
  private val timeInMillis = language.time.getMillis

  /** A date string, formatted for use in directory names. */
  private val directoryDateTime = DateTimeFormat.forPattern("yyyy-MM-dd").print(language.time)

  /** The home directory of the evaluation. */
  private val home = s"$homeRoot/${language.uid}"
  
  /** An exception that gets thrown when needed binaries don't exist in $PATH. */
  private case class MissingBinariesException(message: String) extends RuntimeException

  // TODO: Move these to config file.
  if (List("sandbox", "timeout").exists(program => !ScrubletsUtils.fileIsInPath(program))) {
    throw MissingBinariesException(
      "The programs 'sandbox' and 'timeout' are required and should be in $PATH.")
  }

  /** Set up the directory structure required for performing an evaluation.
    *
    * This is done separately from the constructor so that we can spawn
    * instances of Sandbox without actually performing any IO.
    *
    * @throws IOException if it can't make the required directories for any reason.
    */
  def createDirectories() {
    // Create the Sandbox's home directory.
    FileUtils.forceMkdir(new File(home))

    // And a place to store evaluation requests.
    FileUtils.forceMkdir(new File(s"$homeRoot/audit"))
  }

  /** Make a directory and its parent directories within the sandbox.
    *
    * @throws IOException if it can't make the required directory for any reason.
    * @param directory The name (or path) of the directory to create.
    *   Should be relative to the sandbox's home directory.
    */
  def mkdir(directory: String) {
    FileUtils.forceMkdir(new File(s"$home/$directory"))
  }

  /** Copy a file into the sandbox's home directory.
    *
    * @throws IOException if unable to copy the file.
    * @param source The path to the source file.
    * @param destination The path to the destination, relative to the sandbox's home.
    */
  def copy(source: String, destination: String) {
    val sourceFile = new File(source)
    val destinationFile = new File(s"$home/$destination")
    if (sourceFile.isFile) {
      FileUtils.copyFile(sourceFile, destinationFile, true)
    } else if (sourceFile.isDirectory) {
      FileUtils.copyDirectory(sourceFile, destinationFile, true)
    } else {
      throw new java.io.IOException("Source file was not a valid directory or file.")
    }
  }

  /** Destroy the sandbox's home directory. */
  def destroyHomeDirectory() {
    FileUtils.forceDelete(new File(home))
  }

  /** Check to make sure all required binaries exist. */
  def hasRequiredBinaries = {
    !language.requiredBinaries.exists(f => !ScrubletsUtils.fileIsInPath(f))
  }

  /** Keep a copy of the source code around for auditing later.
    *
    * This might one day be reimplemented by storing the code in a
    * database.
    *
    * This is done so that we can audit code later on to see what caused
    * a bug to be triggered.
    *
    * @throws IOException if unable to copy the source directory to audits.
    */
  def copySourceForAudit() {
    val dailyAuditDirectory = new File(s"$homeRoot/audit/${directoryDateTime}")
    FileUtils.forceMkdir(dailyAuditDirectory)
    FileUtils.copyDirectoryToDirectory(new File(home), dailyAuditDirectory)
  }
}
