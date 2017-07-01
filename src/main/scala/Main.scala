
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
    business.classifyText("本实用新型公开了一种用于高电压环境中的无源无线温度传感器，包括一固定于被测物体上的导热块，该导热块上安装一线路板，该线路板底面设有声表面波温度传感器与导热块接触，该线路板上表面上安装有发射天线。本实用新型利用天线捕捉电磁波为声表面波温度传感器提供能量，声表面波温度传感器把检测到的温度信息转换成电磁波后通过天线发出，不需要外接电源及电源线，具有高可靠性和故障率低的优点。",3)

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
