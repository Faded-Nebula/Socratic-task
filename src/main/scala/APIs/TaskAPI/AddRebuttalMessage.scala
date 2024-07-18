package APIs.TaskAPI

case class AddRebuttalMessage(taskName:String, log_seq:Int, rebuttal:String) extends TaskMessage[String]
