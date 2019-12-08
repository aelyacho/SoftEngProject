package rpgboss.model.resource.random_map_generation

/**
 *  Representation of a container
 *    This class is used to represent the different sections in which the map is split in.
 *    Each container contains one room.
 *
 * @param x   x position of the container
 * @param y   y position of the container
 * @param w   width of the container
 * @param h   height of the container
 */

case class Container (var x : Int,
                      var y : Int,
                      var w : Int,
                      var h : Int){

  var room:Room = new Room(this)

  /**  The ratios are used to make sure the containers have a fairly square shape
   * H_RATIO: Smallest accepted height/width ratio
   * V_RATIO: Smallest accepted width/height ratio
   */
  val H_RATIO = 0.45
  val V_RATIO = 0.45
  val MIN_CONT_DIMENSION = 3

  val r = scala.util.Random

  /**
   * randomSplit: Splits the container into two random containers
   *  The container can either be split vertically or horizontally (random)
   * @return  an array with two containers
   */
  def randomSplit: Array[Container] ={
    val c = this
    val o = r.nextInt(2)//Random orientation (0 == Vertical, 1 == Horizontal)
    if(o == 0) { //Vertical
      val rw = r.nextInt(c.w)

      val c1_width = rw
      val c2_width = c.w-rw

      val r1_w_ratio = c1_width.toFloat / c.h
      val r2_w_ratio = c2_width.toFloat / c.h

      if (r1_w_ratio < V_RATIO || r2_w_ratio < V_RATIO || c1_width<MIN_CONT_DIMENSION || c2_width<MIN_CONT_DIMENSION) randomSplit
      else {
        val result = Array(Container(c.x, c.y, c1_width, c.h), Container(c.x+rw, c.y, c2_width, c.h))
        result
      }

    }
    else{//Horizontal
      val rh = r.nextInt(c.h)

      val c1_height = rh
      val c2_height = c.h-rh

      val r1_h_ratio = c1_height.toFloat / c.w
      val r2_h_ratio = c2_height.toFloat / c.w

      if (r1_h_ratio < H_RATIO || r2_h_ratio < H_RATIO || c1_height<MIN_CONT_DIMENSION || c2_height<MIN_CONT_DIMENSION) randomSplit
      else {
        val result = Array(Container(c.x, c.y, c.w, c1_height), Container(c.x, c.y+rh, c.w, c2_height))
        result
      }
    }
  }

  /** The Center of the container
   */
  val center = new Point(x+w/2, y+h/2)

  override def toString: String = "This is a container at " + x + " " + y
}