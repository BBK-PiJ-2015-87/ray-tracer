import akka.actor.{Props, ActorSystem}
import akka.actor._
import com.mildlyskilled._

object Tracer extends App {

  val (inFile, outFile) = ("src/main/resources/input.dat", "output.png")
  val settings = new Settings
  val counter = new Counter
  val camera = new Camera
  val scene = Scene.fromFile(inFile)

  val image = new Image(settings.width, settings.height)

  val system = ActorSystem("RenderSystem")

  val coordinator = system.actorOf(Props(new Coordinator(image, outFile, scene, settings, counter, camera)), name = "coordinator")

  coordinator ! Start

}
