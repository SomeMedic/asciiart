package filter

import model.Image

trait ImageFilter {
  def apply(image: Image): Image
  
  def name: String
}
