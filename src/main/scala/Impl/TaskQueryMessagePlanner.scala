package Impl

import cats.effect.IO
import io.circe.generic.auto.*
import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Shared.RandomWordSelector


case class TaskQueryMessagePlanner(userName:String, taskName:String, periodicalName:String, pdfBase64:String, researchArea:String, Abstract:String, TLDR:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Check if the user is already registered
    val checkUserExists = readDBBoolean(
      s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_acc WHERE task_name = ?)",
      List(SqlParameter("String", taskName))
    )
    // assign the alias
    val assignedAlias = RandomWordSelector.selectRandomWord()
    
    checkUserExists.flatMap { exists =>
      if (exists) {
        IO.pure("Conflict")
      } else {
        for {
          // assign token
          assignedToken <- readDBRows(
            s"SELECT task_name FROM ${schemaName}.task_acc WHERE task_name = ?",
            List(SqlParameter("String", taskName))
          )
            .map(RegisteredUsers => RegisteredUsers.length.toString)
          
          _ <- writeDB(
            s"INSERT INTO ${schemaName}.task_acc (task_name, user_name, identity, alias, token) VALUES (?, ?, ?, ?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", userName),
              SqlParameter("String", "author"),
              SqlParameter("String", assignedAlias),
              SqlParameter("String", assignedToken),
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
            s"INSERT INTO ${schemaName}.task_info (task_name, task_periodical, task_area, abstract, tldr, state) VALUES (?, ?, ?, ?, ?, ?)",
            List(
              SqlParameter("String", taskName),
              SqlParameter("String", periodicalName),
              SqlParameter("String", researchArea),
              SqlParameter("String", Abstract),
              SqlParameter("String", TLDR),
              SqlParameter("String", "init"),
            )
          )
        } yield "OK"
      }
    }
  }
