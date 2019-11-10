package rpgboss.model.resource
import Array._

/*
  This object contains a 2D-array representation for each room, and methods
  that makes it easier to obtain a location to put events on the map

  Below are listed the most important variable and methods:
  - totalRooms: Is the amount of rooms on the map
  - rooms: Is an array containing each room of the map
  - createRepr: This function will create the 2D-array for each room (is already done automatically when the map is generated)
  - getXCoordinates: Gives a random x-coordinate of a random room
  - getYCoordinates: Gives a random y-coordinate in the same room as the x-coordinate
     => Notice that getXCoordinates & getYCoordinates could give coordinates where there could already be an element .A system preventing
        this has yet to be implemented.
  - eventAdded & decorationAdded: These functions are to be used after the events/decorations have been successfully added to the map.
                                  Their job is to modify the 2D-vector of the corresponding room to denote the presence of these elements.
 */
object mapInfo {
  var totalRooms = 0
  var rooms:Array[Room] = Array()

  private val numGen = scala.util.Random
  private var currentRoomId = -1 //This variable will contain the index of the room where the x- and y-coordinates will be chosen from
  private var currentX = 0
  private var currentY = 0

  object mapElement extends Enumeration {
    val EVENT = 1
    val DECORATION = 2
  }

  def createRepr(tree:Btree[Container]) = {
    val roomList = tree.getLeafs.map(node => node.value.room)
    totalRooms = roomList.length
    rooms = new Array[Room](totalRooms)
    roomList.copyToArray(rooms, 0, totalRooms)
    rooms.foreach(room => {
      val roomHeight = room.h
      val roomWidth = room.w

      /*
      <--Used for debugging, ignore-->
      if (roomHeight <= 0 || roomWidth <= 0){
        println("FOUND A CHAMBER: (" + roomHeight + ", " + roomWidth + ")")
      }
       */

      val roomRepr = ofDim[Int](roomHeight, roomWidth)
      room.representation = roomRepr
    })
  }

  def getXCoordinate() = {
    if(currentRoomId == -1) {
      /*
        A little bug is currently present in the implementation
        of the map generation algorithm. To counter this bug,
        the following solution has been thought.

        Since this solution is temporarily (until the bug gets
        fuxed), it can be ignored.
       */
      currentRoomId = numGen.nextInt(totalRooms)
      var room = rooms(currentRoomId)
      while(room.h <= 0 || room.w <= 0) {
        //Choosing a random room
        currentRoomId = numGen.nextInt(totalRooms)
        room = rooms(currentRoomId)
      }
    }

    val room = rooms(currentRoomId)
    /*
      When choosing a random x-coordinate, this will happen with respect
      to the coordinates of the 2D-array of the room. But those coordinates
      do not correspond to those of the map, therefore it is needed to
      convert the x-coordinate of the 2D-array.
      This is done by adding to room.x (top-left corner of the room) the x-coordinate
      of the 2D-array
     */
    currentX = numGen.nextInt(room.w)
    val roomX = room.x
    roomX +  currentX
  }

  //Similar to getXCoordinate
  def getYCoordinate() = {
    if(currentRoomId == -1) {
      currentRoomId = numGen.nextInt(totalRooms)
      var room = rooms(currentRoomId)
      while(room.h <= 0 || room.w <= 0) {
        currentRoomId = numGen.nextInt(totalRooms)
        room = rooms(currentRoomId)
      }
    }

    val room = rooms(currentRoomId)
    currentY = numGen.nextInt(room.h)
    val roomY = room.y
    roomY +  currentY
  }

  def eventAdded() = {
    val roomRepr = rooms(currentRoomId).representation
    roomRepr(currentY)(currentX) = mapElement.EVENT
    rooms(currentRoomId).representation = roomRepr

    currentRoomId = -1 //The current room can be forgotten
  }

  def decorationAdded() = {
    val roomRepr = rooms(currentRoomId).representation
    roomRepr(currentY)(currentX) = mapElement.DECORATION
    rooms(currentRoomId).representation = roomRepr

    currentRoomId = -1 //The current room can be forgotten
  }

  def getRoomWidth(roomID:Int) = rooms(roomID).w
  def getRoomHeight(roomID:Int) = rooms(roomID).h
}
