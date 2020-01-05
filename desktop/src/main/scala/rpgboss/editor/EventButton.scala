package rpgboss.editor

import java.util.concurrent.ThreadLocalRandom
import scala.swing.{Action, Button}

/**
 *  Event Button: used to  implement the different types of event buttons (npc, enemies,..)
 *  The buttons only differ in deployEvent, this is why the functions is only declared here
 * */
abstract class EventButton(evType: String) extends Button {
  var fixedAmount = 0
  var minAmount = 0
  var maxAmount = 0

  def deployEvent() : Unit

  protected def getValue: Int = {
    if (fixedAmount > 0) {
      println(s"Fixed Amount of $evType: " + fixedAmount)
      fixedAmount
    } else if (minAmount > 0 || maxAmount > 0) {
      val randomAmount = ThreadLocalRandom.current().nextInt(minAmount, maxAmount)
      println(s"Random Amount of $evType: "+ randomAmount)
      randomAmount
    } else 0
  }


  action = new Action("Deploy!") {
    def apply(): Unit = {
      deployEvent()
    }
  }

}