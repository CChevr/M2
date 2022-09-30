# TP3

## 1. Triples manipulations

In this question, we are going to manipulate a set of tuples of size 3. We will call them triples and they enable
us to represent a graph. In fact, the elements of the triple are respectively named a subject, a predicate and
an object. Hence, the triple (1,2,3) states that a node graph identified by id=1 is related to node graph with
id=3 via the predicate identified with the value 2.
Our running example corresponds to the following set of tuples:
((1,0,5),(5,1,8),(8,2,1),(2,0,6),(3,0,6),(6,1,9),(5,1,9),(9,3,11),(9,4,12),(4,0,7),(7,1,9),(7,2,10),(14,1,15),
(15,1,16),(14,1,16),(17,0,18),(18,0,19),(19,1,20),(20,0,17))
Draw this graph in order to easily verify your answers to the following questions

## 1.1 
Create a new Scala object (in the same project as Question 1) that creates an RDD, named triples, from
our running example set of triples

```scala
val triples = sc.parallelize(List((1,0,5),(5,1,8),(8,2,1),(2,0,6),(3,0,6),(6,1,9),(5,1,9),(9,3,11),(9,4,12),(4,0,7),(7,1,9),(7,2,10),(14,1,15),(15,1,16),(14,1,16),(17,0,18),(18,0,19),(19,1,20),(20,0,17)))
```

## 1.2
From the RDD in the previous section, create an RDD, named soPairs, that contains only the subject and
objects of the triples

```scala
val soPairs = triples.map(t => (t._1,t._3))
soPairs.persist(org.apache.spark.storage.StorageLevel.MEMORY_ONLY) //Utilisé plusieurs fois -> le faire persister en mémoire
```

## 1.3
Using soPairs, find the graph nodes which correspond to roots of the graph (no in-going edges on these graph
nodes). Name that RDD as roots

```scala
val roots = soPairs.map(x => x._1).subtract(soPairs.map(x => x._2))
val roots = soPairs.map(x => x._1).distinct.subtract(soPairs.map(x => x._2))
```

## 1.4
Create an RDD, denoted leaves, that contains the ids of the graph nodes corresponding to leaves (no out-
going edges).

```scala
val leaves = soPairs.map(x => x._2).distinct.subtract(soPairs.map(x => x._1))
soPairs.unpersist()
```

## 1.5
Create a new RDD that contains the set of nodes accessible from each node of the graph. This corresponds
to the transitive closure of the graph). Example, given a → b → c, we will end up with (a,b),(b,c), which
we already had, and (a,c). Help: You can use the join RDD operation to perform this task.

```scala
var begin = soPairs
val end = soPairs.map(_.swap)
var size = 0L

while(size != begin.count) {
    size = begin.count
    begin = begin.union(end.join(begin).map(x=>(x._2._1, x._2._2))).distinct
}

val routes = begin
```

## 1.6
Display, in sorted order on the subject value, only the subject,object pairs that have been added in the transitive closure (that is the pairs that were not originally in soPairs).

```scala
val diff = routes.subtract(soPairs).sortBy(x => x._1)
```

##  1.7
Given the RDDs created so far, create an RDD, named rooted, which contains the set of nodes accessible from a root. The resulting RDD should look like a tuple with the root in the first position and a sorted list of all accessible nodes in the second position.

```scala
val rooted = roots.map(x => (x, 0)).join(routes.groupByKey()).map(x => (x._1, x._2._2.toList))
```

## 1.8
Create an RDD, denoted cycles, that contains this graph’s set of cycles (that is its set of graph nodes).

```scala
val cycles = routes.filter(x => x._1 == x._2)

val cyc = oPairs.filter(x => x._1 == x._2)
val cycles = cic.join(sopairs).map(x._2._2, x._1).join(cyc).groupByKey().map(x=> x._2.map(y=>y._1)).distinct
cycles.foreach(println)
```