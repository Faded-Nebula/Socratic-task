package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.Json
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*

case class ReadLogListMessagePlanner(taskName:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using planContext: PlanContext): IO[String] = {
    // Fetch rows from EditorTasks
    readDBRows(
      s"SELECT * FROM ${schemaName}.task_log WHERE task_name = ? ORDER BY log_seq ASC",
      List(SqlParameter("String", taskName))
    ).map { Logs =>
      Json.arr(Logs: _*).noSpaces
    }
  }

