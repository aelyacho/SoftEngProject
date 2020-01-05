package rpgboss.model.resource.random_map_generation.draw_tree

import rpgboss.model.resource.random_map_generation.Container
import rpgboss.model.resource.random_map_generation.btree.Btree

/**
 *  Strategy for drawing the Binary Tree (representing the randomly generated map)
 *
 *  Following the Strategy Design Pattern
 */

abstract class DrawTreeStrategy {
  def drawTree(a: Array[Array[Byte]], t: Btree[Container], floorTile: Array[Byte], wallTile: Array[Byte]): Unit
}
