package com.mildlyskilled

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool


class Coordinator(workers: Int, image: Image, outFile: String, scene: Scene, settings: Settings, counter: Counter, camera: Camera) extends Actor {

  var waiting = image.width * image.height

  val renderNodesRouter = context.actorOf(Props(new RenderEngine(scene, settings, counter, camera)).withRouter(RoundRobinPool(workers)), name = "renderNodes")
  val interval = 10;

  //helper lists for rendering segments
  val startOfSegments = for (i <- 0 to image.height by interval) yield i
  val endOfSegments = startOfSegments.tail


  def receive = {
    case Render =>
      for (i <- 0 until endOfSegments.length) renderNodesRouter ! Render(startOfSegments(i), endOfSegments(i), i)

    case Result(xPos, yPos, ccolor) =>
      set(xPos, yPos, ccolor)
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