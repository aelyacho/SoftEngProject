package rpgboss.model.event.creators

import rpgboss.model.event.RpgEvent

abstract class RpgEventCreator (protected var currentEventId:Int){
  def createEvent(optionalArgs:Array[Any] = Array()) : Array[RpgEvent]
}
