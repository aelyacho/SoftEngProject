package rpgboss.model.resource.random_map_generation.btree

/**
 *  The representation of a non-empty node in a Binary Tree
 *
 * @param v   Value inside the node
 * @param l   Left child of the node
 * @param r   Right child of the node
 * @tparam T  Type of the value inside a node
 */

class Node[T](v: T, l: Btree[T], r: Btree[T]) extends Btree[T]{
  def value = v
  def left = l
  def right = r

  override def toString: String = "Node with value:" + value
}

