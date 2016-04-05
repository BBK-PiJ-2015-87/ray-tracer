import com.mildlyskilled._

object Tracer extends App {

  val (inFile, outFile) = ("src/main/resources/input.dat", "output.png")
  val settings = new Settings
  val counter = new Counter
  val camera = new Camera
  val scene = Scene.fromFile(inFile)

  val renderEngine = new RenderEngine(scene, settings, counter, camera)

  val image = new Image(settings.width, settings.height)


  render(scene, outFile, settings)

  println("rays cast " + counter.rayCount)
  println("rays hit " + counter.hitCount)
  println("light " + counter.lightCount)
  println("dark " + counter.darkCount)

  def render(scene: Scene, outfile: String, renderSettings: Settings) = {


    // Init the coordinator -- must be done before starting it.
    Coordinator.init(image, outfile)

    // TODO: Start the Coordinator actor.

    renderEngine.render()

    // TODO:
    // This one is tricky--we can't simply send a message here to print
    // the image, since the actors started by traceImage haven't necessarily
    // finished yet.  Maybe print should be called elsewhere?
    Coordinator.print
  }

}
