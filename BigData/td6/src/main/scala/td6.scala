package fr.uge.scala

import org.apache.spark.{SparkConf, SparkContext}

object td6 extends App {
  val conf = new SparkConf().setAppName("simpleSparkApp").setMaster("local")
  val sc = new SparkContext(conf)
  val drugs = sc.textFile("med.txt").map(x => (x(1), x(2), x(7)))
  drugs.foreach(println)
}
