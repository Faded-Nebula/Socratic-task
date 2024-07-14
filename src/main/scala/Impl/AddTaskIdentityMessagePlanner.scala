package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName

case class AddTaskIdentityMessagePlanner(taskName: String, userName: String, identity: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Test if identity is valid
    val validIdentities = Set("author", "coauthor", "editor", "reviewer")

    if (!validIdentities.contains(identity)) {
      IO.pure(s"Invalid identity: $identity. Must be one of ${validIdentities.mkString(", ")}")
    } else {
      // Add identity for a task
      val checkIdentityExists = readDBBoolean(
        s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ? AND user_name = ? AND identity = ?)",
        List(
          SqlParameter("String", taskName),
          SqlParameter("String", userName),
          SqlParameter("String", identity)
        )
      )
      checkIdentityExists.flatMap { exists =>
        if (exists) {
          IO.pure("Identity Already Exists")
        } else {
          writeDB(
            s"INSERT INTO ${schemaName}.task_acc (task_name, user_name, identity) VALUES (?, ?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", userName),
              SqlParameter("String", identity)
            )
          )
        }
      }
    }
  }

