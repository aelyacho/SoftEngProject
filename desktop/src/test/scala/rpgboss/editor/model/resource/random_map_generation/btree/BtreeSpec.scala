package rpgboss.editor.model.resource.random_map_generation.btree

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.btree.{EmptyNode, Node, Btree}

class BtreeSpec extends UnitSpec{
  def fixture = new {
    val v = 10
    val leaf1 = new Node[Int](v, EmptyNode, EmptyNode)
    val leaf2 = new Node[Int](v, EmptyNode, EmptyNode)
    val leaf3 = new Node[Int](v, EmptyNode, EmptyNode)
    val leaf4 = new Node[Int](v, EmptyNode, EmptyNode)
    val node1 = new Node[Int](v, leaf1, leaf2)
    val node2 = new Node[Int](v, leaf3, leaf4)
    val root = new Node[Int](v, node1, node2)

    val leafs = List(leaf1, leaf2, leaf3, leaf4)
    val nodes = leafs ::: List(node1, node2, root)

    var nodeValues: List[Int] = List()
    nodes.foreach((n :Btree[Int])=>{
      nodeValues = n.value :: nodeValues
    })
  }

  "The getLeafs method" should "return a list of the leafs" in {
    val f = fixture
    f.root.getLeafs should equal (f.leafs)
  }

  "The foreach method" should "apply a function to all the nodes in the btree" in {
    val f = fixture
    var nodeValues: List[Int]= List()
    f.root.foreach((n :Btree[Int])=>{
      nodeValues = n.value :: nodeValues
    })
    nodeValues should equal (f.nodeValues)
  }
}
