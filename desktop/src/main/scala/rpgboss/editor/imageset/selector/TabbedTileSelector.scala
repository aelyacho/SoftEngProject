package rpgboss.editor.imageset.selector

import scala.swing._
import scala.swing.event._
import rpgboss.model._
import rpgboss.model.resource._
import rpgboss.editor._
import rpgboss.editor.uibase._
import java.awt.image.BufferedImage
import rpgboss.editor.misc.SelectsMap

trait TileBytesSelector {
  // Called when we switch pages to that page
  def selectionBytes: Array[Array[Array[Byte]]]
}

/**
 * Panel to browse and select the available tiles
 * @param sm
 */
class TabbedTileSelector(sm: StateMaster,
                         allowMultiselect : Boolean = true,
                         onSelection: (Array[Array[Array[Byte]]]) => Unit = (s: Array[Array[Array[Byte]]]) => Unit)//Added
  extends BoxPanel(Orientation.Horizontal) with SelectsMap {
  val thisSidebar = this

  def defaultTileCodes = Array(Array(Array(
    RpgMap.autotileByte, 0.asInstanceOf[Byte], 0.asInstanceOf[Byte])))

  def selectionBytes = selectedTileCodes

  // This var must always have at least be 1x1.
  // array of row vectors, so selectedTileCodes(y)(x)
  private var selectedTileCodes: Array[Array[Array[Byte]]] = defaultTileCodes

  def selectMap(mapOpt: Option[RpgMap]) = setContent(mapOpt map { map =>
    new TabbedPane() {
      tabPlacement = Alignment.Bottom

      def selectBytesF(selected: Array[Array[Array[Byte]]]) = {//Added
        onSelection(selected)
        selectedTileCodes = selected
      }

      // Create a tab for all autotiles
      pages += new TabbedPane.Page("A",
        new AutotileSelector(sm, map, selectBytesF, allowMultiselect = allowMultiselect))//Added

      // Create multiple tabs for the remaining tiles
      map.metadata.tilesets.zipWithIndex.map({
        case (tsName, i) =>
          val tileset = sm.assetCache.getTileset(tsName)
          val tabComponent =
            new TilesetTileSelector(i.toByte, tileset, selectBytesF,  allowMultiselect = allowMultiselect)//Added

          pages += new TabbedPane.Page("T%d".format(i), tabComponent)
      })

      // select first Autotile code
      selectedTileCodes = defaultTileCodes

      listenTo(selection)

      reactions += {
        case SelectionChanged(pane) => selectedTileCodes =
          selection.page.content.asInstanceOf[TileBytesSelector].selectionBytes
      }
    }
  })

  def setContent(cOpt: Option[Component]) = {
    contents.clear()
    cOpt map { contents += _ }

    preferredSize = new Dimension(preferredSize.getWidth().toInt, 500)
    revalidate()
    repaint()
  }
}

