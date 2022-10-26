package fr.uge.scala

import org.apache.spark.{SparkConf, SparkContext}

object td6 extends App {
    val conf = new SparkConf().setAppName("simpleSparkApp").setMaster("local")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")

    // 2.2.1
    val drugs = sc.textFile("med.txt").map(x => x.split("\t")).map(x => (x(0).toLong, x(1), x(6)))
    println("Med count: " + drugs.count())              // Med count : 13674

    // 2.2.2
    val rep = drugs.map(x => (x._3, 1)).reduceByKey(_ + _)
    rep.foreach(println)                                // (Commercialis � e, 11861)\n  (Non commercialis � e, 1813)
    val comDrugs = drugs.filter(x => x._3 == "Commercialis�e").map(x => (x._1, x._2))
    //println("COMDRUGS: " + comDrugs.count())          //COMDRUGS: 11861

    // 2.2.3
    val sub = sc.textFile("compo.txt").map(x => x.split("\t")).map(x => (x(0).toLong, x(2), x(3), x(6)))
    println("Compo count: " + sub.count())             // Compo count: 25484
}
