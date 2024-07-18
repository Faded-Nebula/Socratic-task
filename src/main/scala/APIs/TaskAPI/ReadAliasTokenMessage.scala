package APIs.TaskAPI

case class ReadAliasTokenMessage(taskName:String, userName:String) extends TaskMessage[String]