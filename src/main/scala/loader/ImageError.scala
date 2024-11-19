package loader

sealed trait ImageError

case class FileNotFound(path: String) extends ImageError {
  
}

case class InvalidFormat(message: String) extends ImageError {
  
}

case class UnsupportedFormat(format: String) extends ImageError {
  
}

def handleError(error: ImageError): String = error match {
  case FileNotFound(path) => s"File not found: ${path}"

  case InvalidFormat(message) => s"Invalid format: ${message}"

  case UnsupportedFormat(format) => s"Unsupported format: ${format}"
}