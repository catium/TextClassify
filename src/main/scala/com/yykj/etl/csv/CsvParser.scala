package com.yykj.etl.csv

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import com.github.tototoshi.csv._

/**
  * Created by Amber on 2017/6/29.
  */
class CsvParser(csvFileName : String, contentAt : Seq[Int], labelAt : Int) {

  var workingDirectory : String = {
     new File(csvFileName).getParent()
  }

  def parse() : Unit = {
    val file = new File(csvFileName)
    val reader = CSVReader.open(file)
    reader.foreach(processOneLineInCsv)
    reader.close()
  }

  def processOneLineInCsv(line : Seq[String]) : Unit =
  {
    val contentBuffer = new StringBuffer
    contentAt.foreach(index => contentBuffer.append(line(index) + "\r\n"))
    var label = line(labelAt)
    if(label == "") label = "undefined"
    val uuid = java.util.UUID.randomUUID.toString
    val fullFileName = workingDirectory + File.separator + label + File.separator + uuid + ".txt"
    val newFile = new File(fullFileName)
    newFile.getParentFile().mkdirs()
    newFile.createNewFile()
    println("writing "+fullFileName)

    val writer = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8")
    writer.write(contentBuffer.toString)
    writer.flush()
    writer.close()
  }

}
