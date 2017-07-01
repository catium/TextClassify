
import java.io.File

import com.yykj.etl.{CsvResolver, FileName}
import com.yykj.ml.textclassify.TextClassifyProject



object Main {


  def main(args: Array[String]): Unit = {
    use()
  }

  def train(): Unit = {
    val project = new TextClassifyProject()
    val guid = project.newProject(
      "D:\\MLSpace\\ZL",
      true,
      50000
    )
    project.saveProject("D:\\MLSpace\\model")


  }

  def use(): Unit = {
    val project = new TextClassifyProject()
    project.openProject(
      "D:\\MLSpace\\model",
      "5fd92b11-ed09-4c03-895f-e3761749ebaa"
    )
    val business = project.business()
    business.classifyText("---",3)

    project.saveProject()
  }

  def parseCsv(): Unit = {
    val p = new CsvResolver("D:\\MLSpace\\ZL\\qd.csv", Seq(1,8), 14)
    p.resolveEachLineToSingleFile()
  }

  def testhaha() : Unit = {
    println(haha("D:\\MLSpace\\model"))
    println(haha("D:\\MLSpace\\model\\"))
    println(haha("D:\\MLSpace\\model\\e10cf2f7-fcf4-4d69-a3ca-b9a7d0021a73"))
    println(haha("D:\\MLSpace\\model\\e10cf2f7-fcf4-4d69-a3ca-b9a7d0021a73\\"))
    println(haha("D:\\MLSpace\\model\\e10cf2f7-fcf4-4d69-a3ca-b9a7d0021a73\\3443"))
  }

  def haha(workDirectory: String) : String = {
    val projectGuid = "e10cf2f7-fcf4-4d69-a3ca-b9a7d0021a73"
    var saveDirectory = workDirectory
    if(saveDirectory != null)
    {
      if(saveDirectory.endsWith(File.separator))
        saveDirectory = saveDirectory.substring(0, saveDirectory.length - File.separator.length)
      if( ! saveDirectory.endsWith(projectGuid))
        saveDirectory = FileName.concatPath(saveDirectory, projectGuid)
    }
    saveDirectory
  }
}
