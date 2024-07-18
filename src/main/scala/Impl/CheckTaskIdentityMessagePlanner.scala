package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.{readDBBoolean, readDBString}
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO

case class CheckTaskIdentityMessagePlanner(taskName:String, userName:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Check if registered
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ? and user_name = ?)",
      List(
        SqlParameter("String", taskName),
        SqlParameter("String", userName),
      )
    )
    // Search the Task Info on the db
    checkUserExists.flatMap { exists =>
      if (exists){
        readDBString(
          s"SELECT identity FROM ${schemaName}.task_acc WHERE task_name = ? and user_name = ?",
          List(
            SqlParameter("String", taskName),
            SqlParameter("String", userName)
          )
        )
      } else {
        IO.pure(s"User $userName not registered in the task $taskName")
      }
    }
  }
