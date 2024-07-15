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

case class ReadTaskListMessagePlanner(userName: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    val taskListIO: IO[List[Json]] = readDBRows(
      s"SELECT task_name FROM ${schemaName}.task_acc WHERE user_name = ?",
      List(SqlParameter("String", userName))
    )
    taskListIO.map { managerTasks =>
      Json.arr(managerTasks: _*).noSpaces
    }
  }
