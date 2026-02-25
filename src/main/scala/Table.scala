
type Row = Map[String, String]
type Tabular = List[Row]

case class Table (tableName: String, tableData: Tabular) {

  // TODO 1.0
  def header: List[String] = tableData.head.keys.toList
  def data: Tabular = tableData
  def name: String = tableName


  // TODO 1.1
  override def toString: String = {
    val header_toString = header.mkString(",")
    val data_toString = data.map(_.values.mkString(",")).mkString("\n")
    header_toString + "\n" + data_toString
    }

  // TODO 1.3
  def insert(row: Row): Table = {
    def find(row: Row, rows : Tabular) : Tabular = {
      rows match {
        case Nil => List(row)
        case head :: tail =>
          if (head.getOrElse("id", "") == row.getOrElse("id", "")) {
            row :: tail
          } else {
            head :: find(row, tail)
          }
      }
    }
    Table(tableName, find(row, tableData))
  }

  // TODO 1.4
  def delete(row: Row): Table = {
    def find(row: Row, rows : Tabular) : Tabular = {
      rows match {
        case Nil => Nil
        case head :: tail =>
          if (head == row) {
            tail
          } else {
            head :: find(row, tail)
          }
      }
    }
    Table(tableName, find(row, tableData))
  }

  // TODO 1.5
  def sort(column: String, ascending: Boolean = true): Table = {
    val sortedData = tableData.sortBy(_.getOrElse(column, ""))
    val finalSortedData = if (ascending) sortedData else sortedData.reverse
    Table(tableName, finalSortedData)
  }

  // TODO 1.6
  def select(columns: List[String]): Table = {
    val data = tableData.map(row => row.filter
    {case (key, _) => columns.contains(key)})
    Table(tableName, data)
  }

  // TODO 1.7
  // Construiti headerele noi concatenand numele tabelei la headere
  def cartesianProduct(otherTable: Table): Table = {
    def combine(table1: Tabular, table2: Tabular): Tabular = {
    table1 match {
      case Nil => Nil
      case row1 :: tail =>
        val combined = table2.map {
          row2 =>
            val newRow1 = row1.map {
              case (key, value) => (this.tableName + "." + key) -> value
            }
            val newRow2 = row2.map {
              case (key, value) => (otherTable.tableName + "." + key) -> value
            }
            newRow1 ++ newRow2
        }
        combined ++ combine(tail, table2)
    }
    }
    val combinedData = combine(tableData, otherTable.tableData)
    Table(otherTable.tableName ++ tableName, combinedData)
  }

  // TODO 1.8
  def join(other: Table)(col1: String, col2: String): Table = ???




  // TODO 2.3
  def filter(f: FilterCond): Table = {
    val filteredRows = tableData.filter {
      row =>
        f.eval(row).getOrElse(false)
    }
    Table(tableName, filteredRows)
  }
  // TODO 2.4
  def update(f: FilterCond, updates: Map[String, String]): Table = {
    val updateRows = tableData.map {
      row =>
        if (f.eval(row).getOrElse(false)) {
          row ++ updates
        } else {
          row
        }
    }
    Table(tableName, updateRows)
  }

  // TODO 3.5
  // Implement indexing
  def apply(index: Int): Map[String, String] = tableData(index)
}

object Table {
  // TODO 1.2
  def fromCSV(csv: String): Table = {
    val lines = csv.split("\n").toList
    val header = lines.head.split(",").toList
    val data = lines.tail.map {
      line =>
        val values = line.split(",").toList
        header.zip(values).toMap
    }
    Table(lines.head, data)
  }

  // TODO 1.9
  def apply(name: String, s: String): Table = {
    val lines = s.split("\n").toList
    val header = lines.head.split(",").toList
    val data = lines.tail.map {
      line =>
        val values = line.split(",").toList
        header.zip(values).toMap
    }
    Table(name, data)
  }


}