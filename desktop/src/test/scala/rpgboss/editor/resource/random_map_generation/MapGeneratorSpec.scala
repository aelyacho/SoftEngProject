package rpgboss.editor.resource.random_map_generation

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.{Container, MapGenerator}
import rpgboss.model.resource.random_map_generation.btree.{Btree, EmptyNode}

class MapGeneratorSpec extends UnitSpec{
  def generateTreeFixture(w: Int, h: Int, i: Int) = new {
    val width = w
    val height = h
    val iter = i
    val tree = MapGenerator.generateTree(width, height, iter)
  }

  def calcSurface(c: Container)={
    c.w * c.h
  }

  "generateTree" should "generate a correct BSP tree" in {
    def testGenerateTree(w: Int, h: Int, i: Int)={
      val f1 = generateTreeFixture(w, h, i)
      f1.tree shouldBe a [Btree[Container]]

      /** Testing if the sum of the surfaces of the children containers are equal to the surface of the parent container
       */
      f1.tree.foreach((n: Btree[Container]) =>
        if(n != EmptyNode){
          if(n.left!=EmptyNode&&n.right!=EmptyNode){
            (calcSurface(n.left.value) + calcSurface(n.right.value)) should equal (calcSurface(n.value))
            }
        }
      )

      /** The BSP tree should have pow(2,i) leafs with i = amount of iterations
       */
      f1.tree.getLeafs.length should equal (scala.math.pow(2,i))
    }

    /** Calling testGenerateTree with different arguments
     */
    testGenerateTree(15, 18, 2)
    testGenerateTree(20, 20, 3)
    testGenerateTree(45, 60, 4)
    testGenerateTree(67, 89, 5)
  }

  /** make2dTileArray:  makes a 2d array of 'tiles' (dummy map)
   * @param w  desired width
   * @param h   desired height
   * @return    A 2d array width dimensions (w*3, h) containing Bytes(0)
   */
  def make2dTileArray(w: Int, h: Int) = {
    val rowArray = Array.tabulate(w*3)((n) => 0.toByte)
    val array = Array.fill(h)(rowArray.clone())
    array
  }

  /**
   *  drawLineFixture:  makes a dummy map and calls drawLine on it
   */
  def drawLineFixture(w: Int, h: Int, tile: Array[Byte], x1: Int, y1: Int, x2: Int, y2: Int) = new {
    val a = make2dTileArray(w, h)
    MapGenerator.drawLine(a, tile, x1, y1, x2, y2)
  }

  "drawLine" should "draw a line on the map" in {
    val testTile = Array[Byte](3, 1, 1)
    val f1 = drawLineFixture(3, 3, testTile, 0, 1, 3, 1)
    val expectedResult1 =
      Array(
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  3, 1, 1,  3, 1, 1),
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0)
      )
    f1.a should equal (expectedResult1)

    val f2 = drawLineFixture(3, 3, testTile, 0, 0, 0, 3)
    val expectedResult2 =
      Array(
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0)
      )
    f2.a should equal (expectedResult2)
  }

  "drawSquare" should "draw a square on the map" in {
    /**
     *  drawSquare is based on on the drawLine method
     */
  }

  "drawTree" should "draw the tree on the map" in {
    /**
     *  drawTree is based on on the drawSquare method
     */
  }
}
