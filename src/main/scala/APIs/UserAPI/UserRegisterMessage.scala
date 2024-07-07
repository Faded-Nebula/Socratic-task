package APIs.UserAPI

case class UserRegisterMessage(userName:String, password:String) extends UserMessage[String]
