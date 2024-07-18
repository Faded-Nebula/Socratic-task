package APIs.TaskAPI

case class ReadAliasMessage(taskName:String, userName:String) extends TaskMessage[String]
