package com.yykj.ml.textclassify

import com.typesafe.scalalogging.StrictLogging
import com.yykj.etl.file.FileValidator
import org.thunlp.text.classifiers.{BigramChineseTextClassifier, LinearBigramChineseTextClassifier, TextClassifier}

import scala.io.Source

/**
  * Created by Amber on 2017/6/30.
  */
class TextClassiferLauncher(context : TextClassifierContext) extends StrictLogging {

  val classifier : TextClassifier = {
    val categorySize = context.category.categoryList.length
    val newClassifier = context.useLiblinearAsSvm match {
      case true => new LinearBigramChineseTextClassifier(categorySize)
      case false => new BigramChineseTextClassifier(categorySize)
    }
    if(context.maxFeatures > 0)
      newClassifier.setMaxFeatures(context.maxFeatures)
    newClassifier
  }

  private val corpusLoader = new TrainTextLoader(classifier.addTrainingText, context.category.categoryToInt)


  def trainDirectory(directoryPath : String) : Unit = {
    corpusLoader.loadDirectoryAsCorpus(directoryPath)
  }

  def trainFile(filePath : String, label : String) : Unit = {
    corpusLoader.loadFileAsCorpus(filePath, label)
  }


  def loadModelAndConfiguration(fileDirectory : String) : Unit = {
    context.loadConfigurationFromFile(fileDirectory)
    classifier.loadModel(fileDirectory)
  }

  def saveModelAndConfiguration(fileDirectory : String) : Unit = {
    context.saveConfigurationToFile(fileDirectory)
    classifier.saveModel(fileDirectory)
  }

  def classifyText(text : String, maxResult : Int ) : Unit = {
    val result = classifier.classify(text, maxResult)
    var i = -1
    result.foreach(
      o =>
        {
          i += 1
          val categoryName = context.category.categoryList(o.label)
          logger.info(categoryName + "\t" + o.prob)
        }
      )
  }

  def classifyFile(filePath : String, maxResult : Int ) : Unit = {
    FileValidator.validateFile(filePath)
    val text = Source.fromFile(filePath).getLines().mkString
    classifyText(text, maxResult)
  }
}
