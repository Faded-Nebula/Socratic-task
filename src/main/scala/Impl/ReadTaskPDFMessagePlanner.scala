package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class ReadTaskPDFMessagePlanner(taskName:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Search the Task PDF data on the db
    readDBString(
      s"SELECT task_pdf FROM ${schemaName}.task_pdf WHERE task_name = ?",
      List(SqlParameter("String", taskName))
    )
  }
