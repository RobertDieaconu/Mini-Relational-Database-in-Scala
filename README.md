# Mini Relational Database in Scala

A lightweight, functional in-memory relational database implemented in Scala 3. The project models core database concepts — tables, filters, queries, and a database container — using immutable data structures and a composable filter DSL.

## Project Structure

```
src/
├── main/scala/
│   ├── Table.scala       # Table data structure and operations
│   ├── FilterCond.scala  # Filter condition DSL
│   ├── Database.scala    # Database container and management
│   └── Queries.scala     # Predefined query examples
└── test/scala/
    ├── TestTable.scala
    ├── TestFilter.scala
    ├── TestDatabase.scala
    ├── TestQueries.scala
    └── Utils.scala
```

## Core Components

### `Table`

Represents a named database table as a list of rows (`Map[String, String]`).

| Method | Description |
|---|---|
| `header` | Returns the list of column names |
| `data` | Returns all rows |
| `toString` | Serializes the table to CSV format |
| `insert(row)` | Inserts or replaces a row (matched by `id`) |
| `delete(row)` | Removes a matching row |
| `sort(column, ascending)` | Sorts rows by a column |
| `select(columns)` | Projects specific columns |
| `cartesianProduct(other)` | Computes the cartesian product of two tables |
| `filter(f)` | Filters rows using a `FilterCond` |
| `update(f, updates)` | Updates fields on rows matching a condition |
| `apply(index)` | Row access by index |

**Companion object:**
- `Table.fromCSV(csv)` — parses a CSV string (first line as header, first field as table name)
- `Table(name, csv)` — creates a named table from a CSV string

### `FilterCond`

A composable DSL for building filter predicates over rows.

```scala
// Primitive
Field("age", _.toInt > 18)

// Logical operators
cond1 && cond2   // AND
cond1 || cond2   // OR
!cond            // NOT
cond1 === cond2  // EQUAL (both must evaluate to the same Boolean)

// Multi-condition
Any(List(cond1, cond2, cond3))  // at least one is true
All(List(cond1, cond2, cond3))  // all are true
```

All conditions evaluate to `Option[Boolean]` — returning `None` if a referenced column does not exist in the row.

### `Database`

A collection of `Table` objects.

| Method | Description |
|---|---|
| `insert(tableName)` | Adds a new empty table (no-op if already exists) |
| `update(tableName, newTable)` | Replaces an existing table |
| `delete(tableName)` | Removes a table by name |
| `selectTables(names)` | Returns `Some(Database)` with only the named tables, or `None` if any is missing |
| `apply(index)` | Table access by index |

### `Queries`

Example queries demonstrating how to compose `Database` and `Table` operations:

| Query | Description |
|---|---|
| `query_1(db, ageLimit, cities)` | Customers older than `ageLimit` located in any of the given `cities`, sorted by `id` |
| `query_2(db, date, employeeID)` | Orders after `date` not handled by `employeeID`, projected to `order_id` and `cost`, sorted by descending cost |
| `query_3(db, minCost)` | Orders with cost above `minCost`, projected to `order_id`, `employee_id`, and `cost`, sorted by `employee_id` |

## Tech Stack

- **Language:** Scala 3.3.1
- **Build tool:** sbt
- **Testing:** ScalaTest 3.2.18

## Running

**Run all tests:**
```bash
sbt test
```

**Start a Scala REPL with the project on the classpath:**
```bash
sbt console
```

## Type Aliases

```scala
type Row     = Map[String, String]
type Tabular = List[Row]
```
