package com.yykj.ml.textclassify

import java.io.{File, FileOutputStream, OutputStreamWriter}

import com.typesafe.scalalogging.StrictLogging

import scala.io.Source
import scala.collection.mutable

/**
  * Created by Amber on 2017/6/29.
  */
class Category extends StrictLogging {
  val categoryList : mutable.ListBuffer[String] = mutable.ListBuffer[String]()
  val categoryToInt : mutable.Map[String, Int] = mutable.Map[String, Int]()

  def loadCategoryListFromDirectoryName(rootPath: String): Unit = {
    logger.info("依据文件夹名读取分类列表...")
    val directory = new File(rootPath)
    if (!directory.isDirectory) {
      logger.error("依据文件夹名读取分类列表...失败 根目录不是文件夹")
      throw new Exception("train directory not a directory")
    }

    val categories = directory.listFiles().filter(_.isDirectory)
    logger.info("依据文件夹名读取分类列表...完成")
    var i : Int = -1
    categories.foreach(
      o =>
        {
          i += 1
          val name = o.getName
          categoryList.append(name)
          categoryToInt += (name -> i)
        }
    )
  }

  def loadCategoryListFromFile(filePath: String): Unit = {
    categoryList.clear()
    categoryToInt.clear()

    Source.fromFile(filePath).getLines().foreach(
      o =>
        categoryList.append(o)
    )

    var i : Int = -1
    categoryList.foreach(
      o =>
      {
        i += 1
        categoryToInt += (o -> i)
      }
    )

    logger.info("--------------------------------\nCategory List:")

    (categoryList,categoryToInt).zipped.foreach(
      (x,y) =>
        logger.info(x + "\t" + y))

    logger.info("--------------------------------")
  }

  def saveCategoryListToFile(filePath: String): Unit = {
    val writer = new OutputStreamWriter(new FileOutputStream(filePath, false), "UTF-8")
    categoryList.foreach(writer.write)
    writer.flush()
    writer.close()
  }
}
