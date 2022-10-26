package fr.uge.scala

import org.apache.spark.{SparkConf, SparkContext}

object td6 extends App {
  val conf = new SparkConf().setAppName("simpleSparkApp").setMaster("local")
  val sc = new SparkContext(conf)

  // 2.2.1
  val drugs = sc.textFile("med.txt").map(x => x.split("\t")).map(x => (x(0), x(1), x(6)))
  println("DRUGS : " + drugs.count())

  // 2.2.2
  val rep = drugs.map(x => (x._3, 1)).reduceByKey(_ + _)
  rep.foreach(println)
}
