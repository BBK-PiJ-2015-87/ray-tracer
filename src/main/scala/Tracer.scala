import java.io.File

import com.mildlyskilled.{Scene, Image, Coordinator, Settings}

object Tracer extends App {

  val (inFile, outFile) = ("src/main/resources/input.dat", "output.png")
  val scene = Scene.fromFile(inFile)
  val counter = scene.counter
  val settings = scene.settings

  render(scene, outFile, settings.width, settings.height)

  println("rays cast " + counter.rayCount)
  println("rays hit " + counter.hitCount)
  println("light " + counter.lightCount)
  println("dark " + counter.darkCount)

  def render(scene: Scene, outfile: String, width: Int, height: Int) = {
    val image = new Image(width, height)

    // Init the coordinator -- must be done before starting it.
    Coordinator.init(image, outfile)

    // TODO: Start the Coordinator actor.

    scene.traceImage(width, height)

    // TODO:
    // This one is tricky--we can't simply send a message here to print
    // the image, since the actors started by traceImage haven't necessarily
    // finished yet.  Maybe print should be called elsewhere?
    Coordinator.print
  }

}
