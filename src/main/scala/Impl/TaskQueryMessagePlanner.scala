package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName


case class TaskQueryMessagePlanner(userName:String, taskName:String, periodicalName:String, pdfBase64:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Check if the user is already registered
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ?)",
      List(SqlParameter("String", taskName))
    )

    checkUserExists.flatMap { exists =>
      if (exists) {
        IO.pure("Conflict")
      } else {
        for {
          _ <- writeDB(
            s"INSERT INTO ${schemaName}.task_acc (task_name, user_name) VALUES (?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", userName),
            )
          )
          _ <- writeDB(
            s"INSERT INTO ${schemaName}.task_pdf (task_name, task_pdf) VALUES (?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", pdfBase64),
            )
          )
          _ <- writeDB(
            s"INSERT INTO ${schemaName}.task_pdf (task_name, task_periodical) VALUES (?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", periodicalName),
            )
          )
        } yield "OK"
      }
    }
  }
