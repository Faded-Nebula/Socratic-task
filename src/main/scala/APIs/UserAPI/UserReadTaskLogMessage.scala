package APIs.UserAPI

case class UserReadTaskLogMessage(userName:String, taskName:String) extends UserMessage[String]
