package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.readDBString
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO

case class CheckTaskIdentityMessagePlanner(taskName:String, userName:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Search the Task Info on the db
    readDBString(
      s"SELECT identity FROM ${schemaName}.task_info WHERE task_name = ?, user_name = ?",
      List(
        SqlParameter("String", taskName),
        SqlParameter("String", userName)
      )
    )
  }
