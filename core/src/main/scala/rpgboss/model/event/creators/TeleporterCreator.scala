package rpgboss.model.event.creators

import rpgboss.model.{MapLoc, SpriteSpec}
import rpgboss.model.event.{RpgEvent, RpgEventState, Teleport}
import rpgboss.model.resource.mapInfo

class TeleporterCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {

  def createEvent(args:Array[Any]): Array[RpgEvent] = {
    val distance = args(0).asInstanceOf[Int]
    val mapName = args(1).asInstanceOf[String]

    val teleporter1 = RpgEventState()
    val x1 = mapInfo.getXCoordinate() + 0.5f
    val y1 = mapInfo.getYCoordinate() + 0.5f
    val newId1:Int = (() => {currentEventId += 1; currentEventId})()
    val newUserY1 = y1 + 1f
    mapInfo.forgetCoordinates()

    val teleporter2 = RpgEventState()
    var x2 = mapInfo.getXCoordinate() + 0.5f
    var y2 = mapInfo.getYCoordinate() + 0.5f
    val newId2:Int = (() => {currentEventId += 1; currentEventId})()
    var newUserY2 = y2 + 1f

    while(Math.abs(x1 - x2) < distance || Math.abs(y1 - y2) < distance) {
      mapInfo.forgetCoordinates()
      x2 = mapInfo.getXCoordinate() + 0.5f
      y2 = mapInfo.getYCoordinate() + 0.5f
      newUserY2 = y2 + 1f
    }

    teleporter1.sprite = Some(SpriteSpec("sys/!object.png",2,0,0))
    teleporter1.cmds = Array(Teleport(MapLoc(mapName, x2, newUserY2)))

    teleporter2.sprite = Some(SpriteSpec("sys/!object.png",2,0,0))
    teleporter2.cmds = Array(Teleport(MapLoc(mapName, x1, newUserY1)))

    Array(RpgEvent(newId1, "Event%05d".format(newId1), x1, y1, Array(teleporter1)), RpgEvent(newId2, "Event%05d".format(newId2), x2, y2, Array(teleporter2)))
  }
}
