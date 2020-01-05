package rpgboss.model.event.creators

import rpgboss.model.SpriteSpec
import rpgboss.model.event.{AddRemoveGold, IntParameter, RpgEvent, RpgEventState, ShowText}
import rpgboss.model.resource.mapInfo
import math._
class TreasureChestCreator(eventId:Int) extends RpgEventCreator(eventId) {
  def createEvent(args:Array[Any] = Array()): Array[RpgEvent] = {
    val amountOfGold = Randomizer.getRandomVal(300)

    val closedState = RpgEventState()
    closedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,0,0))
    closedState.runOnceThenIncrementState = true
    closedState.cmds = Array(AddRemoveGold(quantity = IntParameter(amountOfGold)), ShowText(Array(s"Obtained gold: $amountOfGold G")))

    val openedState = RpgEventState()
    openedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,2,0))
    openedState.cmds = Array(ShowText(Array("Empty")))

    val x = mapInfo.getXCoordinate() + 0.5f
    val y = mapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(closedState, openedState)))
  }
}
