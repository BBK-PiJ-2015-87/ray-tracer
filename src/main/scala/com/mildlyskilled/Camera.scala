package com.mildlyskilled

/**
  * Created by vladimirsivanovs on 05/04/2016.
  */
class Camera {
  val position = Vector.origin
  val viewAngle = 90f // viewing angle

  val frustum = (.5 * viewAngle * math.Pi / 180).toFloat
  val cosF = math.cos(frustum)
  val sinF = math.sin(frustum)
}
