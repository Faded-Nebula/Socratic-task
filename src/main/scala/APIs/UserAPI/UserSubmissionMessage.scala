package APIs.UserAPI

case class UserSubmissionMessage(userName:String, taskName:String) extends UserMessage[String]
