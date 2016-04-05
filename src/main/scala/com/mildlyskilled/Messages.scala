package com.mildlyskilled

sealed trait RenderMessage
case class Render(start: Int, end: Int) extends RenderMessage
case class Result (x: Int, y: Int, color: Colour) extends RenderMessage
