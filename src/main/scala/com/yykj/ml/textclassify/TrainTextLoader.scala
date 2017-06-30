package com.yykj.ml.textclassify

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.thunlp.language.chinese.LangUtils

import scala.collection.mutable
import scala.io.Source

/**
  * Created by Amber on 2017/6/30.
  */
class TrainTextLoader(loadFunction: (String, Int) => Unit, categoryMap : mutable.Map[String, Int]) extends LazyLogging {

  def loadFileAsCorpus(filePath : String , label : String) : Unit = {
    logger.info("读取训练文件...")
    val file = new File(filePath)
    if ( ! file.isFile) {
      logger.error("读取训练文件...失败 训练文件不是文件")
      throw new Exception("train file not a file")
    }
    if( ! categoryMap.contains(label))
    {
      logger.error("分类名不存在")
      throw new Exception("分类名不存在")
    }
    val labelInt = categoryMap(label)
    loadFileAsCorpus(file, labelInt)
  }

  protected def loadFileAsCorpus(file: File, label: Int) : Unit = {
    file.getName.split('.').last match {
      case "csv" => loadCsvToItemByLine(file, label)
      case  _ => loadTxtToItemByFile(file, label)
    }
  }


  private def trainerFilter(content: String): String = {
    var text = content
    if (text.length > 6003)
      text = text.substring(0, 6001)

    text = LangUtils.mapFullWidthLetterToHalfWidth(text)
    text = LangUtils.mapChineseMarksToAnsi(text)
    text = LangUtils.mapFullWidthNumberToHalfWidth(text)
    text = LangUtils.removeEmptyLines(text)
    text = LangUtils.removeExtraSpaces(text)

    val filteredText = text.toArray.filter(
      c => LangUtils.isChinese(c) || (c.toInt > 31 && c.toInt < 128)).mkString.trim

    LangUtils.T2S(filteredText)
  }


  private def loadTxtToItemByFile(file: File, label: Int) : Unit = {
    val content = Source.fromFile(file).getLines().mkString
    loadSingleItem(content, label)
  }

  private def loadCsvToItemByLine(file: File, label: Int) : Unit = {
    Source.fromFile(file).getLines().foreach(
      line =>
        loadSingleItem(line,label)
    )
  }

  private def loadSingleItem(content: String, label: Int) : Unit = {
    val filtered = trainerFilter(content)
    loadFunction(filtered, label)
  }


  private def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    if(these == null)
      Array()
    else
      these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def loadDirectoryAsCorpus(directoryPath : String) : Unit = {
    logger.info("读取训练列表...")
    val directory = new File(directoryPath)
    if (!directory.isDirectory) {
      logger.error("读取训练列表...失败 训练列表不是文件夹")
      throw new Exception("train directory not a directory")
    }

    val categories = directory.listFiles().filter(_.isDirectory)
    logger.info("读取训练列表...完成")
    categories.foreach(loadFilesForEachCaregroy)


  }


  private def loadFilesForEachCaregroy(categoryDirectory : File) : Unit = {
    logger.info("读取训练列表中的下一个分类...")
    if (!categoryDirectory.isDirectory) {
      logger.error("这不可能!")
      throw new Exception("category directory not a directory")
    }
    val categoryName = categoryDirectory.getName
    logger.info("读取训练列表中的下一个分类...成功.分类名称:" + categoryName)
    if(!categoryMap.contains(categoryName))
    {
      logger.error("分类名不存在,是否在程序执行中变动了训练文件夹?")
    }
    else
    {
      val categoryInt = categoryMap(categoryName)
      val files = recursiveListFiles(categoryDirectory)
      logger.error("分类下找到了" + files.length + "个文件")
      files.foreach(loadFileAsCorpus(_,categoryInt))
    }
  }
}
