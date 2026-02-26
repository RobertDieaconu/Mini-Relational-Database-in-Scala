# Mini Bază de Date Relațională în Scala

O bază de date relațională in-memory, ușoară și funcțională, implementată în Scala 3. Proiectul modelează concepte de bază ale bazelor de date — tabele, filtre, interogări și un container de bază de date — folosind structuri de date imuabile și un DSL de filtrare compozabil.

## Structura Proiectului

```
src/
├── main/scala/
│   ├── Table.scala       # Structura de date a tabelului și operații
│   ├── FilterCond.scala  # DSL pentru condiții de filtrare
│   ├── Database.scala    # Containerul bazei de date și managementul acesteia
│   └── Queries.scala     # Exemple de interogări predefinite
└── test/scala/
    ├── TestTable.scala
    ├── TestFilter.scala
    ├── TestDatabase.scala
    ├── TestQueries.scala
    └── Utils.scala
```

## Componente Principale

### `Table`

Reprezintă un tabel de bază de date cu nume, ca o listă de rânduri (`Map[String, String]`).

| Metodă | Descriere |
|---|---|
| `header` | Returnează lista numelor de coloane |
| `data` | Returnează toate rândurile |
| `toString` | Serializează tabelul în format CSV |
| `insert(row)` | Inserează sau înlocuiește un rând (identificat după `id`) |
| `delete(row)` | Elimină un rând corespunzător |
| `sort(column, ascending)` | Sortează rândurile după o coloană |
| `select(columns)` | Proiectează coloane specifice |
| `cartesianProduct(other)` | Calculează produsul cartezian a două tabele |
| `filter(f)` | Filtrează rândurile folosind un `FilterCond` |
| `update(f, updates)` | Actualizează câmpurile rândurilor ce corespund unei condiții |
| `apply(index)` | Accesul la rând după index |

**Obiect companion:**
- `Table.fromCSV(csv)` — parsează un șir CSV (primul rând ca header, primul câmp ca nume de tabel)
- `Table(name, csv)` — creează un tabel cu nume dintr-un șir CSV

### `FilterCond`

Un DSL compozabil pentru construirea predicatelor de filtrare pe rânduri.

```scala
// Primitiv
Field("age", _.toInt > 18)

// Operatori logici
cond1 && cond2   // ȘI
cond1 || cond2   // SAU
!cond            // NEGAȚIE
cond1 === cond2  // EGAL (ambii trebuie să evalueze la același Boolean)

// Condiții multiple
Any(List(cond1, cond2, cond3))  // cel puțin una este adevărată
All(List(cond1, cond2, cond3))  // toate sunt adevărate
```

Toate condițiile evaluează la `Option[Boolean]` — returnând `None` dacă o coloană referită nu există în rând.

### `Database`

O colecție de obiecte `Table`.

| Metodă | Descriere |
|---|---|
| `insert(tableName)` | Adaugă un tabel nou gol (fără efect dacă există deja) |
| `update(tableName, newTable)` | Înlocuiește un tabel existent |
| `delete(tableName)` | Elimină un tabel după nume |
| `selectTables(names)` | Returnează `Some(Database)` doar cu tabelele specificate, sau `None` dacă vreunul lipsește |
| `apply(index)` | Accesul la tabel după index |

### `Queries`

Exemple de interogări care demonstrează cum se compun operațiile `Database` și `Table`:

| Interogare | Descriere |
|---|---|
| `query_1(db, ageLimit, cities)` | Clienți mai vârstnici de `ageLimit` localizați în oricare dintre `cities`, sortați după `id` |
| `query_2(db, date, employeeID)` | Comenzi după `date` care nu sunt gestionate de `employeeID`, proiectate pe `order_id` și `cost`, sortate descrescător după cost |
| `query_3(db, minCost)` | Comenzi cu costul peste `minCost`, proiectate pe `order_id`, `employee_id` și `cost`, sortate după `employee_id` |

## Tehnologii Utilizate

- **Limbaj:** Scala 3.3.1
- **Instrument de build:** sbt
- **Testare:** ScalaTest 3.2.18

## Rulare

**Rulează toate testele:**
```bash
sbt test
```

**Pornește un REPL Scala cu proiectul pe classpath:**
```bash
sbt console
```

## Aliasuri de Tipuri

```scala
type Row     = Map[String, String]
type Tabular = List[Row]
```
