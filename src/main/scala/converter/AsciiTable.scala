package converter


trait AsciiTable {
  // get ASCII character for each brightness (0-255)
  def getChar(grayscale: Int): Char

}

class LinearAsciiTable(chars: String) extends AsciiTable {
  require(chars.nonEmpty, "ASCII table cannot be empty!")

  def getChar(grayscale: Int): Char = {
    // normalize brightness index to string's length
    val index = (grayscale.toFloat / 255 * (chars.length - 1)).toInt

    chars(index)
  }
}

class CustomRangeAsciiTable(ranges: Seq[(Range, Char)]) extends AsciiTable {
  require(ranges.nonEmpty, "Ranges cannot be empty")

  def getChar(grayscale: Int): Char = {
    ranges
      .find(_._1.contains(grayscale))
      .map(_._2)
      .getOrElse(ranges.head._2)
  }
}

object AsciiTables {
  val Default = new LinearAsciiTable(".:-=+*#%@")
  val Extended = new LinearAsciiTable(".'`^\\\",:;Il!i><~+_-?][}{1)(|/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$")
  val Minimal = new LinearAsciiTable(".*#")
  val Custom = new CustomRangeAsciiTable(Seq(
    (0 to 50) -> ' ',
    (51 to 100) -> '.',
    (101 to 150) -> '+',
    (151 to 200) -> '#',
    (201 to 255) -> '@'))
}
