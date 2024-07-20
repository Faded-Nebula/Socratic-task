package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import APIs.UserManagementAPI.CheckUserExistsMessage
import Shared.RandomWordSelector


case class AddTaskIdentityMessagePlanner(taskName: String, userName: String, identity: String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Test if identity is valid
    val validIdentities = Set("author", "reviewer", "comment")

    if (!validIdentities.contains(identity)) {
      IO.pure(s"Invalid identity: $identity. Must be one of ${validIdentities.mkString(", ")}")
    } else {
      // Add identity for a task
      // First check if the user exists
      val checkUserExists = CheckUserExistsMessage(userName).send
      // Next check if the user already registered
      val checkIdentityRegistered = readDBBoolean(
        s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ? AND user_name = ?)",
        List(
          SqlParameter("String", taskName),
          SqlParameter("String", userName))
      )
      // Then assign an avatar and a token
      val assignedAlias = RandomWordSelector.selectRandomWord()

      checkUserExists.flatMap { exists =>
        if (!exists) {
          IO.pure(s"User $userName doesn't exist")
        } else {
          checkIdentityRegistered.flatMap { registered =>
            if (registered) {
              IO.pure(s"User $userName already registered")
            } else {
              for {
                // assign a token
                assignedToken <- readDBRows(
                  s"SELECT task_name FROM ${schemaName}.task_acc WHERE task_name = ?",
                  List(SqlParameter("String", taskName))
                ).map(RegisteredUsers => RegisteredUsers.length.toString)

                _ <- writeDB(
                    s"INSERT INTO ${schemaName}.task_acc (task_name, user_name, identity, alias, token) VALUES (?, ?, ?, ?, ?)",
                    List(
                      SqlParameter("String", taskName),
                      SqlParameter("String", userName),
                      SqlParameter("String", identity),
                      SqlParameter("String", assignedAlias),
                      SqlParameter("String", assignedToken),
                    )
                  )
              } yield "OK"
            }
          }
        }
      }
    }
  }

