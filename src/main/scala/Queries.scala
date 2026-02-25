object Queries {
  def query_1(db: Database, ageLimit: Int, cities: List[String]): Option[Table] = {
    def func1(row: Row): Boolean = {
      row("age").toInt > ageLimit &&
        cities.contains(row("city"))
    }
    db.tables.find(_.tableName == "Customers")
      .map(table =>
        Table(table.tableName,
          table.tableData.filter(func1)
            .sortBy(row => row("id").toInt)))
  }

  def query_2(db: Database, date: String, employeeID: Int): Option[Table] = {
    def func2(row: Row): Boolean = {
      row("date") > date &&
        row("employee_id").toInt != employeeID
    }
    db.tables.find(_.tableName == "Orders")
      .map(table =>
        Table(table.tableName,
          table.tableData.filter(func2)
            .map(row => row.filterKeys(Set("order_id", "cost").contains).toMap)
            .sortBy(row => -row("cost").toInt)))

  }

  def query_3(db: Database, minCost: Int): Option[Table] = {
    def func3(row: Row): Boolean = {
      row("cost").toInt > minCost
    }
    db.tables.find(_.tableName == "Orders")
      .map(table =>
      Table(table.tableName,
        table.tableData.filter(func3)
      .map(row => row.filterKeys(Set("order_id", "employee_id", "cost").contains).toMap)
      .sortBy(row => row("employee_id").toInt)))
  }
}
