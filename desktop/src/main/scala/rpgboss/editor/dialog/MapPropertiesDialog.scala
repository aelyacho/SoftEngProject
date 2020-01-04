package rpgboss.editor.dialog

import javax.swing.ImageIcon
import rpgboss.editor.Internationalized._
import rpgboss.editor.StateMaster
import rpgboss.editor.cache.MapTileCache
import rpgboss.editor.misc.RandomEncounterSettingsPanel
import rpgboss.editor.resourceselector.{BattleBackgroundField, MusicField, TilesetArrayField}
import rpgboss.editor.uibase.SwingUtils.{boolField, _}
import rpgboss.editor.uibase._
import rpgboss.model._
import rpgboss.model.resource._
import rpgboss.model.resource.random_map_generation.MapGeneratorConstants

import scala.swing._

class MapPropertiesDialog(
                           owner: Window,
                           sm: StateMaster,
                           title: String,
                           initialMap: RpgMap,
                           initialMapData: RpgMapData,
                           onOk: (RpgMap, RpgMapData) => Any)
  extends StdDialog(owner, title + " - " + initialMap.displayId) {

  centerDialog(new Dimension(400, 400))

  val model = initialMap.metadata.copy()

  def okFunc(): Unit = {
    val newMap = initialMap.copy(metadata = model)

    val newMapData = {
      if(model.random){//Added for random map generation (if random boolean is true, get random map data instead of the default data)
        RpgMap.randomMapData(model.xSize, model.ySize, model.iter, floorTile, wallTile)
      }else {
        if (model.xSize == initialMap.metadata.xSize &&
          model.ySize == initialMap.metadata.ySize) {
          initialMapData
        } else {
          initialMapData.resized(model.xSize, model.ySize)
        }
      }
    }

    onOk(newMap, newMapData)
    close()
  }

  val fTitle = textField(model.title, model.title = _)

  val interior = boolField(
    getMessage("Interior"),
    model.interior,
    model.interior = _)

  val fWidth = new NumberSpinner(
    RpgMap.minXSize, RpgMap.maxXSize,
    model.xSize,
    model.xSize = _)

  val fHeight = new NumberSpinner(
    RpgMap.minYSize, RpgMap.maxYSize,
    initialMap.metadata.ySize,
    model.ySize = _)

  val fMusic = new MusicField(
    owner, sm, model.music,
    model.music = _)

  val fBattleback = new BattleBackgroundField(
    owner, sm, model.battleBackground, model.battleBackground = _,
    allowNone = false)

  val fBattleMusic = new MusicField(
    owner, sm, model.battleMusic, model.battleMusic = _, allowNone = false)

  val fTilesets =
    new TilesetArrayField(owner, sm, model.tilesets, model.tilesets = _)

  val fRandomEncounters = new RandomEncounterSettingsPanel(
      owner, sm.getProjData, model.randomEncounterSettings)

  val random = boolField(//Checkbox for activating random map generation
    "Random Map Generation",
    model.random,
    (bool:Boolean)=>{
      randomGuiComponentsList.foreach(x=> x.enabled_=(bool))// Enable/Disable random GUI components
      model.random = bool
    }) //Added option in user interface for random map generation


  val iter = new NumberSpinner( //Added adjustable iter for Random map generation
    1, MapGeneratorConstants.MAX_ITER,
    model.iter,
    model.iter = _)

  var floorTile : Array[Byte] = RpgMap.initFloorTile//Added
  var wallTile : Array[Byte] = RpgMap.initWallTile//Added

  val autotiles = Autotile.list(sm.getProj).map(Autotile.readFromDisk(sm.getProj, _))
  val tilesets = Tileset.list(sm.getProj).map(Tileset.readFromDisk(sm.getProj, _))

  def changeTile(newTile: Array[Byte], setTile: (Array[Byte]) => Unit, canPass: Boolean): Unit ={
    if(canPass == isPassable(newTile)) {
      setTile(newTile)
    }
  }

  def isPassable(tile: Array[Byte]): Boolean ={
    val typ = tile(0)
    val x = tile(1)
    val y = tile(2)
    var passability = 0

    if(typ!=RpgMap.autotileByte){
      passability = tilesets(typ).metadata.blockedDirsAry(y)(x)
    }else{
      passability = autotiles(y*8+x).metadata.blockedDirs
    }

    passability == 0
  }

  def onFloorSelection(s: Array[Array[Array[Byte]]]): Unit = {
    if(isPassable(s(0)(0))){
      floorTileSelector.canSelectLabel.text_=("YES")
    }else{
      floorTileSelector.canSelectLabel.text_=("NO")
    }
  }

  def onWallSelection(s: Array[Array[Array[Byte]]]): Unit = {
    if(isPassable(s(0)(0))){
      wallTileSelector.canSelectLabel.text_=("NO")
    }else{
      wallTileSelector.canSelectLabel.text_=("YES")
    }
  }

  val floorTileSelector = new TileSelectionDialog(owner, sm, (selectedTile: Array[Byte]) => changeTile(selectedTile, setFloorTile, true), onFloorSelection)
  val wallTileSelector = new TileSelectionDialog(owner, sm, (selectedTile: Array[Byte]) => changeTile(selectedTile, setWallTile, false), onWallSelection)

  val floorTileSelectionBtn : Button = Button("") {
    floorTileSelector.open()
  }

  val wallTileSelectionBtn : Button = Button("") {
    wallTileSelector.open()
  }

  def setFloorTile(newTile: Array[Byte]) = {
    floorTile = newTile
    floorTileSelectionBtn.icon_=(new ImageIcon(tileCache.cache.get((floorTile(0), floorTile(1), floorTile(2), 0))))
  }

  def setWallTile(newTile: Array[Byte]) = {
    wallTile = newTile
    wallTileSelectionBtn.icon_=(new ImageIcon(tileCache.cache.get((wallTile(0), wallTile(1), wallTile(2), 0))))
  }

  val tileCache = new MapTileCache(sm.assetCache, initialMap)

  floorTileSelectionBtn.icon_=(new ImageIcon(tileCache.cache.get((floorTile(0), floorTile(1), floorTile(2), 0))))
  wallTileSelectionBtn.icon_=(new ImageIcon(tileCache.cache.get((wallTile(0), wallTile(1), wallTile(2), 0))))

  val randomGuiComponentsList = List(iter, floorTileSelectionBtn, wallTileSelectionBtn)//A list of all the components of the random generation GUI
  randomGuiComponentsList.foreach(x=> x.enabled_=(model.random))// Initialise components' enabled_

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new DesignGridPanel {
        row()
          .grid(leftLabel(getMessageColon("Map_ID"))).add(new TextField {
            text = initialMap.id
            enabled = false
          })

        row().grid(leftLabel(getMessageColon("Map_Title"))).add(fTitle).add(interior)

        row().grid(leftLabel(getMessageColon("Dimensions")))
          .add(leftLabel(getMessage("Width"))).add(leftLabel(getMessage("Height")))
        row().grid()
          .add(fWidth).add(fHeight)

        row().grid(leftLabel(getMessageColon("Music")))
          .add(fMusic)

        row().grid(leftLabel(getMessageColon("Battle_Background")))
          .add(fBattleback)
        row().grid(leftLabel(getMessageColon("Battle_Music")))
          .add(fBattleMusic)

        row().grid(leftLabel(getMessageColon("Tilesets"))).add(fTilesets)
      }

      contents += new DesignGridPanel {// Grid panel for the UI of random map generation
        row().grid().add(random)
        row().grid(lbl("Iterations: ")).add(iter)
        row().grid(lbl("Floor Tile: ")).add(floorTileSelectionBtn)
        row().grid(lbl("Wall Tile: ")).add(wallTileSelectionBtn)
      }

      contents += fRandomEncounters

    }

    contents += new DesignGridPanel {
      addButtons(okBtn, cancelBtn)
    }
  }
}