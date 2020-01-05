package rpgboss.editor.resource.random_map_generation

import rpgboss.editor.UnitSpec
import rpgboss.model.event.RpgEvent
import rpgboss.model.event.creators.{EnemyCreator, NPCCreator}
import rpgboss.model.resource.mapInfo
import rpgboss.model.resource.random_map_generation.{MapGenerator, MapGeneratorConstants}

class MapGeneratorConstantsSpec extends UnitSpec with TileArrayMaker {

  "For the same seed, the generated maps" should "be the same" in {

    /** Procedure used to create events for the tests
     *
     * @param a   : The amount of events to spawn (Enemies and NPCs)
     * @return    : Returns a list of all the spawned events
     */
    def spawnEvents(a:Int)={
      val enemyCreator = new EnemyCreator(0)
      val npcCreator = new NPCCreator(0)
      var result : List[RpgEvent]= List()
      var c = 0
      while(c<a){
        result = enemyCreator.createEvent()(0) :: result
        result = npcCreator.createEvent()(0) :: result
        c += 1
      }
      result
    }

    /** testSeed tests whether or not if 3 maps are generated with the same seed, they are all the same.
     *
     * @param seed            : The seed to test with
     * @param width           : The width of the maps
     * @param height          : The height of the maps
     * @param iter            : The amount of iterations for the map generation algorithm
     * @param eventsCount     : The amount of events to generate
     */
    def testSeed(seed: Int, width: Int, height: Int, iter: Int, eventsCount: Int)={
      // Dummy tiles
      val testFloorTile: Array[Byte] = Array(1, 3, 3)
      val testWallTile: Array[Byte] = Array(2, 8, 7)


      // Map 1
      MapGeneratorConstants.setSeed(seed)
      val testMapArray1 = make2dTileArray(width, height)
      val btree1 = MapGenerator.generateMap(width, height, iter, testMapArray1, testFloorTile, testWallTile)
      // Initialise mapInfo
      mapInfo.createRepr(btree1)
      // Events
      val events1 = spawnEvents(eventsCount)

      // Map 2
      MapGeneratorConstants.setSeed(seed)
      val testMapArray2 = make2dTileArray(width, height)
      val btree2 = MapGenerator.generateMap(width, height, iter, testMapArray2, testFloorTile, testWallTile)
      // Initialise mapInfo
      mapInfo.createRepr(btree2)
      // Event creators
      val events2 = spawnEvents(eventsCount)

      // Map 3
      MapGeneratorConstants.setSeed(seed)
      val testMapArray3 = make2dTileArray(width, height)
      val btree3 = MapGenerator.generateMap(width, height, iter, testMapArray3, testFloorTile, testWallTile)
      // Initialise mapInfo
      mapInfo.createRepr(btree3)
      // Event creators
      val events3 = spawnEvents(eventsCount)

      /// Testing equality
      // Maps:
      testMapArray1 should equal(testMapArray2)
      testMapArray1 should equal(testMapArray3)
      testMapArray2 should equal(testMapArray3)

      // Events: (checking whether the positions of the spawned events are the same in the different scenarios)
      events1.map((e:RpgEvent) => Array(e.x, e.y)) should contain theSameElementsInOrderAs (events2.map((e:RpgEvent) => Array(e.x, e.y)))
      events1.map((e:RpgEvent) => Array(e.x, e.y)) should contain theSameElementsInOrderAs (events3.map((e:RpgEvent) => Array(e.x, e.y)))
      events2.map((e:RpgEvent) => Array(e.x, e.y)) should contain theSameElementsInOrderAs (events3.map((e:RpgEvent) => Array(e.x, e.y)))
    }

    // Calling the test function, with different parameters
    testSeed(0, 100, 100, 4, 3)
    testSeed(25, 50, 80, 3, 4)
    testSeed(98, 156, 134, 5, 5)
  }
}
