package com.mildlyskilled

class Image (val width: Int, val height: Int){
  import java.awt.image.BufferedImage
  import java.io.File
  import javax.imageio.ImageIO

  val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

  def apply(xPos: Int, yPos: Int) = Colour.fromRGB(image.getRGB(xPos, yPos))

  def update(xPos: Int, yPos: Int, color: Colour) = {
    image.setRGB(xPos, yPos, color.rgb)
  }

  def print(file: String) = {
    val outFile = new File(file)
    val fileFormat = "png"
    ImageIO.write(image, fileFormat, outFile)
  }
}
