package com.yykj.etl.loader

import java.io.File

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Amber on 2017/6/30.
  */
class CategoryLoader extends LazyLogging {
  def loadCategoryListFromDirectoryName(rootPath: String): Boolean = {
    logger.info("依据文件夹名读取分类列表...")
    val directory = new File(rootPath)
    if (!directory.isDirectory) {
      logger.error("依据文件夹名读取分类列表...失败 根目录不是文件夹")
      throw new Exception("train directory not a directory")
    }

    val categories = directory.listFiles().filter(_.isDirectory)
    logger.info("依据文件夹名读取分类列表...完成")
    categories.foreach(  // TODO )
  }
}
