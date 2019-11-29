package rpgboss.editor.model.resource.random_map_generation.btree

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.btree.{EmptyNode, Node}

class NodeSpec extends UnitSpec{
  def fixture = new {
    val v = 15
    val l = EmptyNode
    val r = EmptyNode
    val node = new Node[Int](v, l, r)
  }

  "A node's value" should "equal v" in {
    val f = fixture
    f.node.value should equal (f.v)
  }

  "A node's left" should "equal l" in {
    val f = fixture
    f.node.left should equal (f.l)
  }

  "A node's right" should "equal r" in {
    val f = fixture
    f.node.right should equal (f.r)
  }

  "The node's toString method" should "give back a certain string" in{
    val f = fixture
    f.node.toString should equal ("Node with value:" + f.v)
  }
}
