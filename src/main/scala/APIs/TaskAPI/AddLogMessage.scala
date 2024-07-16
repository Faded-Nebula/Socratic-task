package APIs.TaskAPI

import Shared.Log

case class AddLogMessage(log:Log) extends TaskMessage[String]
