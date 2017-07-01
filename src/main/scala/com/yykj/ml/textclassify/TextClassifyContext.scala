package com.yykj.ml.textclassify

import java.io.{File, FileOutputStream, OutputStreamWriter}

import com.typesafe.scalalogging.LazyLogging
import com.yykj.etl.{FileReader, FileValidator}
import scala.collection.mutable

/**
  * Created by Amber on 2017/6/29.
  */

class TextClassifyContext extends LazyLogging{
  protected val categoryList : mutable.ListBuffer[String] = mutable.ListBuffer[String]()
  //val categoryToInt : mutable.Map[String, Int] = mutable.Map[String, Int]()

  def categoryLength() : Int = categoryList.length

  def categoryName(index : Int) : String = categoryList(index)

  def categoryIndex(name : String) : Int = categoryList.indexOf(name)

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
        //categoryToInt += (name -> i)
      }
    )
  }

  private def loadCategoryListFromFile(filePath: String): Unit = {
    categoryList.clear()

    FileReader.readLines(filePath).foreach(
      o =>
        categoryList.append(o)
    )

    var i : Int = -1
    categoryList.foreach(
      o =>
      {
        i += 1
        logger.info(i + "\t" + categoryList(i))
      }
    )

  }

  def saveCategoryListToFile(filePath: String): Unit = {
    val writer = new OutputStreamWriter(new FileOutputStream(filePath, false), "UTF-8")
    categoryList.foreach(
      o =>
        writer.write(o+"\n")
    )
    writer.flush()
    writer.close()
  }

  private val configFileName_MaxFeatures = "configMaxFeatures"
  private val configFileName_Categories = "configCategories"
  def loadConfigurationFromFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures
    FileValidator.validateFile(configFilePath_MaxFeatures)
    val maxFeaturesFromFile = FileReader.readEntire(configFilePath_MaxFeatures).toInt
    maxFeatures(maxFeaturesFromFile)

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories
    FileValidator.validateFile(configFilePath_Categories)
    loadCategoryListFromFile(configFilePath_Categories)
  }

  def saveConfigurationToFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures

    val configFileWriter_MaxFeatures = new OutputStreamWriter(new FileOutputStream(configFilePath_MaxFeatures, false), "UTF-8")
    configFileWriter_MaxFeatures.write(maxFeatures.toString)
    configFileWriter_MaxFeatures.flush()
    configFileWriter_MaxFeatures.close()

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories

    saveCategoryListToFile(configFilePath_Categories)
  }

  /**
    * 最大特征数
    */
  protected var _maxFeatures: Int = -1
  def maxFeatures(max : Int) : Unit = {
    if(max <= 0)
      throw new Exception("最大特征必须>0")
    _maxFeatures = max
  }
  def maxFeatures = _maxFeatures

  /**
    * svm使用liblinear
    * false to set as libsvm
    */
  protected var _useLiblinearAsSvm = true
  def useLiblinearAsSvm(isUse : Boolean) : Unit = {
    _useLiblinearAsSvm = isUse
  }
  def useLiblinearAsSvm = _useLiblinearAsSvm


}


