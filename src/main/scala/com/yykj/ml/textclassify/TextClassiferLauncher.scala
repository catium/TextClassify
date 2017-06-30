package com.yykj.ml.textclassify

import org.thunlp.text.classifiers.{BigramChineseTextClassifier, LinearBigramChineseTextClassifier, TextClassifier}

/**
  * Created by Amber on 2017/6/30.
  */
class TextClassiferLauncher(context : TextClassifierContext) {

  val classifier : TextClassifier = {
    val categorySize = context.category.categoryList.length
    context.useLiblinearAsSvm match {
      case true => new LinearBigramChineseTextClassifier(categorySize)
      case false => new BigramChineseTextClassifier(categorySize)
    }
  }

  private val corpusLoader = new TrainTextLoader(classifier.addTrainingText, context.category.categoryToInt)

  def init() : Unit = {
    if(context.maxFeatures > 0)
      classifier.setMaxFeatures(context.maxFeatures)
  }

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
}
