package Private

import java.nio.file.{Files, Paths, StandardCopyOption}
import java.io.InputStream
import cats.effect.IO

object FileStorage {
  def saveFile(taskName: String, inputStream: InputStream, fileName: String): IO[String] = IO {
    val directory = Paths.get(s"submission/$taskName")
    if (!Files.exists(directory)) {
      Files.createDirectories(directory)
    }
    val filePath = directory.resolve(fileName)
    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
    filePath.toString
  }
}

