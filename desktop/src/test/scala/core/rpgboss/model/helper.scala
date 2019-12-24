package core.rpgboss.model

import rpgboss.model.resource.random_map_generation.Room

import scala.util.control.Breaks.{break, breakable}

object helper {
  def findRoom(x:Int, y:Int, rooms:Array[Room]): Int = {
    var ctr = 0
    var idx = -1

    breakable {
      for(room <- rooms) {
        val roomX = room.x
        val roomXLim = roomX + room.w
        val roomY = room.y
        val roomYLim = roomY + room.h

        if (((roomX <= x) && (x <= roomXLim)) && ((roomY <= y) && (y <= roomYLim))) {
          idx = ctr
          break
        }
        else
          ctr += 1
      }
    }
    idx
  }
}
