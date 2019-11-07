package rpgboss.model.resource

class Room(c: Container) {
  val r = scala.util.Random

  val v1 : Int = c.w/3+1//+1 to avoid 0
  val v2 : Int = c.h/3+1

  val x = c.x + r.nextInt(v1) + 1
  val y = c.y + r.nextInt(v2) + 1

  var w = c.w - (x - c.x)
  var h = c.h - (y - c.y)

  w = w - r.nextInt(v1) - 1
  h = h - r.nextInt(v2) - 1
}
