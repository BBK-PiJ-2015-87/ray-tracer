package com.mildlyskilled

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool


class Coordinator(workers: Int, image: Image, outFile: String, scene: Scene, settings: Settings, counter: Counter, camera: Camera) extends Actor {

  var waiting = image.width * image.height

//  val renderNodesRouter = context.actorOf(Props(new RenderEngine(scene, settings, counter, camera)).withRouter(RoundRobinPool(workers)), name = "renderNodes")
  val renderNodesRouter = context.actorOf(Props(new RenderEngine(scene, settings, counter, camera)), name = "renderNodes")

  def receive = {
    case Render =>
//      for (i <- 0 until image.height) renderNodesRouter ! Render(image.width, i)
      renderNodesRouter ! Render(image.width, image.height)

    case Result(x, y, c) =>
      set(x, y, c)
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

  def set(x: Int, y: Int, c: Colour) = {
    image(x, y) = c
    waiting -= 1
  }

  def print = {
    assert(waiting == 0)
    image.print(outFile)
  }
}