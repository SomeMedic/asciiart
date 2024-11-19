package model

case class Image (height: Int, width: Int, pixels: Array[Array[RGB]])

case class Grayscale (value: Int)

case class RGB (red: Int, green: Int, blue: Int)
