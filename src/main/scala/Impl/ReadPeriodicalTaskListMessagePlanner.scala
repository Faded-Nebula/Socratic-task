package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.{writeDB, *}
import Common.Object.{ParameterList, SqlParameter}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.Json
import io.circe.generic.auto.*

case class ReadPeriodicalTaskListMessagePlanner(periodicalName: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    val taskListIO: IO[List[Json]] = readDBRows(
      s"SELECT task_name FROM ${schemaName}.task_info WHERE task_periodical = ?",
      List(SqlParameter("String", periodicalName))
    )
    taskListIO.map { editorTasks =>
      Json.arr(editorTasks: _*).noSpaces
    }
  }
