package APIs.TaskAPI

case class SearchTaskMessage(taskName:String, userName:String) extends TaskMessage[String]
