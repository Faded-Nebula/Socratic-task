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
    for {
      // 获取当前 taskName 的 log 序号
      currentCnt <- readDBInt(
        s"SELECT cnt FROM ${schemaName}.task_log_counter WHERE task_name = ?",
        List(SqlParameter("String", taskName))
      )

      // 更新序号
      newCnt = currentCnt + 1

      // 插入新的 log 记录
      _ <- writeDB(
        s"INSERT INTO ${schemaName}.task_log (log_seq, log_type, task_name, user_name, decision, comment, reasons_to_accept, reasons_to_reject, questions_to_authors, rating, confidence, rebuttal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        List(
          SqlParameter("Int", newCnt.toString),
          SqlParameter("String", log.logType),
          SqlParameter("String", taskName),
          SqlParameter("String", log.userName),
          SqlParameter("String", log.decision.toString),
          SqlParameter("String", log.comment),
          SqlParameter("String", log.reasonsToAccept),
          SqlParameter("String", log.reasonsToReject),
          SqlParameter("String", log.questionsToAuthors),
          SqlParameter("Int", log.rating.toString),
          SqlParameter("Int", log.confidence.toString),
          SqlParameter("String", "None")
        )
      )

      // 更新 task_log_counter 表中的序号
      _ <- writeDB(
        s"UPDATE ${schemaName}.task_log_counter SET cnt = ? WHERE task_name = ?",
        List(
          SqlParameter("Int", newCnt.toString),
          SqlParameter("String", taskName)
        )
      )
    } yield {
      s"Log entry for task $taskName added with sequence number $newCnt"
    }
  }
