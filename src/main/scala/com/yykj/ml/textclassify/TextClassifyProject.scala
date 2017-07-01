package com.yykj.ml.textclassify

import java.io.File
import java.nio.file.Paths

import com.typesafe.scalalogging.LazyLogging
import com.yykj.etl.{FileName, FileValidator}

/**
  * Created by Amber on 2017/7/1.
  */
class TextClassifyProject extends LazyLogging {
  protected var projectGuid : String = null
  protected var controller : TextClassifyBusinessImpl = null
  def newProject(
      corpusRoot : String,
      useLiblinearAsSvm : Boolean,
      maxFeatures : Int
  ) : String = {
    projectGuid = java.util.UUID.randomUUID.toString

    val context = TextClassifyContext.create(useLiblinearAsSvm,maxFeatures,corpusRoot)
    logger.info("新工程配置完毕")
    controller = new TextClassifyBusinessImpl(context)
    controller.trainDirectory(corpusRoot)

    logger.info("新工程训练完毕")
    projectGuid
  }

  def openProject(
                  modelRoot : String,
                  guid : String
                 ) : Unit = {
    projectGuid = guid

    val projectPath = FileName.concatPath(modelRoot, projectGuid)
    val context = TextClassifyContext.open(projectPath)
    logger.info("工程配置读取完毕")
    controller = new TextClassifyBusinessImpl(context)
    logger.info("工程打开完毕")
     }

  def saveProject(workDirectory : String = null) : Unit = {
    if(projectGuid == null || controller == null) throw new Exception("未打开工程")
    logger.info("正在保存...")
    var saveDirectory = workDirectory
    if(saveDirectory != null)
      {
        if(saveDirectory.endsWith(File.separator))
          saveDirectory = saveDirectory.substring(0, saveDirectory.length - File.separator.length)
        if( ! saveDirectory.endsWith(projectGuid))
          saveDirectory = FileName.concatPath(saveDirectory, projectGuid)
      }
    controller.saveModelAndProperties(saveDirectory)
    logger.info("保存完毕...")

  }

  def business() : TextClassifyBusiness = controller
}
