package rpgboss.model.event

import rpgboss.model.{MapLoc, SpriteSpec}
import rpgboss.model.resource.MapInfo

/** generate a random value between 0 and x */
object Randomizer {
  def getRandomVal(x: Int): Int = scala.util.Random.nextInt(x)
}

abstract class RpgEventCreator(protected var currentEventId:Int) {
  def createEvent() : Array[RpgEvent]
}

class NPCCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {
  private val npcAnimation = List(AnimationType.NONE.id, AnimationType.RANDOM_MOVEMENT.id)
  private val storeItems = Randomizer.getRandomVal(32)
  private def startRange: Int = if ((storeItems-12)>=0) storeItems-12 else 0
  private val quotesFile = scala.io.Source.fromFile("/Users/aeya/Desktop/rpgboss-team3/desktop/src/main/resources/quotes.txt")
  private val quotes = quotesFile.getLines().toArray

  /** @return a Random action (Open a store or show a random quote) depending on the evtype value which is a random value between 0 and 1 */
  private def getRandomAction(evType: Int): Array[EventCmd] = {
    evType match {
      case 0 => Array(OpenStore(IntArrayParameter(Array.range(startRange, storeItems))))
      case 1 => Array(ShowText(Array(quotes(Randomizer.getRandomVal(quotes.length))), useCharacterFace = true, characterId = Randomizer.getRandomVal(5)))
      case _ => throw new Exception("Invalid Event Type")
    }


  }

  def createEvent(): Array[RpgEvent] = {
    val state = RpgEventState()
    val x = MapInfo.getXCoordinate() + 0.5f
    val y = MapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    state.sprite = Some(SpriteSpec("sys/vx_chara01_a.png", Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = npcAnimation(Randomizer.getRandomVal(2))
    state.cmds = getRandomAction(Randomizer.getRandomVal(2))
    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
  }
}

class EnemyCreator(eventId:Int) extends RpgEventCreator(eventId) {
  private val enemyAnimation = List(AnimationType.FOLLOW_PLAYER.id, AnimationType.RANDOM_MOVEMENT.id)

  def createEvent(): Array[RpgEvent] = {
    val state = RpgEventState()
    val x = MapInfo.getXCoordinate() + 0.5f
    val y = MapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    state.sprite = Some(SpriteSpec("sys/vx_chara08_a.png" ,Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = enemyAnimation(Randomizer.getRandomVal(2))
    state.cmds = Array(StartBattle(IntParameter(Randomizer.getRandomVal(6))))
    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
  }
}

class TreasureChestCreator(eventId:Int) extends RpgEventCreator(eventId) {
  def createEvent(): Array[RpgEvent] = {
    val amountOfGold = Randomizer.getRandomVal(300) + 1

    val closedState = RpgEventState()
    closedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,0,0))
    closedState.runOnceThenIncrementState = true
    closedState.cmds = Array(AddRemoveGold(quantity = IntParameter(amountOfGold)), ShowText(Array(s"Obtained gold: $amountOfGold G")))

    val openedState = RpgEventState()
    openedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,2,0))
    openedState.cmds = Array(ShowText(Array("Empty")))

    val x = MapInfo.getXCoordinate() + 0.5f
    val y = MapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(closedState, openedState)))
  }
}

class TeleporterCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {
  def createEvent() = Array(RpgEvent())

  def createEvent(distance: Int, mapName:String): Array[RpgEvent] = {
    val teleporter1 = RpgEventState()
    val x1 = MapInfo.getXCoordinate() + 0.5f
    val y1 = MapInfo.getYCoordinate() + 0.5f
    val newId1:Int = (() => {currentEventId += 1; currentEventId})()
    val newUserY1 = y1 + 1f
    MapInfo.forgetCoordinates()

    val teleporter2 = RpgEventState()
    var x2 = MapInfo.getXCoordinate() + 0.5f
    var y2 = MapInfo.getYCoordinate() + 0.5f
    val newId2:Int = (() => {currentEventId += 1; currentEventId})()
    var newUserY2 = y2 + 1f

    while(Math.abs(x1 - x2) < distance || Math.abs(y1 - y2) < distance) {
      MapInfo.forgetCoordinates()
      x2 = MapInfo.getXCoordinate() + 0.5f
      y2 = MapInfo.getYCoordinate() + 0.5f
      newUserY2 = y2 + 1f
    }

    teleporter1.sprite = Some(SpriteSpec("sys/!object.png",2,0,0))
    teleporter1.cmds = Array(Teleport(MapLoc(mapName, x2, newUserY2)))

    teleporter2.sprite = Some(SpriteSpec("sys/!object.png",2,0,0))
    teleporter2.cmds = Array(Teleport(MapLoc(mapName, x1, newUserY1)))

    Array(RpgEvent(newId1, "Event%05d".format(newId1), x1, y1, Array(teleporter1)), RpgEvent(newId2, "Event%05d".format(newId2), x2, y2, Array(teleporter2)))
  }
}