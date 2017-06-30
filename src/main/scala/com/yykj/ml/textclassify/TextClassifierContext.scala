package com.yykj.ml.textclassify

import java.io.{File, FileOutputStream, OutputStreamWriter}
import java.util
import java.util.{ArrayList, Hashtable}

import com.yykj.etl.file.FileValidator
import org.thunlp.text.classifiers.{BigramChineseTextClassifier, LinearBigramChineseTextClassifier, TextClassifier}

import scala.io.Source

/**
  * Created by Amber on 2017/6/29.
  */

class TextClassifierArgumentResolver {
  val usage = """org.thunlp.text.classifiers.BasicTextClassifier \n
  [-c CATEGORY_LIST_FILE_PATH]\t从文件中读入类别信息。该文件中每行包含且仅包含一个类别名称。\n"
  [-train TRAIN_PATH]\t进行训练，并设置训练语料文件夹路径。该文件夹下每个子文件夹的名称都对应一个类别名称，内含属于该类别的训练语料。若不设置，则不进行训练。\n"
  [-eval EVAL_PATH]\t进行评测，并设置评测语料文件夹路径。该文件夹下每个子文件夹的名称都对应一个类别名称，内含属于该类别的评测语料。若不设置，则不进行评测。也可以使用-test。\n"
  [-classify FILE_PATH]\t对一个文件进行分类。\n"
  [-n topN]\t设置返回候选分类数，按得分大小排序。默认为1，即只返回最可能的分类。\n"
  [-svm libsvm or liblinear]\t选择使用libsvm还是liblinear进行训练和测试，默认使用liblinear\n"
  [-l LOAD_MODEL_PATH]\t设置读取模型路径。\n
  [-s SAVE_MODEL_PATH]\t设置保存模型路径。\n
  [-f FEATURE_SIZE]\t设置保留特征数目，默认为5000。\n
  [-d1 RATIO]\t设置训练集占总文件数比例，默认为0.8。\n
  [-d2 RATIO]\t设置测试集占总文件数比例，默认为0.2。\n
  [-e ENCODING]\t设置训练及测试文件编码，默认为UTF-8。\n
  [-filter SUFFIX]\t设置文件后缀过滤。例如设置“-filter .txt”，则训练和测试时仅考虑文件名后缀为.txt的文件。\n
  """

}

class TextClassifierContext {
  val category = new Category()

  /**
    * 是否已读取模型
    */
  private var modelLoaded = false
  /**
    * 词典大小
    */
  private var lexiconSize = 0
  /**
    * 返回的可能分类数目
    */
  protected var resultNum = 1
  /**
    * 分类器接口
    */
  protected var classifier = null




  /**
    * 分类器应用路径
    */
  protected var classifyPath = null

  /**
    * 训练目录编号
    */
  protected var trainingPathIndex: Int = -1
  /**
    * 测试目录编号
    */
  protected var testingPathIndex: Int = -1
  /**
    * 训练文件使用比例
    */
  protected var ratio1 = 0.8
  /**
    * 测试文件使用比例
    */
  protected var ratio2 = 0.2

  /**
    * 文件编码
    */
  protected var encoding = "utf-8"
  /**
    * 后缀过滤
    */
  protected var suffix = null
  /**
    * 是否打印细节
    */
  protected var printDetail = false

  protected var index = 0

  private val configFileName_MaxFeatures = "configMaxFeatures"
  private val configFileName_Categories = "configCategories"
  def loadConfigurationFromFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures
    FileValidator.validateFile(configFilePath_MaxFeatures)
    val maxFeaturesFromFile = Source.fromFile(configFilePath_MaxFeatures).getLines().mkString.toInt
    maxFeatures(maxFeaturesFromFile)

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories
    FileValidator.validateFile(configFilePath_MaxFeatures)
    category.loadCategoryListFromFile(configFilePath_Categories)
  }

  def saveConfigurationToFile(configDirectory : String) : Unit = {
    FileValidator.validateDirectory(configDirectory)

    val configFilePath_MaxFeatures = configDirectory + File.separator + configFileName_MaxFeatures
    FileValidator.validateFile(configFilePath_MaxFeatures)
    val configFileWriter_MaxFeatures = new OutputStreamWriter(new FileOutputStream(configFilePath_MaxFeatures, false), "UTF-8")
    configFileWriter_MaxFeatures.write(maxFeatures.toString)
    configFileWriter_MaxFeatures.flush()
    configFileWriter_MaxFeatures.close()

    val configFilePath_Categories = configDirectory + File.separator + configFileName_Categories
    FileValidator.validateFile(configFilePath_MaxFeatures)
    category.saveCategoryListToFile(configFilePath_Categories)
  }


  /**
    * 最大特征数
    */
  protected var _maxFeatures: Int = -1
  def maxFeatures(max : Int) : Unit = {
    if(max <= 0)
      throw new Exception("最大特征必须>0")
    _maxFeatures = max
  }
  def maxFeatures = _maxFeatures

  /**
    * svm使用liblinear
    * false to set as libsvm
    */
  protected var _useLiblinearAsSvm = true
  def useLiblinearAsSvm(isUse : Boolean) : Unit = {
    _useLiblinearAsSvm = isUse
  }
  def useLiblinearAsSvm = _useLiblinearAsSvm

  /**
    * 读取模型路径
    */
  protected var _modelPathLoad : String = null
  def modelPathLoad(folder : String) : Unit = {
    FileValidator.validateDirectory(folder)
    _modelPathLoad = folder
  }
  def modelPathLoad = _modelPathLoad

  /**
    * 保存模型路径
    */
  protected var _modelPathSave : String = null
  def modelPathSave(folder : String) : Unit = {
    FileValidator.validateDirectory(folder)
    _modelPathSave= folder
  }
  def modelPathSave = _modelPathSave



  /**
    * 训练语料路径
    */
  var trainingFolder : String = null
  def setTrainingFolder(folder : String) : Unit = {
    FileValidator.validateDirectory(folder)
    trainingFolder = folder
  }

  /**
    * 测试语料路径
    */
  var testingFolder : String = null
  def setTestingFolder(folder : String) : Unit = {
    FileValidator.validateDirectory(folder)
    testingFolder = folder
  }




}

else if ("-classify".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -classify Error!");
        classifyPath = args[i + 1];
        if (!new File(classifyPath).exists())
          exit("Can't find classify FILE_PATH: " + classifyPath);
        i ++;
      } else if ("-n".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -n topN Error!");
        try {
          resultNum = Integer.parseInt(args[i + 1]);
        } catch (Exception e) {
          exit("-n topN  needs an INTEGER input!");
        }
        i ++;
      } else if ("-f".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -f FEATURE_SIZE Error!");
        try {
          maxFeatures = Integer.parseInt(args[i + 1]);
        } catch (Exception e) {
          exit("-f FEATURE_SIZE  needs an INTEGER input!");
        }
        i ++;
      } else if ("-d1".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -d1 RATIO_TRAIN Error!");
        try {
          ratio1 = Double.parseDouble(args[i + 1]);
        } catch (Exception e) {
          exit("-d1 RATIO_TRAIN  needs an DOUBLE input!");
        }
        i ++;
      } else if ("-d2".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -d2 RATIO_TEST Error!");
        try {
          ratio2 = Double.parseDouble(args[i + 1]);
        } catch (Exception e) {
          exit("-d2 RATIO_TRAIN  needs an DOUBLE input!");
        }
        i ++;
      } else if ("-e".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -e ENCODING Error!");
        encoding = args[i + 1];
        i ++;
      } else if ("-filter".equals(args[i])) {
        if (i + 1 >= args.length)
          exit("loading -filter SUFFIX Error!");
        suffix = args[i + 1];
        i ++;
      } else if ("-print".equals(args[i])) {
        printDetail = true;
      }
    }

    /****************************************************************************************/
    if (!loadCategoryListFromFolder(trainingFolder) && loadModelPath != null)
      loadCategoryListFromFile(loadModelPath + File.separator + "category");

    if (categoryList.size() == 0 && (testingFolder != null || loadModelPath != null))
      exit("Category list NOT LOADED !!! \nUse [-c CATEGORY_LIST_FILE_PATH] ");
}
