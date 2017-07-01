package com.yykj.ml.textclassify

import java.io.{File, FileOutputStream, OutputStreamWriter}

import com.typesafe.scalalogging.LazyLogging
import com.yykj.etl.{FileName, FileReader, FileValidator}
import play.api.libs.json.Json

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by Amber on 2017/6/29.
  */

object TextClassifyContext extends LazyLogging {
  def open(workDirectory : String) : TextClassifyContext = {
    val instance = new TextClassifyContextImpl()
    instance.workDirectory(workDirectory)
    instance.loadPropertiesFromWorkDirectory()
    instance
  }

  def create(useLiblinearAsSvm : Boolean,
              maxFeatures : Int,
              loadCategoryListFromDirectoryName : String
            ) : TextClassifyContext = {
    val instance = new TextClassifyContextImpl()
    instance.useLiblinearAsSvm(useLiblinearAsSvm)
    instance.maxFeatures(maxFeatures)
    instance.loadCategoryListFromDirectoryName(loadCategoryListFromDirectoryName)
    instance
  }


  private class TextClassifyContextImpl extends TextClassifyContext with LazyLogging{

    def loadCategoryListFromDirectoryName(rootPath: String): Unit = {
      logger.info("依据文件夹名读取分类列表...")
      val directory = new File(rootPath)
      if (!directory.isDirectory) {
        logger.error("依据文件夹名读取分类列表...失败 根目录不是文件夹")
        throw new Exception("train directory not a directory")
      }

      val categoriesRead = directory.listFiles().filter(_.isDirectory)
      logger.info("依据文件夹名读取分类列表...完成")
      categoriesRead.foreach(
        o =>
        {
          val name = o.getName
          categories.append(name)
        }
      )
    }

    private val configFileName_Properties = "properties"
    def loadPropertiesFromWorkDirectory() : Unit = {
      FileValidator.validateDirectory(workDirectory)

      val configFilePath_Properties = FileName.concatPath(workDirectory,configFileName_Properties)
      FileValidator.validateFile(configFilePath_Properties)
      val propertiesJson = FileReader.readEntire(configFilePath_Properties)
      val propertiesJValue = Json.parse(propertiesJson)
      maxFeatures((propertiesJValue \ "maxFeatures").as[Int])
      useLiblinearAsSvm((propertiesJValue \ "useLiblinearAsSvm").as[Boolean])
      categories = (propertiesJValue \ "categories").as[List[String]].to[ListBuffer]

    }

    def savePropertiesToWorkDirectory() : Unit = {
      new File(workDirectory).mkdirs()

      val properties = Json.toJson(
        Map(
          "maxFeatures" -> Json.toJson(maxFeatures),
          "useLiblinearAsSvm" -> Json.toJson(useLiblinearAsSvm),
          "categories" -> Json.toJson(categories)
        )
      )

      val configFilePath_Properties = FileName.concatPath(workDirectory, configFileName_Properties)
      val configFileWriter_Properties = new OutputStreamWriter(new FileOutputStream(configFilePath_Properties, false), "UTF-8")
      configFileWriter_Properties.write(Json.stringify(properties))
      configFileWriter_Properties.flush()
      configFileWriter_Properties.close()

    }


  }
}

sealed trait TextClassifyContext {
  protected var categories : mutable.ListBuffer[String] = mutable.ListBuffer[String]()

  def categoryLength() : Int = categories.length

  def categoryName(index : Int) : String = categories(index)

  def categoryIndex(name : String) : Int = categories.indexOf(name)

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

  /**
    * 模型,配置目录
    */
  protected var _workDirectory : String = null
  def workDirectory(path : String) : Unit = {
    _workDirectory = path
  }
  def workDirectory = _workDirectory

  def savePropertiesToWorkDirectory() : Unit
}




