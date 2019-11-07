package rpgboss.model.resource

class Node[T](v: T, l: Btree[T], r: Btree[T]) extends Btree[T]{
  def value = v
  def left = l
  def right = r

  override def toString: String = "Node with value:" + value
}

