package com.yykj.etl.file

import java.io._

import org.thunlp.io.TextFileReader

/**
  * Created by Amber on 2017/6/29.
  */

object TextReader {
}

class TextReader(file: File, encode: String) {

  @throws[IOException]
  private[io] var br : BufferedReader = new BufferedReader(
    new InputStreamReader(new FileInputStream(file), encode)
  )

  def this(filename: String, encode: String) {
    this(new File(filename), String)
  }

  def this(filename: String) {
    this(filename, "UTF-8")
  }

  @throws[IOException]
  def readLines: String = br.readLine()

  @throws[IOException]
  def close(): Unit = {
    br.close()
  }

}
