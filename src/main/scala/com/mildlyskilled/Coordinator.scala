package com.mildlyskilled

/**
  * TODO
  * Make this an actor and write a message handler for at least the
  * set method.
  */

object Coordinator {
  def init(image: Image, file: String) = {
    renderedImage = image
    outputFile = file
    pixelsLeft = image.width * image.height
  }

  // Number of pixels we're waiting for to be set.
  var pixelsLeft = 0
  var outputFile: String = null
  var renderedImage: Image = null

  // TODO: make set a message
  def set(x: Int, y: Int, c: Colour) = {
    renderedImage(x, y) = c
    pixelsLeft -= 1
  }

  def print = {
    assert(pixelsLeft == 0)
    renderedImage.print(outputFile)
  }
}