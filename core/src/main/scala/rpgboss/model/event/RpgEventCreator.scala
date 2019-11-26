package rpgboss.model.event

import rpgboss.model.SpriteSpec
import rpgboss.model.event._

/** generate a random value between 0 and x */
object Randomizer {
  def getRandomVal(x: Int): Int = scala.util.Random.nextInt(x)
}

abstract class RpgEventCreator {
  def createEvent(idFromMap:Int, x:Float, y:Float) : RpgEvent
}

class NPCCreator extends RpgEventCreator {
  private val npcAnimation = List(AnimationType.NONE.id, AnimationType.RANDOM_MOVEMENT.id)
  private val storeItems = Randomizer.getRandomVal(32)
  private def startRange: Int = if ((storeItems-12)>=0) storeItems-12 else 0

  def createEvent(idFromMap:Int, x:Float, y:Float) = {
    val state = RpgEventState()
    state.sprite = Some(SpriteSpec("sys/vx_chara01_a.png", Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = npcAnimation(Randomizer.getRandomVal(2))
    state.cmds = Array(OpenStore(IntArrayParameter(Array.range(startRange, storeItems))))
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y, Array(state))
  }
}

class EnemyCreator extends RpgEventCreator {
  private val enemyAnimation = List(AnimationType.FOLLOW_PLAYER.id, AnimationType.RANDOM_MOVEMENT.id)

  def createEvent(idFromMap:Int, x:Float, y:Float) = {
    val state = RpgEventState()
    state.sprite = Some(SpriteSpec("sys/vx_chara08_a.png" ,Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
    state.height = Randomizer.getRandomVal(3)
    state.animationType = enemyAnimation(Randomizer.getRandomVal(2))
    state.cmds = Array(StartBattle(IntParameter(Randomizer.getRandomVal(6))))
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y, Array(state))
  }
}

class TreasureChestCreator extends RpgEventCreator {
  def createEvent(idFromMap:Int, x:Float, y:Float) = {
    val amountOfGold = Randomizer.getRandomVal(300+1)

    val closedState = RpgEventState()
    closedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,0,0))
    closedState.height = 1
    closedState.animationType = AnimationType.NONE.id
    closedState.runOnceThenIncrementState = true
    closedState.cmds = Array(AddRemoveGold(quantity = IntParameter(amountOfGold)), ShowText(Array(s"Obtained gold: $amountOfGold G")))

    val openedState = RpgEventState()
    openedState.sprite = Some(SpriteSpec("sys/!$chest-opengameart-Blarumyrran.png",0,2,0))
    openedState.height = 1
    openedState.animationType = AnimationType.NONE.id
    openedState.cmds = Array(ShowText(Array("Empty")))

    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y, Array(closedState, openedState))
  }
}

class TeleporterCreator extends RpgEventCreator {
  def createEvent(idFromMap:Int, x:Float, y:Float) = {
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y,
      Array.empty, 0)
  }
}