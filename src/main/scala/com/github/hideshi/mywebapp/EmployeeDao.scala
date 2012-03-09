package com.github.hideshi.mywebapp


import java.sql.{Connection, DriverManager, Statement, PreparedStatement, ResultSet, SQLException}
import scala.collection.mutable.ArrayBuffer

class EmployeeDao {

	private def createConnection(): Connection = {
			Class.forName("com.mysql.jdbc.Driver").newInstance
			DriverManager.getConnection("jdbc:mysql://localhost:3306/test?user=root&password=")
	}

	def findAll(): List[Employee] = {
		var conn: Connection = null
		var stmt: Statement = null
		var rs: ResultSet = null
		try {
			conn = createConnection
			stmt = conn.createStatement
			rs = stmt.executeQuery("SELECT * FROM EMPLOYEE")
			var result = new ArrayBuffer[Employee]
			while(rs.next) {
				var id = rs.getInt("id")
				var name = rs.getString("name")
				var age = rs.getInt("age")
				var employee = Employee(id, name, age)
				result + employee
			}
			result.toList
		} catch {
			case ex: SQLException => println(ex)
			return Nil
		} finally {
			rs.close
			stmt.close
			conn.close	
		}
	}

	def findOne(id: Int): Option[Employee] = {
		var conn: Connection = null
		var pstmt: PreparedStatement = null
		var rs: ResultSet = null
		try {
			conn = createConnection
			pstmt = conn.prepareStatement("SELECT * FROM EMPLOYEE WHERE ID = ?")
			pstmt.setInt(1, id)
			rs = pstmt.executeQuery
			if(rs.next) {
				var id = rs.getInt("id")
				var name = rs.getString("name")
				var age = rs.getInt("age")
				Some(Employee(id, name, age))
			} else {
				None
			}
		} catch {
			case ex: SQLException => println(ex)
			return None
		} finally {
			rs.close
			pstmt.close
			conn.close	
		}
	}

	def insert(entity:Employee): Int = {
		var conn: Connection = null
		var pstmt: PreparedStatement = null
		try {
			conn = createConnection
			pstmt = conn.prepareStatement("INSERT INTO EMPLOYEE (NAME, AGE) VALUES (?, ?)")
			println(entity.name)
			println(entity.age)
			pstmt.setString(1, entity.name)
			pstmt.setInt(2, entity.age)
			pstmt.executeUpdate
		} catch {
			case ex: SQLException => println(ex)
			return 0 
		} finally {
			pstmt.close
			conn.close	
		}
	}
}
