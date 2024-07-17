package APIs.TaskAPI

case class TaskEditInfoMessage(taskName:String, property:String, updateValue:String) extends TaskMessage[String]
