import scala.io.Source.fromFile

/*
2.1
Search in the scala.io.Source for a fromFile method and use it to load the movies.csv file. Then get all the
lines and transform the object into a List. How many entries do you have in this list.
*/
var movies = fromFile("./movies.csv")
var nbLines = fromFile("movies.csv").getLines.size

/*
2.2 Remove the first line of this list which consists of the file header
*/
val list = fromFile("movies.csv").getLines.toList
val l2 = list.drop(1)

/*
2.3
The scala console returns the type of the resulting variable after each instruction execution. You can also
type the name of a variable and type enter to get that type. The type of your List is String (List[String]).
We would like to transform the list into List[Array[String]] where the array contains 10 columns, that is all
columns. In the orginal file, the entries are separated by a tabulation symbol (“
t”). To perform this taval md = sc.textFile("../../td2/mobyDick.txt")), Array(tt0000002, short, Le clown et ses chiens, Le clown et ses chiens, 0, 1892,
N, 5, Animation,Short),...
*/
val array = l2.map(x=>x.split("\t"))

/*
2.4
The original document follows the following schema : (tconst,titleType, primaryTitle,originalTitle, isAdult,
startYear, endYear, runtimeMinutes, genres) We would like to transfom that list to only keep the following
entries : (tconst, titleType, originalTitle, startYear, runtimeMinutes, genres). Write the scala code to create
that new list.
*/
var array2 = array.map(x=>(x(0), x(1), x(3), x(5), x(8)))

/*
3 Spark
*/

/*
3.1
Install Spark. From the bin folder, start the Spark REPL: spark-shell. Execute the code of Question #1
*/

/*
3.2
Using the Spark documentation (https://spark.apache.org/), compute the number of lines in the book Moby
Dick. You should use the Spark API, that is use the sc variable (Spark context). You should create an RDD
to process this question and the remaining ones
*/
val moby = sc.textFile("../td2/mobyDick.txt")
val lines = moby.count

/*
3.3
From the structure (RDD) created in the previous question, select the lines containing the “CHAPTER”
string. How many do you have?
*/
val chap = moby.filter(x=>x.contains("CHAPTER"))
chap.count

/*
3.4
How many empty lines (ie, length is 0) does the file contain?
*/
val empty = moby.filter(x=>x.length()==0)
empty.count

/*
3.5
Using the structure of the previous question and inspired by the WordCount approach (2.1 Example in the
MapReduce paper), compute the number of occurrence of each word in the Moby Dick book. Your solution
needs to use a flatMap, map and reduceByKey operations. How many entries do you have in the structure?
*/
val res = moby.flatMap(x=>x.split(" ")).map(x=>(x, 1)).reduceByKey(_ + _)

/*
3.6
How many entries do you have in the structure of the previous question which contain the “captain” string?
*/
val captains = res.filter(tup=>tup._1.contains("captain"))

/*
3.7
Display 10 records of the structure of the previous question.
*/
captains.take(10)

/*
3.8
Display the top 10 most frequent words in the book. The display should contain the word and its number
of occurrences
*/
captains.map{case(k,v)=>(v,k)}.top(10)