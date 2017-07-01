package com.yykj.ml.textclassify

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import com.yykj.etl.FileValidator

/**
  * Created by Amber on 2017/7/1.
  */
class TextClassifyProject extends LazyLogging {
  def newProject(
      corpusRoot : String,
      modelRoot : String,
      useLiblinearAsSvm : Boolean,
      maxFeatures : Int
  ) : String = {
    val uuid = java.util.UUID.randomUUID.toString

    val context = new TextClassifyContext()
    context.useLiblinearAsSvm(useLiblinearAsSvm)
    context.maxFeatures(maxFeatures)
    context.loadCategoryListFromDirectoryName(corpusRoot)
    logger.info("新工程配置完毕")
    val controller = new TextClassifyController(context)
    controller.trainDirectory(corpusRoot)



    val path = new File(modelRoot, uuid)
    path.mkdirs()
    controller.saveModelAndConfiguration(path.getPath)
    logger.info("新工程训练完毕")
    uuid
  }

  def openProject(
                  modelRoot : String,
                  useLiblinearAsSvm : Boolean,
                  projectGuid : String
                 ) : Unit = {
    val context = new TextClassifyContext()
    context.useLiblinearAsSvm(useLiblinearAsSvm)


    val projectPath = new File(modelRoot, projectGuid).getPath
    val controller = new TextClassifyController(context)
    logger.info("新工程读取完毕")
    controller.loadModelAndConfiguration(projectPath)
    controller.classifyText("本实用新型公开了一种用于高电压环境中的无源无线温度传感器，包括一固定于被测物体上的导热块，该导热块上安装一线路板，该线路板底面设有声表面波温度传感器与导热块接触，该线路板上表面上安装有发射天线。本实用新型利用天线捕捉电磁波为声表面波温度传感器提供能量，声表面波温度传感器把检测到的温度信息转换成电磁波后通过天线发出，不需要外接电源及电源线，具有高可靠性和故障率低的优点。",3)
  }
}
