package com.yykj.etl

import java.io.File

import scala.collection.Iterator
import scala.io.{Codec, Source}


/**
  * Created by Amber on 2017/7/1.
  */
object FileReader {
  val codec = Codec("UTF-8")

  def readEntire(path : String) : String = {

    Source.fromFile(path)(codec).mkString
  }

  def readEntire(file: File) : String = {
    Source.fromFile(file)(codec).mkString
  }

  def readLines(path : String) : Iterator[String] = {
    Source.fromFile(path)(codec).getLines()
  }

  def readLines(file: File) : Iterator[String] = {
    Source.fromFile(file)(codec).getLines()
  }
}
