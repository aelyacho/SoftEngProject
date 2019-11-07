package rpgboss.model.resource

case class Container (var x : Int,
                      var y : Int,
                      var w : Int,
                      var h : Int){
  var room:Room = new Room(this)

  val H_RATIO = 0.45
  val V_RATIO = 0.45

  val r = scala.util.Random

  def randomSplit: Array[Container] ={
    val c = this
    val o = r.nextInt(2)//Random orientation (0 == Vertical, 1 == Horizontal)
    if(o == 0) { //Vertical
      val rw = r.nextInt(c.w)
      val result = Array(Container(c.x, c.y, rw, c.h), Container(c.x+rw, c.y, c.w-rw, c.h))

      val r1_w_ratio = result(0).w.toFloat / result(0).h
      val r2_w_ratio = result(1).w.toFloat / result(1).h

      if (r1_w_ratio < V_RATIO || r2_w_ratio < V_RATIO) randomSplit
      else {
        result
      }

    }
    else{//Horizontal
      val rh = r.nextInt(c.h)
      val result = Array(Container(c.x, c.y, c.w, rh), Container(c.x, c.y+rh, c.w, c.h-rh))

      val r1_h_ratio = result(0).h.toFloat / result(0).w
      val r2_h_ratio = result(1).h.toFloat / result(1).w

      if (r1_h_ratio < H_RATIO || r2_h_ratio < H_RATIO) randomSplit
      else result
    }
  }

  val center = new Point(x+w/2, y+h/2)

  override def toString: String = "This is a container at " + x + " " + y
}