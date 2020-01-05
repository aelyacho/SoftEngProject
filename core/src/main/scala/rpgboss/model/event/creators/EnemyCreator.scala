package rpgboss.model.event.creators

import rpgboss.model.SpriteSpec
import rpgboss.model.event.{AnimationType, IntParameter, RpgEvent, RpgEventState, StartBattle}
import rpgboss.model.resource.mapInfo

class EnemyCreator (eventId:Int) extends RpgEventCreator(eventId) {
    private val enemyAnimation = List(AnimationType.FOLLOW_PLAYER.id, AnimationType.RANDOM_MOVEMENT.id)
    def createEvent(): Array[RpgEvent] = {
      val state = RpgEventState()
      val x = mapInfo.getXCoordinate() + 0.5f
      val y = mapInfo.getYCoordinate() + 0.5f
      val newId:Int = (() => {currentEventId += 1; currentEventId})()

      state.sprite = Some(SpriteSpec("sys/vx_chara08_a.png" ,Randomizer.getRandomVal(8),Randomizer.getRandomVal(3),Randomizer.getRandomVal(4)))
      state.animationType = enemyAnimation(Randomizer.getRandomVal(2))
      state.cmds = Array(StartBattle(IntParameter(Randomizer.getRandomVal(6))))
      mapInfo.eventAdded()
      Array(RpgEvent(newId, "Event%05d".format(newId), x, y, Array(state)))
    }
}

