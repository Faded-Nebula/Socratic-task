package Impl

import Common.API.{PlanContext, Planner}
import Common.DBAPI.*
import Common.Object.SqlParameter
import Common.ServiceUtils.schemaName
import Shared.Log
import cats.effect.IO
import io.circe.generic.auto.*


case class AddRebuttalMessagePlanner(taskName:String, log_seq:Int, rebuttal:String, override val planContext: PlanContext) extends Planner[String]:
  override def plan(using PlanContext): IO[String] = {
      // 检查是否存在对应的 taskName 和 log_seq 的记录
      val logExists = readDBBoolean(
        s"SELECT EXISTS(SELECT 1 FROM ${schemaName}.task_log WHERE task_name = ? AND log_seq = ?)",
        List(
          SqlParameter("String", taskName),
          SqlParameter("Int", log_seq.toString)
        )
      )
    logExists.flatMap { Exists =>
        if (Exists) {
          // 更新记录，添加 rebuttal
          writeDB(
            s"UPDATE ${schemaName}.task_log SET rebuttal = ? WHERE task_name = ? AND log_seq = ?",
            List(
              SqlParameter("String", rebuttal),
              SqlParameter("String", taskName),
              SqlParameter("Int", log_seq.toString)
            )
          ).map(_ => s"Rebuttal added for task $taskName with sequence number $log_seq")
        } else {
          // 返回 "Invalid log" 信息
          IO.pure("Invalid log")
        }
      }
    
  }
