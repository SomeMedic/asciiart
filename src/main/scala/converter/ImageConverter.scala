package converter

import model.{Grayscale, Image, RGB}
import java.awt.{Color, Font, Graphics2D, RenderingHints}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import scala.util.{Try, Success, Failure}


trait ImageConverter {
  def rgbToGrayscale(rgb: RGB): Grayscale = {
    // rgb to grayscale conversion formula
    val value = (0.3 * rgb.red + 0.59 * rgb.green + 0.11 * rgb.blue).toInt
    
    // check correct RGB range
    Grayscale(value.max(0).min(255))
  }
  
  def imageToGrayscale(image: Image): Array[Array[Grayscale]] = {
    image.pixels.map(_.map(rgbToGrayscale))
  }
  
  // get ASCII character for each grayscale value
  def grayscaleToAscii(grayscale: Grayscale)(using table: AsciiTable): Char = {
    table.getChar(grayscale.value)
  }
  
  def convert(image: Image)(using table: AsciiTable): String = {
    val grayscaleImage = imageToGrayscale(image)
    
    // convert each pixel to ASCII character
    grayscaleImage.map(row => row.map(pixel => grayscaleToAscii(pixel)).mkString).mkString("\n")
  }

  def saveAsciiArt(ascii: String, path: String): Either[String, Unit] = {
    Try {
      val lines = ascii.split("\n")
      val fontSize = 10
      val font = new Font(Font.MONOSPACED, Font.PLAIN, fontSize)
      
      // Create temporary image to measure text size
      val tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
      val g2d = tempImg.createGraphics()
      g2d.setFont(font)
      val metrics = g2d.getFontMetrics
      
      val charWidth = metrics.charWidth('W')
      val charHeight = metrics.getHeight
      
      // Create image of the required size
      val width = lines(0).length * charWidth
      val height = lines.length * charHeight
      val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
      
      val graphics = image.createGraphics()
      graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                               RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      graphics.setColor(Color.WHITE)
      graphics.fillRect(0, 0, width, height)
      graphics.setColor(Color.BLACK)
      graphics.setFont(font)
      
      // Draw text
      lines.zipWithIndex.foreach { case (line, i) =>
        graphics.drawString(line, 0, (i + 1) * charHeight)
      }
      
      graphics.dispose()
      
      val format = path.substring(path.lastIndexOf('.') + 1)
      if (!ImageIO.write(image, format, new File(path))) {
        throw new IllegalStateException(s"Couldn't save ASCII art image!")
      }
    } match {
      case Success(_) => Right(())
      case Failure(ex) => Left(s"Failed to save ASCII art: ${ex.getMessage}")
    }
  }
}
