package rpgboss.model.event

import rpgboss.model._
import rpgboss.lib.Utils
import rpgboss.lib.DistinctCharacterSet

object EventTrigger extends RpgEnum {
  val NONE = Value(0, "None")
  val BUTTON = Value(1, "Button")
  val PLAYERTOUCH = Value(2, "Touch_By_Player")
  val EVENTTOUCH = Value(3, "Touch_By_Other_Event")
  val ANYTOUCH = Value(4, "Touch_By_Any")
  val AUTORUN = Value(5, "Autorun_parallel")

  def default = BUTTON
}

object AnimationType extends RpgEnum {
  val NONE = Value(0, "None")
  val FOLLOW_PLAYER = Value(1, "Follow_Player")
  val RANDOM_MOVEMENT = Value(2, "Random_Movement")
  val RUN_FROM_PLAYER = Value(3, "Run_From_Player")

  def default = NONE
}

object EventHeight extends RpgEnum {
  val UNDER = Value(0, "Under_Player")
  val SAME = Value(1, "Same_Level_As_Player")
  val OVER = Value(2, "Always_On_Top_Of_Player")

  def default = UNDER
}

/**
 * @param   states          Guaranteed to be size at least 1, unless this event
 *                          is an event instance, in which case size must be 0.
 * @param   eventClassId    -1 if normal event. Otherwise, the id of the event
 *                          class this is an instance of.
 * @param   params          Variables to bind for the event class.
 */
case class RpgEvent(
  id: Int = 0,
  var name: String = "",
  var x: Float = 0,
  var y: Float = 0,
  var states: Array[RpgEventState] = Array(RpgEventState()),
  var eventClassId: Int = -1,
  var params: Array[EventParameter[_]] = Array()) {

  def isInstance = eventClassId >= 0
  override def toString() : String = {
  s"id: $id - "+
    s"name: $name - " +
    s"x: $x, y: $y - " +
    s"states: ${states.map(s => s.print())} - " +
    s"eventClassId: $eventClassId - " +
    s"params: $params "
  }
}

object RpgEvent {
  /** generate a random value between 0 and x */
  private def getRandomVal(x: Int): Int = scala.util.Random.nextInt(x)

  /** Creates a random Animation depending on the evType: enemy (evtype=0) or a NPC (evtype=1)
   *
   *  @param evType the chosen event (enemy or npc atm)
   *  @return a random animationtype id that will be used in getRandomState
   */
  private def randomAnimation(evType: Int): Int = {
    val enemyAnimation = List(AnimationType.FOLLOW_PLAYER.id, AnimationType.RANDOM_MOVEMENT.id)
    val npcAnimation = List(AnimationType.NONE.id, AnimationType.RANDOM_MOVEMENT.id)
    evType match {
      case 0 => enemyAnimation(getRandomVal(2))
      case 1 => npcAnimation(getRandomVal(2))
      case _ => throw new Exception("Invalid Event Type")
    }
  }

  /** Creates a random RpgEventState depending on the evType
   *
   *  @param evType the chosen event (enemy or npc atm)
   *  @return an Array containing a new RpgEventState with the right spriteset and random behaviour(height, trigger, etc...)
   */
  private def getRandomState(evType: Int) = {
    /** generate the right sprite set depending on the event type (enemy if 0, npc if 1) */
    def getSpriteSet(): String = {
      if (evType == 0) {
        "sys/vx_chara08_a.png"
      } else if (evType == 1) {
        "sys/vx_chara01_a.png"
      } else throw new Exception("Invalid Event Type")
    }

    val state = RpgEventState()

    state.sprite = Some(SpriteSpec(getSpriteSet(),getRandomVal(8),getRandomVal(3),getRandomVal(4)))
    state.height = getRandomVal(3)
    state.trigger = 1 //getRandomVal(6)
    state.animationType = randomAnimation(evType)
    state.cmds = Array(StartBattle(IntParameter(getRandomVal(6))))

    Array(state)
  }

  def blank(idFromMap: Int, x: Float, y: Float) =
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y,
             Array(RpgEventState()))

  def blankInstance(idFromMap: Int, x: Float, y: Float) =
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y,
             Array.empty, 0)

  /** Create an Enemy Event (event type = 0) */
  def enemyEvent(idFromMap: Int, x: Float, y: Float) =
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y, getRandomState(0))

  /** Create a NPC Event (event type = 1) */
  def npcEvent(idFromMap: Int, x: Float, y: Float) =
    RpgEvent(idFromMap, "Event%05d".format(idFromMap), x, y, getRandomState(1))
}




/**
 * @param   cmds                        May be empty.
 * @param   runOnceThenIncrementState   If true, increments the state after
 *                                      running the commands.
 */
case class RpgEventState(
  var conditions: Array[Condition] = Array(),
  var sprite: Option[SpriteSpec] = None,
  var height: Int = EventHeight.UNDER.id,
  var affixDirection: Boolean = false,

  var trigger: Int = EventTrigger.BUTTON.id,
  var animationType: Int = AnimationType.NONE.id,
  var runOnceThenIncrementState: Boolean = false,

  var cmds: Array[EventCmd] = Array()) {
  override def toString() : String = {
    "------------------------ RPG EVENT STATE VARIABLES------------------------\n"+
      s"Conditions: ${conditions map(cond => println(cond))}, " +
      s"Sprite: $sprite, " +
        s"Height: $height, " +
      s"affix dir: $affixDirection, " +
      s"trigger: $trigger, " +
      s"animationType: $animationType, " +
      s"runOncethenInc: $runOnceThenIncrementState, " +
        s"cmd's: ${cmds map(cmd => println(cmd))}"
    }

  def print(): Unit = println(this)

  def distinctChars = {
    val set = new DistinctCharacterSet
    for (cmd <- cmds) {
      cmd match {
        case ShowText(lines, _, _, _) => set.addAll(lines)
        case _ => Unit
      }
    }
    set
  }

  def getFreeVariables() = {
    // All variables are free right now since there's exposed EventCmd to
    // bind them (for now).
    cmds.flatMap(_.getParameters().filter(
        _.valueTypeId == EventParameterValueType.LocalVariable.id))
  }

  def copyEssentials() = {
    val newState = RpgEventState()
    newState.sprite = sprite
    newState.height = height
    newState.affixDirection = affixDirection
    newState
  }
  def copyAll() = Utils.deepCopy(this)
}
