package filter

import model.{Image, RGB}

class InvertFilter extends ImageFilter {
  def name: String = "Invert colors"
  
  def apply(image: Image): Image = {
    val newPixels = image.pixels.map(_.map(rgb => RGB(255 - rgb.red, 255 - rgb.green, 255 - rgb.blue)))
    
    Image(image.height, image.width, newPixels)
  }
}
