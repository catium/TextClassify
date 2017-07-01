package com.yykj.ml.textclassify

import java.io.{File, FileOutputStream, OutputStreamWriter}
import java.util
import java.util.{ArrayList, Hashtable}

import com.yykj.etl.file.FileValidator
import org.thunlp.text.classifiers.{BigramChineseTextClassifier, LinearBigramChineseTextClassifier, TextClassifier}

import scala.io.Source

/**
  * Created by Amber on 2017/6/29.
  */

class TextClassifierContext {
  val category = new Category()

  private val configFileName_MaxFeatures = "configMaxFeatures"
  private val configFileName_Categories = "configCategories"
  def loadConfigurationFromFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures
    FileValidator.validateFile(configFilePath_MaxFeatures)
    val maxFeaturesFromFile = Source.fromFile(configFilePath_MaxFeatures).getLines().mkString.toInt
    maxFeatures(maxFeaturesFromFile)

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories
    FileValidator.validateFile(configFilePath_MaxFeatures)
    category.loadCategoryListFromFile(configFilePath_Categories)
  }

  def saveConfigurationToFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures
    FileValidator.validateFile(configFilePath_MaxFeatures)
    val configFileWriter_MaxFeatures = new OutputStreamWriter(new FileOutputStream(configFilePath_MaxFeatures, false), "UTF-8")
    configFileWriter_MaxFeatures.write(maxFeatures.toString)
    configFileWriter_MaxFeatures.flush()
    configFileWriter_MaxFeatures.close()

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories
    FileValidator.validateFile(configFilePath_MaxFeatures)
    category.saveCategoryListToFile(configFilePath_Categories)
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


