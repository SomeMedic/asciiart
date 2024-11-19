package loader

import model.{Image, RGB}

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import scala.util.Try
import scala.util.Success
import scala.util.Failure

class PngLoader extends ImageLoader {
  def supportedFormats: String = "png"

  def load(path: String): Either[String, Image] = {
    Try {
      val bufferedImage = ImageIO.read(new File(path))
      if (bufferedImage == null) {
        throw new IllegalArgumentException("Cannot read image.")
      }

      val width = bufferedImage.getWidth
      val height = bufferedImage.getHeight

      val pixels = Array.ofDim[RGB](height, width)

      for {
        y <- 0 until height
        x <- 0 until width
      } {
        val pixel = bufferedImage.getRGB(x, y)
        val red = (pixel >> 16) & 0xFF
        val green = (pixel >> 8) & 0xFF
        val blue = pixel & 0xFF
        pixels(y)(x) = RGB(red, green, blue)
      }
      Image(height, width, pixels)
    } match {
      case Success(image) => Right(image)
      case Failure(ex) => Left(s"Failed to load image: ${ex.getMessage}")
    }
  }
  
  def save(path: String, image: Image): Either[String, Unit] = {
    Try {
      // creating new buffered image with required sizes
      val bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
      for {
        y <- 0 until image.height
        x <- 0 until image.width
      }
        {
          val rgb = image.pixels(y)(x)
          // collect all RGB components to one 32-bit number
          val pixel = (rgb.red << 16) | (rgb.green << 8) | (rgb.blue)
          bufferedImage.setRGB(x, y, pixel)
        }
        val outputFile = new File(path)
        
        if (!ImageIO.write(bufferedImage, "png", outputFile)) {
          throw new IllegalStateException(s"Couldn't save PNG image!")
        }
    } match {
      case Success(_) => Right(())
      case Failure(exception) => Left(s"Couldn't save PNG image! ${exception.getMessage}")
    }
  }
}

