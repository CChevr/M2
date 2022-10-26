package fr.uge.scala

import org.apache.spark.{SparkConf, SparkContext}

object SparkRunner extends App {
    val conf = new SparkConf().setAppName("simpleSparkApp").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd1 = sc.parallelize(Array(1, 2, 3, 4)).map(x => x * x)
    rdd1.foreach(println(_))
}