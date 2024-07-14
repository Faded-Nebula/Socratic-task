package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class ReadTaskInfoMessagePlanner(taskName:String, property:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Search the Task Info on the db
    readDBString(
      s"SELECT ${property} FROM ${schemaName}.task_info WHERE task_name = ?",
      List(SqlParameter("String", taskName))
    )
  }

