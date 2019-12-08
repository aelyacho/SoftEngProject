package rpgboss.model.resource.random_map_generation

/**   Representation of a room in a container
 *    The room gets a random position inside the container, and random dimensions
 * @param c   Container inside of which the room is situated
 */

class Room(c: Container) {
  val r = scala.util.Random

  val max_hDistance : Int = c.w/3 // Maximum horizontal distance
  val max_vDistance : Int = c.h/3 // Maximum vertical distance

  val x = c.x + r.nextInt(max_hDistance)
  val y = c.y + r.nextInt(max_vDistance)

  var w = c.w - (x - c.x)
  var h = c.h - (y - c.y)

  w = w - r.nextInt(max_hDistance)
  h = h - r.nextInt(max_vDistance)

}
