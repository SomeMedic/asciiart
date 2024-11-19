package loader

import model.Image

trait ImageLoader {
  def load(path: String): Either[String, Image] 
  
  def save(path: String, image: Image): Either[String, Unit]
  
  def supportedFormats: String
}
