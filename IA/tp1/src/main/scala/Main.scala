package fr.uge.ia

import org.apache.jena.rdf.model.ModelFactory

object Main {
    def main(args: Array[String]): Unit = {
        val model = ModelFactory.createDefaultModel()

        val subject = model.createResource("http://upem.fr/example/macarron")
        val prop = model.createProperty("http://upem.fr/example/love")
        val obj = model.createResource("http://upem.fr/example/cookies")
        model.add(subject, prop, obj)

        model.write(System.out)
    }
}
