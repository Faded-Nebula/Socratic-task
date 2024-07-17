package Process

import Common.API.{API, PlanContext, TraceID}
import Global.{ServerConfig, ServiceCenter}
import Common.DBAPI.{initSchema, writeDB}
import Common.ServiceUtils.schemaName
import cats.effect.IO
import io.circe.generic.auto.*
import org.http4s.client.Client

import java.util.UUID

object Init {
  def init(config:ServerConfig):IO[Unit]=
    given PlanContext=PlanContext(traceID = TraceID(UUID.randomUUID().toString),0)
    for{
      _ <- API.init(config.maximumClientConnection)
      _ <- initSchema(schemaName)
      _ <- writeDB(s"CREATE TABLE IF NOT EXISTS ${schemaName}.task_acc (task_name TEXT, user_name TEXT, identity TEXT, alias TEXT, token TEXT)", List())
      _ <- writeDB(s"CREATE TABLE IF NOT EXISTS ${schemaName}.task_pdf (task_name TEXT, task_pdf TEXT)", List())
      _ <- writeDB(s"CREATE TABLE IF NOT EXISTS ${schemaName}.task_info (task_name TEXT, task_periodical TEXT, task_area TEXT, abstract TEXT, tldr TEXT, keyword TEXT, state TEXT)", List())
      _ <- writeDB(s"CREATE TABLE IF NOT EXISTS ${schemaName}.task_log (log_type TEXT, task_name TEXT, user_name TEXT, decision TEXT, comment TEXT, reasons_to_accept TEXT, reasons_to_reject TEXT, questions_to_authors TEXT, rating INT, confidence INT)", List())
    } yield ()

}
