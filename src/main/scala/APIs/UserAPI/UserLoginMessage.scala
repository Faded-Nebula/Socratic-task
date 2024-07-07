package APIs.UserAPI

case class UserLoginMessage(userName:String, password:String) extends UserMessage[String]
