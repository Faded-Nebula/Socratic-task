package APIs.TaskAPI

case class TaskQueryMessage(userName:String, taskName:String) extends TaskMessage[String]
