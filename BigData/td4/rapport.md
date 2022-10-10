# TD 4
## 1.1
Load the dataset and the two dictionaries. Provide the sizes of these 3 RDDs.

```scala
val univ = sc.textFile("univ1.nt").map(x => x.split(" ")).map(x => (x(1), x(0), x(2)));
val univC = sc.textFile("univConcepts.txt").map(x => x.split(" ")).map(x => (x(1), x(0).toLong));
val univP = sc.textFile("univProps.txt");

univ.persist();
univ.count;

univC.persist();
univC.count;

univP.persist();
univP.count;
```

## 1.2
Encode the properties of the dataset. Provide the number of properties used in this new version of the
dataset. Are all dictionary properties used in the dataset?

```scala

```