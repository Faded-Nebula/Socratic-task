package APIs.UserAPI

import Common.API.API
import Global.ServiceCenter.userServiceCode
import io.circe.Decoder

abstract class UserMessage[ReturnType:Decoder] extends API[ReturnType](userServiceCode)
