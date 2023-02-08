# 2 - Query
1. Write a query to display all the triples in the dataset.

select (count(*) as ?count) where {?s ?p ?o}
count
517023

2. Write a query displaying the subjects and objects whose property is rdf:type. How many rows do you
have in the result

select ?s ?o where {?s rdf:type ?o}
count
24465

3. Write a query showing the subjects of triples whose property is rdf:type and the object is
http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs. How many lines do you get?

select * where {?s rdf:type <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs>}

4. Take the request from question 3 and impose an ascending order on the subject.

select * where {?s rdf:type <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs>} ORDER BY ?x

5. Click on the subject whose URI ends with DB01146. 3 tables are displayed: outgoing, ingoing and
attributes. After a visual search, provide the value of molecularWeightAverage.
<http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/molecularWeightAverage>	281.392


6. Write a request providing all drugs with a triple where the object contains the string ”Dextromethorphan”. How many distinct identifiers do you get?

select DISTINCT * 
where {?s ?p ?o
      FILTER regex(?o, ".*Dextromethorphan.*")}

select (count(DISTINCT *) as ?count)
where {?s ?p ?o
      FILTER regex(?o, ".*Dextromethorphan.*")}

count
278

select (count(DISTINCT ?s) as ?count)
where {?s ?p ?o. 
       ?s rdf:type <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs>.
      FILTER regex(?o, ".*Dextromethorphan.*")}

count
11

7. Provide the value of the ATC code (atcCode) for each of these drugs. How many lines do you get?

PREFIX prop: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>

select DISTINCT ?code
where {?s ?p ?o. 
       ?s rdf:type <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs>.
       ?s prop:atcCode ?code.
      FILTER regex(?o, ".*Dextromethorphan.*")}

PREFIX prop: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>

select (count(DISTINCT ?code) as ?count)
where {?s ?p ?o. 
       ?s rdf:type <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/drugs>.
       ?s prop:atcCode ?code.
      FILTER regex(?o, ".*Dextromethorphan.*")}

COUNT
22

8. Write a Boolean SPARQL query to find out if there is a drug with the ATC code ”R05DA09”.
PREFIX prop: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>

ask {?s prop:atcCode "R05DA09"}
true

PREFIX prop: <http://www4.wiwiss.fu-berlin.de/drugbank/resource/drugbank/>

9. Write a DESCRIBE-type query to obtain all the information on the subject of the query 8. How many
rows do you get in the result?

describe ?s 
where {?s prop:atcCode "R05DA09"}
COUNT
266

10. Write a query that only displays triples whose object is a literal. Write a query that displays only
triples whose object has a data type of type integer.
