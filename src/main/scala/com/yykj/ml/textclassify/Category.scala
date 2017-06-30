package com.yykj.ml.textclassify

import scala.io.Source

import scala.collection.mutable

/**
  * Created by Amber on 2017/6/29.
  */
class Category {
  val categoryList : mutable.ListBuffer[String] = mutable.ListBuffer[String]()
  val categoryToInt : mutable.Map[String, Int] = mutable.Map[String, Int]()

  def loadCategoryListFromFile(filePath: String):
  Unit = {
    categoryList.clear()
    categoryToInt.clear()

    Source.fromFile(String).getLines().foreach(
      o =>
        categoryList.append(o)
    )

    var i : Int = -1
    categoryList.foreach(
      o =>
        {
          i += 1
          categoryToInt += (o -> i)
        }
    )

    println("--------------------------------\nCategory List:")

    (categoryList,categoryToInt).zipped.foreach(
      (x,y) =>
        println(x + "\t" + y))

    println("--------------------------------")
  }
}
