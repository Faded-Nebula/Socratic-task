package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBBoolean, readDBString}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO

case class ReadAliasTokenMessagePlanner(taskName:String, userName:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    val checkIdentityRegistered = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ? AND user_name = ?)",
      List(
        SqlParameter("String", taskName),
        SqlParameter("String", userName),
      )
    )
    checkIdentityRegistered.flatMap { exists =>
      if (!exists) {
        IO.pure(s"User doesn't registered")
      } else {
        // Search the Alias on the db
        readDBString(
          s"SELECT token FROM ${schemaName}.task_acc WHERE task_name = ? AND user_name = ?",
          List(
            SqlParameter("String", taskName),
            SqlParameter("String", userName)
          )
        )
      }
    }
  }
