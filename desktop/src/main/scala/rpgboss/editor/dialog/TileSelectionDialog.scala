package rpgboss.editor.dialog

import javax.swing.BorderFactory
import rpgboss.editor.Internationalized._
import rpgboss.editor.imageset.selector.TabbedTileSelector
import rpgboss.editor.{Settings, StateMaster, VisibleConnection}
import rpgboss.editor.uibase.SwingUtils._
import rpgboss.editor.uibase.{StdDialog, _}
import rpgboss.model._
import rpgboss.model.resource.RpgMap

import scala.swing._
import scala.swing.event._

class TileSelectionDialog(owner: Window, sm: StateMaster, onOk: Array[Byte] => Any, onSelection: (Array[Array[Array[Byte]]]) => Unit = (s: Array[Array[Array[Byte]]]) => Unit)
  extends StdDialog(owner, "Tile Selection") {

  centerDialog(new Dimension(400, 400))

  val initialMap = {
    val mapStates = sm.getMapStates
    if (mapStates.nonEmpty) {
      val idToLoad =
        if (mapStates.contains(sm.getProj.data.recentMapName))
          sm.getProj.data.recentMapName
        else
          mapStates.keys.min

      mapStates.get(idToLoad).map(_.map)
    } else None
  }

  val tileSelector = new TabbedTileSelector(sm, allowMultiselect = false, onSelection = onSelection)//Added allowMultiselect
  tileSelector.selectMap(initialMap)

  var tile = tileSelector.selectionBytes(0)(0) //selectionBytes gives out a 2d array of tile arrays (3 value arrays that represent a tile)

  def okFunc() = {
    tile = tileSelector.selectionBytes(0)(0)
    onOk(tile)
    close()
  }

  val canSelectLabel = new Label("YES")

  contents = new DesignGridPanel {
    row().grid(lbl("Allowed: ")).add(canSelectLabel)//Added
    row().grid().add(tileSelector)
    addButtons(okBtn, cancelBtn)
  }

  reactions += {
    case MouseClicked(`okBtn`, _, _, 2, _) => okBtn.doClick()
  }
}