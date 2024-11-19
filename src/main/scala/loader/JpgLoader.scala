package loader

import model.{Image, RGB}

import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import scala.util.Try
import scala.util.Success
import scala.util.Failure

class JpgLoader extends ImageLoader {
  def supportedFormats: String = "jpg"

  def load(path: String): Either[String, Image] = {
    // trying to load image using ImageIO package
    Try {
      val bufferedImage = ImageIO.read(new File(path))
      if (bufferedImage == null) {
        throw new IllegalArgumentException("Cannot read image.")
      }

      // getting height and width of an image
      val width = bufferedImage.getWidth
      val height = bufferedImage.getHeight

      // create an array to store pixels
      val pixels = Array.ofDim[RGB](height, width)

      // read each pixel and converting it to RGB
      for {
        y <- 0 until height
        x <- 0 until width
      } {
        val pixel = bufferedImage.getRGB(x, y)
        val red = (pixel >> 16) & 0xFF
        val green = (pixel >> 8) & 0xFF
        val blue = (pixel >> 0) & 0xFF
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
      val bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
      
      for {
        y <- 0 until image.height
        x <- 0 until image.width
      } {
        val rgb = image.pixels(y)(x)
        val pixel = (rgb.red << 16) | (rgb.green << 8) | (rgb.blue)
        bufferedImage.setRGB(x, y, pixel)
      }
      
      val outputFile = new File(path)
      if (!ImageIO.write(bufferedImage, "jpg", outputFile)) {
        throw new IllegalStateException(s"Couldn't save JPG image!")
      }
    } match {
      case Success(_) => Right(())
      case Failure(exception) => Left(s"Couldn't save JPG image! ${exception.getMessage}")
    }
  }
}
