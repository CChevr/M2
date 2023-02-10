# TP3 

## 1 Blazegraph with and without inferences

### 1.1 without inferences

As in the SPARQL lab session, load in a Blazegraph namespace the drugbank dataset. The namespace must
be configured without the support for inferences. Observe the loading time of the set of triples and write a
SPARQL query displaying the number of triples in the graph

> Le chargement est assez rapide, pour le chargement de 517023 tuples

### 1.2 with inferences

Create another Blazegraph namespace but this time configure it with inferences. How can you interpret the evolution of the loading time and the number of triples in the dataset.
You can the next section since the processing of the inference takes quite some time.
Read the page https://wiki.blazegraph.com/wiki/index.php/InferenceAndTruthMaintenance to under-
stand how blazegraph works.

> Modified: 766920
Milliseconds: 507208

## 2 RDF4J programming over Blazegraph

You will develop in Java some programs that will use a Blazegraph database. For this, you will use the RDF4J API (an API supporting different services in the context of semantic web application development) from URIs: http://docs.rdf4j.org/ and http://docs.rdf4j.org/javadoc/latest/ Create a java project with Maven and modify the pom.xml to get the following code (adapt the version of Blazegraph):
