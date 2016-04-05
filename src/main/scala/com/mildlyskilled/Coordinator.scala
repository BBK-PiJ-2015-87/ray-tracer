package com.mildlyskilled

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool


class Coordinator(image: Image, outFile: String, scene: Scene, settings: Settings, counter: Counter, camera: Camera) extends Actor {
  var waiting = image.width * image.height //for counting rendered pixels

  val renderNodesRouter = context.actorOf(Props(new RenderingEngine(scene, settings, counter, camera)).withRouter(RoundRobinPool(settings.renderNodeNumber)), name = "renderNodes")

  //helper lists for rendering segments
  val startOfSegments = for (i <- 0 to image.height by settings.renderLineWidth) yield i
  val endOfSegments = startOfSegments.tail

//  for testing purposes
//  val startOfSegments = for (i <- 0 to 800 by 10) yield i
//  val endOfSegments = startOfSegments.tail
//  for (i <- 0 until endOfSegments.length) println(startOfSegments(i)+ "  " + endOfSegments(i))

  def receive = {
    case Start =>
      for (i <- 0 until endOfSegments.length) renderNodesRouter ! Render(startOfSegments(i), endOfSegments(i), i)

    case Result(xPos, yPos, color) =>
      set(xPos, yPos, color)

      if (waiting == 0) {
        println("rays cast " + counter.rayCount)
        println("rays hit " + counter.hitCount)
        println("light " + counter.lightCount)
        println("dark " + counter.darkCount)

        print
        println("Image printed out")
        context.system.shutdown()
        context stop self
      }
  }

  def set(xPos: Int, yPos: Int, color: Colour) = {
    image(xPos, yPos) = color
    waiting -= 1
  }

  def print = {
    assert(waiting == 0)
    image.print(outFile)
  }
}