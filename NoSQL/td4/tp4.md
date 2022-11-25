# MongoDB – Modeling and querying a Document store

## 3 Given the database designed in 1a

a. Display the 2005 movies. Between the parentheses of find(), we place a
filter in JSON format: {year: '' 2005 ''}. Make it more readable with pretty()
```
db.dbma.find({year:2005}).pretty
```

b. Provide all the titles of the 2005 films. To select only a few output attributes,
we add a filter in the second position of find().
```
db.dbma.find({year:2005}, {title:1}).pretty
```

We still get _id. How to remove it with what we have just seen.
```
db.dbma.find({year:2005}, {title: 1, _id: 0}).pretty
```
