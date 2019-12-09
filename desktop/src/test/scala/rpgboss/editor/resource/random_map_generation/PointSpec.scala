package rpgboss.editor.resource.random_map_generation

import rpgboss.model.resource.random_map_generation.Point
import rpgboss.editor.UnitSpec

class PointSpec extends UnitSpec {
  def fixture = new {
    val y = 15
    val x = 10
    val point = new Point(x, y)
  }

  "A point's x-value" should "equal x" in {
    val f = fixture
    f.point.x should equal (f.x)
  }

  "A point's y-value" should "equal y" in {
    val f = fixture
    f.point.y should equal (f.y)
  }
}
