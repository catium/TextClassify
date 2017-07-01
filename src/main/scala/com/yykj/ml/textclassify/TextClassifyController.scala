package com.yykj.ml.textclassify

import com.typesafe.scalalogging.LazyLogging
import com.yykj.etl.{FileReader, FileValidator}
import org.thunlp.text.classifiers.{BigramChineseTextClassifier, LinearBigramChineseTextClassifier, TextClassifier}


/**
  * Created by Amber on 2017/6/30.
  */
class TextClassifyController(context : TextClassifyContext) extends LazyLogging {

  val classifier : TextClassifier = {
    val categorySize = context.categoryLength()
    val newClassifier = context.useLiblinearAsSvm match {
      case true => new LinearBigramChineseTextClassifier(categorySize)
      case false => new BigramChineseTextClassifier(categorySize)
    }
    if(context.maxFeatures > 0)
      newClassifier.setMaxFeatures(context.maxFeatures)
    newClassifier
  }

  private val corpusLoader = new TrainTextLoader(classifier.addTrainingText, context.categoryIndex)


  def trainDirectory(directoryPath : String) : Unit = {
    corpusLoader.loadDirectoryAsCorpus(directoryPath)
    classifier.train()
  }

  def trainFile(filePath : String, label : String) : Unit = {
    corpusLoader.loadFileAsCorpus(filePath, label)
    classifier.train()
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
          val categoryName = context.categoryName(o.label)
          logger.info(categoryName + "\t" + o.prob)
        }
      )
  }

  def classifyFile(filePath : String, maxResult : Int ) : Unit = {
    FileValidator.validateFile(filePath)
    val text = FileReader.readEntire(filePath)
    classifyText(text, maxResult)
  }
}
