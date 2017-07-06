package com.yykj.ml.textclassify

/**
  * Created by Amber on 2017/7/1.
  */
trait TextClassifyBusiness {
  def trainDirectory(directoryPath : String) : Unit
  def trainFile(filePath : String, label : String) : Unit
  def classifyText(text : String, maxResult : Int ) : Array[(String, Double)]
  def classifyFile(filePath : String, maxResult : Int ) : Array[(String, Double)]

  def getContext(): TextClassifyContext
}