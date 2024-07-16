package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*
import Shared.Log


case class AddLogMessagePlanner(taskName:String, log:Log, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
    // Check if the user is already registered
        writeDB(
          s"INSERT INTO ${schemaName}.task_log (log_type, task_name, user_name, decision, comment, reasons_to_accept, reasons_to_reject, questions_to_authors, rating, confidence) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
          List(
            SqlParameter("String", log.logType),
            SqlParameter("String", taskName),
            SqlParameter("String", log.userName),
            SqlParameter("String", log.decision.toString),
            SqlParameter("String", log.comment),
            SqlParameter("String", log.reasonsToAccept),
            SqlParameter("String", log.reasonsToReject),
            SqlParameter("String", log.questionsToAuthors),
            SqlParameter("Int", log.rating.toString),
            SqlParameter("Int", log.confidence.toString)
          )
        )
  }
