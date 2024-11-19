package Main

import converter.{AsciiTable, AsciiTables, ImageConverter}
import loader.JpgLoader
import model.Image
import javax.imageio.ImageIO
import java.io.File
import scala.util.{Try, Success, Failure}

object Main extends ImageConverter {
  def main(args: Array[String]): Unit = {
    val loader = new JpgLoader()
    
    loader.load("input.jpg") match {
      case Right(image) => {
        // Convert to ASCII
        given AsciiTable = AsciiTables.Extended
        val ascii = convert(image)
        
        // Print ASCII art to console
        println(ascii)
        
        // Save ASCII art as image
        saveAsciiArt(ascii, "output.jpg") match {
          case Right(_) => println("ASCII-art saved successfully!")
          case Left(error) => println(s"Error saving ASCII-art: $error")
        }
      }
      case Left(error) => println(s"Error: $error")
    }
  }
}
