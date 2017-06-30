package com.yykj.etl.file

import java.io.File

/**
  * Created by Amber on 2017/6/29.
  */
object FileValidator {
  def validateFile(filePath : String) : File = {
    if (filePath == null)
      throw new Exception("path is null")
    var file = new File(filePath)
    if(file.isDirectory){
      throw new Exception("try to open a file but it is a directory")
    }
    if(!file.exists()){
      throw new Exception("file not exists")
    }

    file
  }

  def validateDirectory(directoryPath : String) : File = {
    if (directoryPath == null)
      throw new Exception("path is null")
    var file = new File(directoryPath)
    if(!file.isDirectory){
      throw new Exception("try to open a directory but it is a file")
    }
    if(!file.exists()){
      throw new Exception("directory not exists")
    }

    file
  }
}
