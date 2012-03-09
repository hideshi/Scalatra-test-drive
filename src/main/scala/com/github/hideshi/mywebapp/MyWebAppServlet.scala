package com.github.hideshi.mywebapp

import org.scalatra._
import scalate.ScalateSupport

case class Employee(id:Int, name:String, age:Int)

class MyWebAppServlet extends ScalatraServlet with ScalateSupport {

	beforeAll {
		contentType = "text/html"
	}

  get("/") {
		scaml("index", "layout" -> "index")
  }

	get("/index") {
		scaml("index", "layout" -> "index")
	}

	post("/employeelist") {
		val employeeList = new EmployeeDao().findAll
		scaml("employeelist", "layout" -> "employeelist","employeeList" -> employeeList)
	}

	post("/employeedetail") {
		val result = new EmployeeDao().findOne(params("id").toInt)
		val employeeDetail = result match {
			case Some(s)	=> s
			case None			=> throw new Exception
		}
		scaml("employeedetail", "layout" -> "employeedetail", "employeeDetail" -> employeeDetail)
	}

	post("/addnewemployee") {
		scaml("addnewemployee", "layout" -> "employeedetail")
	}

	post("/submitnewemployee") {
		val result = new EmployeeDao().insert(Employee(0, params("name"), params("age").toInt))		
		scaml("addnewemployee", "layout" -> "employeedetail")
	}

  notFound {
    // Try to render a ScalateTemplate if no route matched
    findTemplate(requestPath) map { path =>
      layoutTemplate(path)
    } orElse serveStaticResource() getOrElse resourceNotFound() 
  }
}
