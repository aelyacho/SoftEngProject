package rpgboss.model.resource
import Array._

object mapInfo {
  var totalRooms = 0
  var rooms:Array[Room] = Array()//List[Room] = List()

  def createRepr(tree:Btree[Container]) = {
    val roomList = tree.getLeafs.map(node => node.value.room)
    totalRooms = roomList.length
    rooms = new Array[Room](totalRooms)
    roomList.copyToArray(rooms, 0, totalRooms)
    rooms.foreach(room => {
      val roomHeight = room.h
      val roomWidth = room.w
      val roomRepr = ofDim[Int](roomHeight, roomWidth)
      room.representation = roomRepr
    })
  }

  def hasEvent(roomID:Int, x:Int, y:Int):Boolean = {
    val roomRepr = rooms(roomID).representation
    val value = roomRepr(y)(x)
    value == 1
  }

  def getXCoordinate(roomID:Int, x:Int) = {
    println("TOTAL ROOMS = " + totalRooms)
    println("ROOM IDX IS " + roomID)
    val room = rooms(roomID)
    val roomX = room.x
    roomX + x
  }

  def getYCoordinate(roomID:Int, y:Int) = {
    val room = rooms(roomID)
    val roomY = room.y
    roomY + y
  }

  def getRoomWidth(roomID:Int) = rooms(roomID).w
  def getRoomHeight(roomID:Int) = rooms(roomID).h
}
