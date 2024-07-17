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

case class ReadTaskAuthorMessagePlanner(taskName: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    val taskListIO: IO[List[Json]] = readDBRows(
      s"SELECT user_name FROM ${schemaName}.task_acc WHERE task_name = ? and identity = ?",
      List(
        SqlParameter("String", taskName),
        SqlParameter("String", "author"),
      )
    )
    taskListIO.map { taskAuthors =>
      Json.arr(taskAuthors: _*).noSpaces
    }
  }
