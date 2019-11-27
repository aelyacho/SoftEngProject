package rpgboss.model.event

import rpgboss.model.{SpriteSpec}
import rpgboss.model.MapLoc
import rpgboss.model.resource.mapInfo

/** generate a random value between 0 and x */
object Randomizer {
  def getRandomVal(x: Int): Int = scala.util.Random.nextInt(x)
}

abstract class RpgEventCreator(protected var currentEventId:Int) {
  def createEvent : Array[RpgEvent]
}

class NPCCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {
  private val npcAnimation = List(AnimationType.NONE.id, AnimationType.RANDOM_MOVEMENT.id)
  private val storeItems = Randomizer.getRandomVal(32)
  private def startRange: Int = if ((storeItems-12)>=0) storeItems-12 else 0

  def createEvent() = {
    val state = RpgEventState()
    val x = mapInfo.getXCoordinate() + 0.5f
    val y = mapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    state.sprite = Some(SpriteSpec("sys/vx_chara01_a.png", Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = npcAnimation(Randomizer.getRandomVal(2))
    state.cmds = Array(OpenStore(IntArrayParameter(Array.range(startRange, storeItems))))
    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
  }
}

class EnemyCreator(eventId:Int) extends RpgEventCreator(eventId) {
  private val enemyAnimation = List(AnimationType.FOLLOW_PLAYER.id, AnimationType.RANDOM_MOVEMENT.id)

  def createEvent() = {
    val state = RpgEventState()
    val x = mapInfo.getXCoordinate() + 0.5f
    val y = mapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    state.sprite = Some(SpriteSpec("sys/vx_chara08_a.png" ,Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = enemyAnimation(Randomizer.getRandomVal(2))
    state.cmds = Array(StartBattle(IntParameter(Randomizer.getRandomVal(6))))
    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
  }
}

class TreasureChestCreator(eventId:Int) extends RpgEventCreator(eventId) {
  def createEvent() = {
    val amountOfGold = Randomizer.getRandomVal(300+1)

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

class TeleporterCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {
  def createEvent() = Array(RpgEvent())

  def createEvent(distance: Int, mapName:String) = {
    val teleporter1 = RpgEventState()
    val x1 = mapInfo.getXCoordinate() + 0.5f
    val y1 = mapInfo.getYCoordinate() + 0.5f
    val newId1:Int = (() => {currentEventId += 1; currentEventId})()
    val newUserY1 = y1 + 1f

    val teleporter2 = RpgEventState()
    var x2 = mapInfo.getXCoordinate() + 0.5f
    var y2 = mapInfo.getYCoordinate() + 0.5f
    val newId2:Int = (() => {currentEventId += 1; currentEventId})()
    var newUserY2 = y2 + 1f

    while(Math.abs(x1 - x2) < distance || Math.abs(y1 - y2) < distance) {
      mapInfo.forgetCoordinate()
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