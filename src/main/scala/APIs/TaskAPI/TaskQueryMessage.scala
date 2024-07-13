package APIs.TaskAPI

case class TaskQueryMessage(userName:String, taskName:String, periodicalName:String, pdfBase64:String) extends TaskMessage[String]
