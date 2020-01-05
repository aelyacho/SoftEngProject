package rpgboss.model.resource.random_map_generation.draw_tree

import rpgboss.model.resource.random_map_generation.Container
import rpgboss.model.resource.random_map_generation.btree.{Btree, EmptyNode}

/**
 *  Basic way for drawing a Binary Tree (representing the randomly generated map) in an array
 *
 *  Following the Strategy Design Pattern
 */

object NormalDrawTree extends DrawTreeStrategy {
  def inBound(x: Int, y: Int, w: Int, h: Int)={
    0<=x && x<=w && 0<=y && y<=h
  }

  /** drawLine:   Draws a line in the array (a) from (x1, y1) to (x2, y2)
   *
   * @param a       2d array that represents the map
   * @param tile    tile used in the drawing [array of 3 elements (type, xTile, yTile)]
   */
  def drawLine(a: Array[Array[Byte]], tile: Array[Byte], x1: Int, y1: Int, x2: Int, y2: Int): Unit ={
    val width = a(0).length/3 // Divided by 3, because its an array of tile tuples(3 bytes representing one tile)
    val height = a.length
    if(!(inBound(x1, y1, width, height)&&inBound(x2, y2, width, height))){
      throw new Exception("Out of bounds")
    }
    else{
      if(y1==y2){//Horizontal
        var c = x1
        while(c<=x2) {
          a(y1)(c*3) = tile(0)
          a(y1)(c*3+1) = tile(1)
          a(y1)(c*3+2) = tile(2)
          c = c + 1
        }
      }
      else if(x1==x2){//Vertical
        var c = y1
        while(c<=y2) {
          a(c)(x1*3) = tile(0)
          a(c)(x1*3+1) = tile(1)
          a(c)(x1*3+2) = tile(2)
          c = c + 1
        }
      }
    }
  }

  /**  drawRect:    Draws a rectangle in the array (a) at position (x, y) with dimensions (w, h)
   *
   * @param a       array representing the map
   * @param tile    tile used in the drawing [array of 3 elements (type, xTile, yTile)]
   * @param fill    boolean to specify whether or not the rectangle has to be filled
   */

  def drawRect(a: Array[Array[Byte]], tile: Array[Byte], x: Int, y: Int, w: Int, h: Int, fill :Boolean): Unit ={
    if(fill){
      var ctr = 0
      while(ctr<h){
        drawLine(a, tile, x, y+ctr, x+w-1, y+ctr)
        ctr = ctr + 1
      }
    }
    else {
      drawLine(a, tile, x, y, x+w-1, y)
      drawLine(a, tile, x+w-1, y, x+w-1, y+h-1)
      drawLine(a, tile, x, y+h-1, x+w-1, y+h-1) //+1 to fill bottom right corner
      drawLine(a, tile, x, y, x, y+h-1)
    }
  }

  /**   drawTree:   draws a tree by using the above defined tools (drawRectangle, drawLine)
   *
   * @param a           array representing the map
   * @param t           tree to be drawn
   * @param floorTile   tile to be used for the floor
   * @param wallTile    tile to be used for the walls
   */

  def drawTree(a: Array[Array[Byte]], t: Btree[Container], floorTile: Array[Byte], wallTile: Array[Byte]): Unit ={//Parameters Added
    t.getLeafs.foreach((n: Btree[Container]) =>{
      drawRect(a, wallTile, n.value.x, n.value.y, n.value.w, n.value.h ,true)
      drawRect(a, floorTile, n.value.room.x, n.value.room.y, n.value.room.w, n.value.room.h ,true)
    })

    /** drawCorridors:    draws corridors between rooms, by drawing a line connecting the children containers of a node
     */
    def drawCorridor(b :Btree[Container]){//Drawing Corridors
      if(b != EmptyNode){
        if(b.left!=EmptyNode&&b.right!=EmptyNode){
          val p1 = b.left.value.center
          val p2 = b.right.value.center
          drawLine(a, floorTile, p1.x, p1.y, p2.x, p2.y)
        }
      }
    }
    t.foreach(drawCorridor)
    println(a.deep.mkString("\n"))
  }
}
