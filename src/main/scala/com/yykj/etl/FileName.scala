package com.yykj.etl

import java.nio.file.Paths

/**
  * Created by Amber on 2017/7/2.
  */
object FileName {
  def concatPath(item : String, items : String*) : String = {
    Paths.get(item, items: _*).toString
  }
}
