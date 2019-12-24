package rpgboss.editor.model.resource.random_map_generation.btree

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.btree.EmptyNode

class EmptyNodeSpec extends UnitSpec{
  def fixture = new {
    val node = EmptyNode
  }

  "An EmptyNode" should "not have a value" in {
    val f = fixture
    a [Exception] should be thrownBy {f.node.value}
  }

  "An EmptyNode" should "not have a left" in {
    val f = fixture
    a [Exception] should be thrownBy {f.node.left}
  }

  "An EmptyNode" should "not have a right" in {
    val f = fixture
    a [Exception] should be thrownBy {f.node.right}
  }

  "The node's toString method" should "give back a certain string" in{
    val f = fixture
    f.node.toString should equal ("Empty node")
  }
}
