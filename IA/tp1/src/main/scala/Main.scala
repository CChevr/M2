package fr.uge.ia

import org.apache.jena.rdf.model.{ModelFactory, SimpleSelector}

import scala.io.Source

object Main {
    val model = ModelFactory.createDefaultModel()
    val typeProperty = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
    val propertyProperty = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Property"

    def q2() {
        val subject = model.createResource("http://upem.fr/example/macarron")
        val prop = model.createProperty("http://upem.fr/example/love")
        val obj = model.createResource("http://upem.fr/example/cookies")
        model.add(subject, prop, obj)

        model.write(System.out)
    }

    def addTriple(s: String, p: String, o: String): Unit = {
        val subject = model.createResource(s)
        val prop = model.createProperty(p)
        val obj = model.createResource(o)

        model.add(subject, prop, obj)
    }

    def load() = {model.read(s"file:///${new java.io.File(".").getCanonicalPath}/drugbank_dump.nt", "N-TRIPLES")}

    def showModel(): Unit = println("is empty ? " + model.isEmpty)

    def size() = model.size()

    def typePropertySize() = {
        val rdfType = model.createProperty(typeProperty)
        val iterator = model.listSubjectsWithProperty(rdfType)
        iterator.toList().size()
    }

    def propertyPropertySize() = {
        val rdfType = model.createProperty(typeProperty)
        val obj = model.createResource(propertyProperty)
        val iterator = model.listSubjectsWithProperty(rdfType, obj)
        iterator.toSet().size()
    }
    /*
    model.listStatements()
    new ListBuffer[String]()

    */

    def numberOfDrugs() = {
        val rdfType = model.createProperty(typeProperty)
        val obj = model.createResource("http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs")
        val iterator = model.listSubjectsWithProperty(rdfType, obj)
        iterator.toList.size()
    }

    def q3(): Unit = {
        load()
        println(size())
    }

    def q3_2(): Unit = {
        load()
        println(typePropertySize())
    }

    def q3_3(): Unit = {
        load()
        println(numberOfDrugs())
    }

    def q3_4(): Unit = {
        load()
        println(propertyPropertySize())
    }

    def main(args: Array[String]): Unit = {
        //q2()
        //q3()
        //q3_2()
        //q3_3()
        q3_4()
    }
}
