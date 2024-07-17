package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class TaskEditInfoMessagePlanner(taskName: String, property: String, updateValue:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    writeDB(
      s"UPDATE ${schemaName}.task_info SET ${property} = ? WHERE task_name = ?",
      List(
        SqlParameter("String", updateValue),
        SqlParameter("String", taskName)
      )
    ).map(_ => "OK")
  }
