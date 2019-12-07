package rpgboss.model.event.creators

import rpgboss.model.event.RpgEvent

abstract class RpgEventCreator (protected var currentEventId:Int){
  def createEvent() : Array[RpgEvent]
}
