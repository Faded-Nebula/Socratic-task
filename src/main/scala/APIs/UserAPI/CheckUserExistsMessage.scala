package APIs.UserAPI

case class CheckUserExistsMessage(userName:String) extends UserMessage[Boolean]
