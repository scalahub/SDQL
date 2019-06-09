
package sdql.util

import dql.DQLUtil
import java.io.File
import org.sh.easyweb.Text
import scala.util.Random._
import trap.file.Util._

object FileUtil {
  ///////////////////
  val systemTmp = System.getProperty("java.io.tmpdir")
  val tmpDir = DQLUtil.dqlDir //systemTmp+"/jdql"+nextInt.abs
  org.sh.utils.file.Util.createDir(systemTmp)
  org.sh.utils.file.Util.createDir(tmpDir)

  if (new java.io.File(tmpDir).isFile) throw new Exception(tmpDir+" must be a directory")
  def deleteRecursive(f:File):Boolean = if (f.isFile) f.delete else {
    f.listFiles.map(deleteRecursive).foldLeft(true)((x, y) => x && y) && f.delete
  }
  implicit def toFile(text:Text) = {
    val tmpFile = tmpDir+"/"+nextInt.abs+".txt"
    writeToTextFile(tmpFile, text.getText)
    new File(tmpFile)
  }
  def getSubFiles(file:File):Array[String] = 
    if (file.isDirectory) file.listFiles.flatMap (getSubFiles(_))
    else Array(file.getPath)
  def getSubDirs(file:File):Array[String] = 
    if (file.isDirectory) file.getPath +: file.listFiles.flatMap (getSubDirs(_))
    else Array()
}
