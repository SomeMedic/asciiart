package filter

import model.{Image, RGB}

class RotateFilter(angle: Int) extends ImageFilter {
  require(angle % 90 == 0, "Rotation angle must be a multiple of 90!")
  
  def name: String = s"Rotate ${angle}"

  def apply(image: Image): Image = {
    val normalizedAngle = Math.floorMod(angle, 360)
    
    normalizedAngle match {
      case 0 => image

      case 90 => rotate90(image)

      case 180 => rotate180(image)

      case 270 => rotate270(image)

      case 360 => image
    }
  }
  
  private def rotate90(image: Image): Image = {
    val newWidth = image.height
    val newHeight = image.width
    val newPixels = Array.ofDim[RGB](newHeight, newWidth)

    for {
      y <- 0 until image.height
      x <- 0 until image.width
    } {
      newPixels(newHeight - 1 - x)(y) = image.pixels(y)(x)
    }
    
    Image(newHeight, newWidth, newPixels)
  }

  private def rotate180(image: Image): Image = {
    val newPixels = Array.ofDim[RGB](image.width, image.height)

    for {
      y <- 0 until image.height
      x <- 0 until image.width
    }
      {
        newPixels(image.width - 1 - x)(image.height - 1 - y) = image.pixels(y)(x)
      }
      Image(image.height, image.width, newPixels)
  }

  private def rotate270(image: Image): Image = {
    val newPixels = Array.ofDim[RGB](image.height, image.width)

    for {
      y <- 0 until image.height
      x <- 0 until image.width
    }
      {
        newPixels(image.width - 1 - x)(y) = image.pixels(y)(x)
      }
      Image(image.width, image.height, newPixels)
  }
}
