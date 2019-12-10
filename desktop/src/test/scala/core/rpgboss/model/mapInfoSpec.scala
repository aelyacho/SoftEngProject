package core.rpgboss.model

import core.UnitSpec
import rpgboss.model.resource.{RpgMap, mapInfo}

class mapInfoSpec extends UnitSpec {
  val btree = RpgMap.generateTree(RpgMap.maxXSize, RpgMap.maxYSize, RpgMap.ITER)
  mapInfo.createRepr(btree)
  val allRooms = mapInfo.rooms
  var x = mapInfo.getXCoordinate()
  var y = mapInfo.getYCoordinate()

  "mapInfo" should "correctly be initialized" in {
    val totalNodes = btree.getLeafs.length
    val totalRooms = mapInfo.totalRooms
    totalRooms should equal(totalNodes)
    allRooms should have length totalNodes
  }

  "Generated coordinates" should "be in rooms" in {
    val isInRoom = helper.findRoom(x, y, allRooms)
    isInRoom should not equal (-1)
  }
}