package rpgboss.model.event.creators

import rpgboss.model.SpriteSpec
import rpgboss.model.event.{AnimationType, EventCmd, IntArrayParameter, OpenStore, RpgEvent, RpgEventState, ShowText}
import rpgboss.model.resource.mapInfo

class NPCCreator(eventId:Int) extends RpgEventCreator(eventId:Int) {
  private val npcAnimation = List(AnimationType.NONE.id, AnimationType.RANDOM_MOVEMENT.id)
  private val storeItems = Randomizer.getRandomVal(32)
  private def startRange: Int = if ((storeItems-12)>=0) storeItems-12 else 0
  /** quotes fetched from https://gist.github.com/signed0/d70780518341e1396e11 */
  private val quotesFile = scala.io.Source.fromFile("./desktop/src/main/resources/quotes.txt")
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
    val x = mapInfo.getXCoordinate() + 0.5f
    val y = mapInfo.getYCoordinate() + 0.5f
    val newId:Int = (() => {currentEventId += 1; currentEventId})()

    state.sprite = Some(SpriteSpec("sys/vx_chara01_a.png", Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = npcAnimation(Randomizer.getRandomVal(2))
    state.cmds = getRandomAction(Randomizer.getRandomVal(2))
    Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
  }
}
