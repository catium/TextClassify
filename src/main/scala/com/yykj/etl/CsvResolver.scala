package com.yykj.etl

import java.io.{File, FileOutputStream, OutputStreamWriter}

import com.github.tototoshi.csv._

/**
  * Created by Amber on 2017/6/29.
  */
class CsvResolver(csvFileName : String, contentAt : Seq[Int], labelAt : Int) {

  private val workingDirectory : String = {
     new File(csvFileName).getParent
  }

  def resolveEachLineToSingleFile() : Unit = {
    val file = new File(csvFileName)
    val reader = CSVReader.open(file)
    reader.foreach(processOneLineInCsv)
    reader.close()
  }

  private def processOneLineInCsv(line : Seq[String]) : Unit = {
    val label = createLabel(line)
    val content = createConcatedContent(line)
    val fullFileName = createRandomFileNameWithFullPathByLabel(label)
    writeContentToSplitedFile(fullFileName, content)
  }

  private def createConcatedContent(csvLine : Seq[String]) : String = {
    val contentBuffer = new StringBuffer
    contentAt.foreach(index => contentBuffer.append(csvLine(index) + "\r\n"))
    contentBuffer.toString
  }

  private def createLabel(line : Seq[String]) : String = {
    var label = line(labelAt)
    if(label == "") label = "undefined"
    label
  }

  private def createRandomFileNameWithFullPathByLabel(label : String ): String = {
    val uuid = java.util.UUID.randomUUID.toString
    workingDirectory + File.separator + label + File.separator + uuid + ".txt"
  }

  private def writeContentToSplitedFile(fileName : String , content : String ): Unit = {
    val newFile = new File(fileName)
    newFile.getParentFile.mkdirs()
    newFile.createNewFile()
    println("writing "+ fileName)

    val writer = new OutputStreamWriter(new FileOutputStream(newFile, false), "UTF-8")
    writer.write(content)
    writer.flush()
    writer.close()
  }
}
