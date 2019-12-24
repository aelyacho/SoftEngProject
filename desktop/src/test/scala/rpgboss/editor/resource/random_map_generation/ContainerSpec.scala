package rpgboss.editor.resource.random_map_generation
import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.{Container, Point, Room}

import scala.util.Random

class ContainerSpec extends UnitSpec {
  def fixture = new {
    val y = 15
    val x = 10
    val w = 20
    val h = 30
    val c = new Point(x+w/2, y+h/2)

    val container = Container(x, y, w, h)
  }

  "Container's x-value" should "equal x" in {
    val f = fixture
    f.container.x should equal (f.x)
  }

  "Container's y-value" should "equal y" in {
    val f = fixture
    f.container.y should equal (f.y)
  }

  "Container's width" should "equal w" in {
    val f = fixture
    f.container.w should equal (f.w)
  }

  "Container's height" should "equal h" in {
    val f = fixture
    f.container.h should equal (f.h)
  }

  "Container's center" should "equal c" in {
    val f = fixture
    f.container.center.x should equal (f.c.x)
    f.container.center.y should equal (f.c.y)
  }

  "Container" should "contain a room" in {
    val f = fixture
    f.container.room shouldBe a [Room]
  }

  "randomSplit method" should "split the container into two containers" in {
    val f = fixture
    val newContainers = f.container.randomSplit
    newContainers should have length (2)
    newContainers shouldBe a [Array[Container]]
    val containerSurface = f.container.w * f.container.h
    val newContainersSurface = (newContainers(0).w * newContainers(0).h) + (newContainers(1).w * newContainers(1).h)
    newContainersSurface should equal (containerSurface)
  }

  "Container's toString method" should "give back a certain string" in{
    val f = fixture
    f.container.toString should equal ("Container at " + (f.container.x, f.container.y))
  }
}
