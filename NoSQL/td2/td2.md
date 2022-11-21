# Caching PostgreSQL with Redis

## Goal

Discover different caching strategies with the Redis key-value database system

## First queries

We can use a hash structure in a value
```
HSET user:1000 name "John Smith"
HSET user:1000 email "john.smith@example.com"
HSET user:1000 password "s3cret"
```

We can get all the pairs of a key with
```
HGETALL user:1000
```