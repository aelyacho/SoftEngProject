package rpgboss.editor.resource.random_map_generation

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.{Container, MapGenerator, MapGeneratorConstants}
import rpgboss.model.resource.random_map_generation.btree.{Btree, EmptyNode}
import rpgboss.model.resource.random_map_generation.draw_tree.NormalDrawTree

class MapGeneratorSpec extends UnitSpec with TileArrayMaker {
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
    testGenerateTree(36, 32, 0)

    /** Too many iterations given for a small map surface
     */
    a [Exception] should be thrownBy(MapGenerator.generateTree(20, 20, 10))
    a [Exception] should be thrownBy(MapGenerator.generateTree(35, 25, 8))
  }

  /**
   *  drawLineFixture:  makes a dummy map and calls drawLine on it
   */
  def drawLineFixture(w: Int, h: Int, tile: Array[Byte], x1: Int, y1: Int, x2: Int, y2: Int) = new {
    val a = make2dTileArray(w, h)
    NormalDrawTree.drawLine(a, tile, x1, y1, x2, y2)
  }

  "drawLine" should "draw a line on the map" in {
    // A dummy tile
    val testTile = Array[Byte](3, 1, 1)

    val f1 = drawLineFixture(3, 3, testTile, 0, 1, 2, 1)
    val expectedResult1 =
      Array(
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  3, 1, 1,  3, 1, 1),
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0)
      )
    f1.a should contain theSameElementsInOrderAs (expectedResult1)

    val f2 = drawLineFixture(3, 3, testTile, 0, 0, 0, 2)
    val expectedResult2 =
      Array(
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0),
        Array[Byte](3, 1, 1,  0, 0, 0,  0, 0, 0)
      )
    f2.a should contain theSameElementsInOrderAs (expectedResult2)

    /** Out of bounds tests
     */
    a [Exception] should be thrownBy(drawLineFixture(4, 4, testTile, 0, 3, 5, 3))
    a [Exception] should be thrownBy(drawLineFixture(8, 10, testTile, 4, 5, 4, 12))
  }

  def drawRectFixture(w: Int, h: Int, tile: Array[Byte], x1: Int, y1: Int, rw: Int, rh: Int, fill: Boolean) = new {
    val a = make2dTileArray(w, h)
    NormalDrawTree.drawRect(a, tile, x1, y1, rw, rh, fill)
  }

  "drawRect" should "draw a rect on the map" in {
    // A dummy tile
    val testTile = Array[Byte](3, 1, 1)

    val f1 = drawRectFixture(4, 4, testTile, 0, 0, 3, 3, false)
    val expectedResult1 =
      Array(
        Array[Byte](3, 1, 1,  3, 1, 1,  3, 1, 1,  0, 0, 0),
        Array[Byte](3, 1, 1,  0, 0, 0,  3, 1, 1,  0, 0, 0),
        Array[Byte](3, 1, 1,  3, 1, 1,  3, 1, 1,  0, 0, 0),
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0,  0, 0, 0)
      )
    f1.a should contain theSameElementsInOrderAs (expectedResult1)

    val f2 = drawRectFixture(4, 4, testTile, 1, 1, 3, 3, true)
    val expectedResult2 =
      Array(
        Array[Byte](0, 0, 0,  0, 0, 0,  0, 0, 0,  0, 0, 0),
        Array[Byte](0, 0, 0,  3, 1, 1,  3, 1, 1,  3, 1, 1),
        Array[Byte](0, 0, 0,  3, 1, 1,  3, 1, 1,  3, 1, 1),
        Array[Byte](0, 0, 0,  3, 1, 1,  3, 1, 1,  3, 1, 1)
      )
    f2.a should contain theSameElementsInOrderAs (expectedResult2)

    /** Out of bounds tests
     */
    a [Exception] should be thrownBy(drawRectFixture(4, 4, testTile, 3, 3, 5, 5, true))
    a [Exception] should be thrownBy(drawRectFixture(5, 10, testTile, 4, 5, 2, 10, false))
  }

  "drawTree" should "draw the tree on the map" in {
    /**
     *  Testing the drawTree method, by drawing a known tree in an array and comparing the arrays
     */
    // A dummy tiles
    val testFloorTile = Array[Byte](3, 1, 1)
    val testWallTile = Array[Byte](2, 2, 2)
    val testMapArray = make2dTileArray(10, 10)

    // Setting the seed to 0
    MapGeneratorConstants.setSeed(0)

    val f = generateTreeFixture(10, 10, 1)
    NormalDrawTree.drawTree(testMapArray, f.tree, testFloorTile, testWallTile)

    val expectedMapArray = Array(
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2),
        Array(2, 2, 2, 2, 2, 2, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 2, 2, 2, 2, 2, 2)
    )
    testMapArray should contain theSameElementsInOrderAs (expectedMapArray)

  }
}
