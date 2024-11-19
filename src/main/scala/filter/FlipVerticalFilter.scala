package filter

import model.{Image, RGB}
import model.Image

class FlipVerticalFilter extends ImageFilter {
  def name: String = "Flip horizontal"

  def apply(image: Image): Image = {
    val newPixels = Array.ofDim[RGB](image.width, image.height)

    for {
      y <- 0 until image.height
      x <- 0 until image.width
    }
      {
        newPixels(y)(x) = image.pixels(image.height - y - 1)(x)
      }
      Image(image.height, image.width, newPixels)
  }
}
