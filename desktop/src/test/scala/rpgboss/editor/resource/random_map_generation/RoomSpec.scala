package rpgboss.editor.resource.random_map_generation

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.Container

class RoomSpec extends UnitSpec{
  def fixture = new {
    val cx = 0
    val cy = 0
    val cw = 30
    val ch = 30

    val container = Container(cx, cy, cw, ch)
    val room = container.room
  }

  "Room" should "be inside a container" in {
    val f = fixture
    f.room.x should be >= f.cx
    f.room.y should be >= f.cy
    f.room.w should be <= f.cw
    f.room.h should be <= f.ch
  }
}
