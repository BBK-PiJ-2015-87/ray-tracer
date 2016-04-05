package com.mildlyskilled

object Scene {

  import java.io.{FileReader, LineNumberReader}
  import scala.annotation.tailrec

  def fromFile(file: String) = {
    val in = new LineNumberReader(new FileReader(file))
    val (objects, lights) = readLines(in, Nil, Nil)
    new Scene(objects, lights)
  }

  @tailrec
  private def readLines(in: LineNumberReader, objects: List[Shape], lights: List[Light]): (List[Shape], List[Light]) = {
    val line = in.readLine
    if (line == null) {
      (objects, lights) match {
        case (Nil, _) => throw new RuntimeException("no objects")
        case (_, Nil) => throw new RuntimeException("no lights")
        case (os, ls) => (os.reverse, ls.reverse)
      }
    }
    else {
      val fields = line.replaceAll("#.*", "").trim.split("\\s+").filter(_ != "")
      fields.headOption match {
        case Some("sphere") =>
          val Array(x, y, z, rad, r, g, b, shine) = fields.tail.map(_.toFloat)
          readLines(in, Sphere(Vector(x, y, z), rad, Colour(r, g, b), shine) :: objects, lights)
        case Some("light") =>
          val Array(x, y, z, r, g, b) = fields.tail.map(_.toFloat)
          readLines(in, objects, Light(Vector(x, y, z), Colour(r, g, b)) :: lights)
        case None =>
          // blank line
          readLines(in, objects, lights)
        case Some(x) =>
          throw new RuntimeException("unknown command: " + x)
      }
    }
  }
}

class Scene private(val objects: List[Shape], val lights: List[Light]) {

  private def this(p: (List[Shape], List[Light])) = this(p._1, p._2)

}

