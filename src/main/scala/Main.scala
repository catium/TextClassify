
import com.yykj.etl.csv.CsvResolver
import com.yykj.ml.interfaces.thuctc.Demo


object Main {
  def main(args: Array[String]): Unit = {
    parseCsv()
  }

  def train(): Unit = {
    Demo.runTrainAndTest()
  }

  def use(): Unit = {
    Demo.runLoadModelAndUse("本实用新型公开了一种用于高电压环境中的无源无线温度传感器，包括一固定于被测物体上的导热块，该导热块上安装一线路板，该线路板底面设有声表面波温度传感器与导热块接触，该线路板上表面上安装有发射天线。本实用新型利用天线捕捉电磁波为声表面波温度传感器提供能量，声表面波温度传感器把检测到的温度信息转换成电磁波后通过天线发出，不需要外接电源及电源线，具有高可靠性和故障率低的优点。");


  }

  def parseCsv(): Unit = {
    val p = new CsvResolver("D:\\MLSpace\\ZL\\qd.csv", Seq(1,8), 14)
    p.resolveEachLineToSingleFile()
  }
}
