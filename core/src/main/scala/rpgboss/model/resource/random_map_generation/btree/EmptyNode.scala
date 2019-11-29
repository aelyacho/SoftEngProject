package rpgboss.model.resource.random_map_generation.btree

object EmptyNode extends Btree[Nothing] {
  def value : Nothing = throw new Exception("Cannot get the value of empty node")
  def left : Nothing = throw new Exception("Cannot get the left of empty node")
  def right : Nothing = throw new Exception("Cannot get the right of empty node")

  override def toString = "()"
}