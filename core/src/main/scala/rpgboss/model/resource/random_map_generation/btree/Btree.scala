package rpgboss.model.resource.random_map_generation.btree

/**
 *  Representation of the Binary Tree
 *
 * @tparam T   The type of the values inside the tree
 */

abstract class Btree[+T] {
  def value : T
  def left : Btree[T]
  def right : Btree[T]

  /**
   * Applies a function to every node in the binary tree
   * @param f   The function to be applied
   */
  def foreach(f :Btree[T]=>Any): Unit ={
    if (this != EmptyNode){
      f(this)
      this.left.foreach(f)
      this.right.foreach(f)
    }
  }

  /**
   * @return  Returns a list containing all the leafs of the tree
   */

  def getLeafs: List[Btree[T]] ={
    if (this == EmptyNode) List()
    else if(left == EmptyNode && right == EmptyNode) List(this)
    else left.getLeafs ::: right.getLeafs
  }
}
