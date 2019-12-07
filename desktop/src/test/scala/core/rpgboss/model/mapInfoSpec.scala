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

  "Representation of rooms" should "update accordingly" in {
    /**Testing if the method eventAdded works*/
    mapInfo.eventAdded()
    var roomIdx = helper.findRoom(x, y, allRooms)
    var room = allRooms(roomIdx)
    var roomRepresentation = room.representation
    roomRepresentation(y - room.y)(x - room.x) == mapElement.EVENT

    /**Testing if the method decorationAdded works*/
    x = mapInfo.getXCoordinate()
    y = mapInfo.getYCoordinate()
    mapInfo.decorationAdded()
    roomIdx = helper.findRoom(x, y, allRooms)
    room = allRooms(roomIdx)
    roomRepresentation = room.representation
    roomRepresentation(y - room.y)(x - room.x) == mapElement.DECORATION

    /**Testing if the methods work with coordinates with respect to the map*/
    x = mapInfo.getXCoordinate()
    y = mapInfo.getYCoordinate()
    mapInfo.eventAdded(x, y)
    roomIdx = helper.findRoom(x, y, allRooms)
    room = allRooms(roomIdx)
    roomRepresentation = room.representation
    roomRepresentation(y - room.y)(x - room.x) == mapElement.EVENT
  }
}