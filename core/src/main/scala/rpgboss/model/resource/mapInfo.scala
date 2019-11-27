package rpgboss.model.resource
import Array._

/** Contains the representation of the rooms (2D-arrays) and methods to interact with these
  *
  * This object contains a 2D-array representation for each room, and methods
  * that makes it easier to obtain a location to put events on the map
  *
  * Below are listed the most important variable and methods:
  * - totalRooms: Is the amount of rooms on the map
  * - rooms: Is an array containing each room of the map
  * - createRepr: This function will create the 2D-array for each room (is already done automatically when the map is generated)
  * - getXCoordinates: Gives a random x-coordinate of a random room
  * - getYCoordinates: Gives a random y-coordinate in the same room as the x-coordinate
  *    => Notice that getXCoordinates & getYCoordinates could give coordinates where there could already be an element .A system preventing
  *       this has yet to be implemented.
  * - eventAdded & decorationAdded: These functions are to be used after the events/decorations have been successfully added to the map.
  *                                 Their job is to modify the 2D-vector of the corresponding room to denote the presence of these elements.
  *
  */
object mapInfo {
  /** Amount of rooms on the map*/
  var totalRooms = 0
  /** Array containing all the rooms*/
  var rooms:Array[Room] = Array()

  private val numGen = scala.util.Random
  /**Contains the index of the room where the x- and y-coordinates will be chosen from*/
  private var currentRoomId = -1
  /**X-coordinate in the chosen room*/
  private var currentX = 0
  /**Y-coordinate in the chosen room*/
  private var currentY = 0

  /**Defining the semantic of the values found in the representations of the rooms*/
  object mapElement extends Enumeration {
    val FREE = 0
    val EVENT = 1
    val DECORATION = 2
  }

  /** Given the x- and y-coordinate of an element on the map, it will search the corresponding room and
   *  resiz the coordinates to fit the corresponding 2-D array
   *
   * @param x X-coordinate of the element
   * @param y Y-coordinate of the element
   *
   * The procedure will go through all the rooms, searching for the one which the x- and y-coordinates
   * are inside its bounds. Once the room is found, the coordinates are resized by calling convert
   * The index of the room, and the resized coordinates, will be held in the variables currentRoomId,
   * currentX and currentY
   */
  private def findRoom(x:Int, y:Int) = {
    import util.control.Breaks._
    /**Converts the x- and y-coordinates in coordinates that can be used in a 2D-array
     *
     * The x- and y-coordinates given as parameters cannot immediately be used in the 2D-arrays,
     * they first need to be resized to fit the dimension of the corresponding 2D-array. This can be easily
     * achieved by substracting from the coordinates the x/y-coordinate of the room.
     */
    def convert() = {
      val room = rooms(currentRoomId)
      currentX = x - room.x
      currentY = y - room.y
    }

    for(roomId <- 0 until totalRooms) {
      breakable {
        val room = rooms(roomId)
        val roomX = room.x
        val roomXLim = roomX + room.w
        val roomY = room.y
        val roomYLim = roomY + room.h

        if (((roomX <= x) && (x <= roomXLim)) && ((roomY <= y) && (y <= roomYLim))) {
          currentRoomId = roomId
          convert()
          break
        }
      }
    }
  }

  /** Sets a given integer (element) in a 2D-array
   *
   * @param x The x-coordinate of the element
   * @param y The y-coordinate of the element
   * @param element The element to set in the 2D-array
   *
   * The procedure is pretty basic.
   */
  private def addElement(x:Int, y:Int , element:Int) = {
    if(x != currentX && y != currentY)
      findRoom(x, y)

    val roomRepr = rooms(currentRoomId).representation
    roomRepr(currentY)(currentX) = element
    /** After modifying the 2D-array of a room, currentRoomId is invalidated */
    currentRoomId = -1
  }

  /**Creates the representation of the rooms
   *
   * @param tree Tree containing the rooms
   */
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

  /**Generates a random x-coordinate in a random room
   *
   * @return a x-coordinate as an integer
   */
  def getXCoordinate() = {
    /** Check of a random has already been chosen */
    if(currentRoomId == -1)
      currentRoomId = numGen.nextInt(totalRooms)

    val room = rooms(currentRoomId)
    /** Choosing a random x inside the room */
    currentX = numGen.nextInt(room.w)
    val roomX = room.x
    roomX +  currentX
  }

  /** Similar to getXCoordiante*/
  def getYCoordinate() = {
    if(currentRoomId == -1)
      currentRoomId = numGen.nextInt(totalRooms)

    val room = rooms(currentRoomId)
    currentY = numGen.nextInt(room.h)
    val roomY = room.y
    roomY +  currentY
  }

  /** Used to signal when an element has been deleted
   *
   * @param x X-coordinate of the element
   * @param y Y-coordinate of the element
   */
  def elementDeleted(x:Int, y:Int): Unit ={
    findRoom(x, y)
    val room = rooms(currentRoomId)
    val roomRepr = room.representation
    roomRepr(currentY)(currentX) = mapElement.FREE
    currentRoomId = -1
    currentX = -1
    currentY = -1
  }

  /** Used to signal that an event has been added */
  def eventAdded(x:Int = currentX, y:Int = currentY) = addElement(x, y, mapElement.EVENT)
  /** Used to signal that a decoration has been added */
  def decorationAdded(x:Int = currentX, y:Int = currentY) = addElement(x, y, mapElement.DECORATION)

  def forgetCoordinate() = currentRoomId = -1
}
