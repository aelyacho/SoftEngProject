package rpgboss.editor.resource.random_map_generation

import rpgboss.editor.UnitSpec
import rpgboss.model.resource.random_map_generation.{MapGenerator, MapGeneratorConstants}

class MapGeneratorConstantsSpec extends UnitSpec with TileArrayMaker {

  "For the same seed, the generated maps" should "be the same" in {

    /** testSeed tests whether or not if 3 maps are generated with the same seed, they are all the same.
     *
     * @param seed    : The seed to test with
     * @param width   : The width of the maps
     * @param height  : The height of the maps
     * @param iter    : The amount of iterations for the map generation algorithm
     */
    def testSeed(seed: Int, width: Int, height: Int, iter: Int)={
      val testFloorTile: Array[Byte] = Array(1, 3, 3)
      val testWallTile: Array[Byte] = Array(2, 8, 7)
      val testMapArray1 = make2dTileArray(width, height)
      val testMapArray2 = make2dTileArray(width, height)
      val testMapArray3 = make2dTileArray(width, height)

      // Map 1
      MapGeneratorConstants.setSeed(seed)
      MapGenerator.generateMap(width, height, iter, testMapArray1, testFloorTile, testWallTile)

      // Map 2
      MapGeneratorConstants.setSeed(seed)
      MapGenerator.generateMap(width, height, iter, testMapArray2, testFloorTile, testWallTile)

      // Map 3
      MapGeneratorConstants.setSeed(seed)
      MapGenerator.generateMap(width, height, iter, testMapArray3, testFloorTile, testWallTile)

      // Testing equality
      testMapArray1 should equal(testMapArray2)
      testMapArray1 should equal(testMapArray3)
      testMapArray2 should equal(testMapArray3)
    }

    // Calling the test function, with different parameters
    testSeed(0, 100, 100, 4)
    testSeed(25, 50, 80, 3)
    testSeed(98, 156, 134, 5)
  }
}
