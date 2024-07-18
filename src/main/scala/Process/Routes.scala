package Process

import Common.API.PlanContext
import Impl.*
import cats.effect.*
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import org.http4s.*
import org.http4s.client.Client
import org.http4s.dsl.io.*


object Routes:
  private def executePlan(messageType:String, str: String): IO[String]=
    messageType match {
      case "TaskQueryMessage" =>
        IO(decode[TaskQueryMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for TaskQueryMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadTaskInfoMessage" =>
        IO(decode[ReadTaskInfoMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadTaskInfoMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadTaskPDFMessage" =>
        IO(decode[ReadTaskPDFMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadTaskPDFMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "AddTaskIdentityMessage" =>
        IO(decode[AddTaskIdentityMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for AddTaskIdentityMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadTaskListMessage" =>
        IO(decode[ReadTaskListMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadTaskListMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadPeriodicalTaskListMessage" =>
        IO(decode[ReadPeriodicalTaskListMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadPeriodicalTaskListMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "AddLogMessage" =>
        IO(decode[AddLogMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for AddLogMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadLogListMessage" =>
        IO(decode[ReadLogListMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadLogListMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadTaskAuthorMessage" =>
        IO(decode[ReadTaskAuthorMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadTaskAuthorMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "CheckTaskIdentityMessage" =>
        IO(decode[CheckTaskIdentityMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for CheckTaskIdentityMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "TaskEditInfoMessage" =>
        IO(decode[TaskEditInfoMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for TaskEditInfoMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "AddRebuttalMessage" =>
        IO(decode[AddRebuttalMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for AddRebuttalMessage")))
           .flatMap{m=>
                m.fullPlan.map(_.asJson.toString)
              }
      case "SearchTaskMessage" =>
        IO(decode[SearchTaskMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for SearchTaskMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadAliasMessage" =>
        IO(decode[ReadAliasMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadAliasMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case "ReadAliasTokenMessage" =>
        IO(decode[ReadAliasTokenMessagePlanner](str).getOrElse(throw new Exception("Invalid JSON for ReadAliasTokenMessage")))
          .flatMap{m=>
            m.fullPlan.map(_.asJson.toString)
          }
      case _ =>
        IO.raiseError(new Exception(s"Unknown type: $messageType"))
    }

  val service: HttpRoutes[IO] = HttpRoutes.of[IO]:
    case req @ POST -> Root / "api" / name =>
        println("request received")
        req.as[String].flatMap{executePlan(name, _)}.flatMap(Ok(_))
        .handleErrorWith{e =>
          println(e)
          BadRequest(e.getMessage)
        }
