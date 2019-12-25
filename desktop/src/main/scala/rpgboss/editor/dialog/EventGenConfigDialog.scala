package rpgboss.editor.dialog

import javax.swing.ImageIcon
import rpgboss.editor.StateMaster
import rpgboss.editor.uibase.{DesignGridPanel, NumberSpinner, StdDialog}
import rpgboss.editor.uibase.SwingUtils.lbl
import rpgboss.lib.Utils
import rpgboss.model.event.creators.EnemyCreator

import scala.swing._
import scala.swing.event.MouseClicked

class EventGenConfigDialog(owner: Window, sm: StateMaster)
  extends StdDialog(owner, "Event generation configuration") {

  centerDialog(new Dimension(200, 200))

  /* val enemyButton = new Button() {
    var amount = 0
    action = new Action("Enemies") {
      def apply() = {
        val enemyCreator = new EnemyCreator(getEventId())
        for (_ <- 0 to amount) {
          val enemyEvent = enemyCreator.createEvent()
          drawEvent(enemyEvent)
        }
      }
    }
    icon = new ImageIcon(Utils.readClasspathImage(
      "hendrik-weiler-theme/tool.png"))
  }
*/

  def okFunc() = {
    close()
  }

  val sp1 = new NumberSpinner(0, 50, 0, (x:Int)=> {println("Amount of enemies desired: "+x)})
  val sp2 = new NumberSpinner(0, 50, 0, (x:Int)=> {println("Amount of NPC's desired: "+x)})
  val sp3 = new NumberSpinner(0, 50, 0, (x:Int)=> {println("Amount of treasure chests desired: "+x)})
  val sp4 = new NumberSpinner(0, 50, 0, (x:Int)=> {println("Distance btwn teleporters desired: "+x)})

  contents = new DesignGridPanel {
    row().grid(lbl("Enemies: ")).add(sp1)
    row().grid(lbl("NPC's: ")).add(sp2)
    row().grid(lbl("Treasure Chests: ")).add(sp3)
    row().grid(lbl("Distance between Teleporters: ")).add(sp4)
    addButtons(okBtn, cancelBtn)
  }

  reactions += {
    case MouseClicked(`okBtn`, _, _, 2, _) => okBtn.doClick()
  }

}



