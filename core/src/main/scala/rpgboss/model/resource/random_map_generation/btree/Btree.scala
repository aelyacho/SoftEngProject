package rpgboss.model.resource.random_map_generation.btree

abstract class Btree[+T] {
  def value : T
  def left : Btree[T]
  def right : Btree[T]

  def foreach(f :Btree[T]=>Any): Unit ={
    if (this != EmptyNode){
      f(this)
      this.left.foreach(f)
      this.right.foreach(f)
    }
  }

  def getLeafs: List[Btree[T]] ={
    if (this == EmptyNode) List()
    else if(left == EmptyNode && right == EmptyNode) List(this)
    else left.getLeafs ::: right.getLeafs
  }
}
