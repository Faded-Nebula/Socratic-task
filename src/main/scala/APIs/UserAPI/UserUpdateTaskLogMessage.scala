package APIs.UserAPI

case class UserUpdateTaskLogMessage(userName:String, taskName:String) extends UserMessage[String]
