
import com.yykj.etl.CsvResolver
import com.yykj.ml.interfaces.thuctc.Demo
import com.yykj.ml.textclassify.TextClassifyProject



object Main {


  def main(args: Array[String]): Unit = {
    use()
  }

  def train(): Unit = {
    new TextClassifyProject().newProject(
      "D:\\MLSpace\\ZL",
      "D:\\MLSpace\\model",
      true,
      50000
    )
  }

  def use(): Unit = {
    new TextClassifyProject().openProject(
      "D:\\MLSpace\\model",
      true,
      "39b7727a-55bc-496b-8708-f3f86a60a0b1"
    )


  }

  def parseCsv(): Unit = {
    val p = new CsvResolver("D:\\MLSpace\\ZL\\qd.csv", Seq(1,8), 14)
    p.resolveEachLineToSingleFile()
  }



  def haha(implicit s:String) : Unit = {println(s)}
}
