case class Database(tables: List[Table]) {
  // TODO 3.0
  override def toString: String = {
    tables.map(_.toString).mkString("\n")
  }

  // TODO 3.1
  def insert(tableName: String): Database = {
    if (tables.exists(_.tableName == tableName)) this
    else {
      val newTable = Table(tableName, List.empty[Row]) // Create a new table with an empty list of rows
      Database(tables :+ newTable) // Append the new table to the list of tables
    }
  }

  // TODO 3.2
  def update(tableName: String, newTable: Table): Database = {
    if (tables.exists(_.tableName == tableName)) {
      val updatedTables = tables.map {
        case table if table.tableName == tableName => newTable
        case table => table
      }
      Database(updatedTables)
    } else {
      this
    }
  }
  // TODO 3.3
  def delete(tableName: String): Database = {
    if(tables.exists(_.tableName == tableName)) {
      val updateTables = tables.filterNot(_.tableName == tableName)
      Database(updateTables)
    } else {
      this
    }
  }

  // TODO 3.4
  def selectTables(tableNames: List[String]): Option[Database] = {
    val selectedTables = tables.filter(table => tableNames.contains(table.tableName))
    if (selectedTables.size == tableNames.size) {
      Some(Database(selectedTables))
    } else {
      None
    }
  }

  // TODO 3.5
  // Implement indexing here
  def apply(index: Int): Table = tables(index)
}
