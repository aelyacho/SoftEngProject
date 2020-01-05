package rpgboss.model.resource.random_map_generation
import rpgboss.model.resource.random_map_generation.btree.{Btree, EmptyNode, Node}
import rpgboss.model.resource.random_map_generation.draw_tree.{DrawTreeStrategy, NormalDrawTree}

/**   MapGenerator:   Object that handles the random map generation
 */

object MapGenerator {

  /** Strategy for drawing the tree (which represents the map)*/
  val drawTreeStrategy : DrawTreeStrategy = NormalDrawTree

  /** generateTree:  constructs the BSP tree for the map generation
   *    This procedure starts off with one node containing the initial container representing the whole map,
   *    and then goes through (iter) amount of iterations of splitting containers and building a tree.
   *
   * @param width     width of the map
   * @param height    height of the map
   * @param iter      amount of iterations
   */


  def generateTree(width :Int, height :Int, iter: Int) ={
    def splitContainer(c: Container, iter: Int): Btree[Container] ={
      if(iter == 0) new Node[Container](c, EmptyNode, EmptyNode)
      else {
        val sr = c.randomSplit
        new Node[Container](c, splitContainer(sr(0), iter-1), splitContainer(sr(1), iter-1))
      }
    }
    splitContainer(Container(0, 0, width, height), iter)
  }

  /**   generateMap:    Calls the above defined procedures to generate a map, and draw it on the provided array (a)
   */

  def generateMap(width :Int, height :Int, iter: Int, a: Array[Array[Byte]], floorTile: Array[Byte], wallTile: Array[Byte]) ={
    val tree = generateTree(width, height, iter)
    drawTreeStrategy.drawTree(a, tree, floorTile, wallTile)
    tree
  }
}
