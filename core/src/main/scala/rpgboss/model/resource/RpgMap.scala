package rpgboss.model.resource

import rpgboss.model._
import rpgboss.lib._
import rpgboss.lib.FileHelper._
import org.json4s.native.Serialization
import scala.collection.JavaConversions._
import java.io._
import java.util.Arrays
import org.json4s.DefaultFormats
import rpgboss.model.resource.random_map_generation.btree.{Btree, EmptyNode, Node}
import rpgboss.model.resource.random_map_generation.{Container}

import scala.collection.mutable.ArrayBuffer

case class RpgMapMetadata(var parent: String,
                          var title: String,
                          var xSize: Int,
                          var ySize: Int,
                          var tilesets: Array[String] =
                            ResourceConstants.defaultTilesets,
                          var autotiles: Array[String] =
                            ResourceConstants.defaultAutotiles,
                          var interior:Boolean = false,
                          var random:Boolean = false,//Boolean used to start the random map generation process
                          var iter:Int = 3,//Amount of iterations for the random map generation algorithm
                          var music: Option[SoundSpec] = None,
                          var battleBackground: String =
                            ResourceConstants.defaultBattleback,
                          var battleMusic: Option[SoundSpec] =
                            ResourceConstants.defaultBattleMusic,
                          var editorCenterX: Float = 0f,
                          var editorCenterY: Float = 0f,
                          var lastGeneratedEventId: Int = 0,
                          var randomEncounterSettings: RandomEncounterSettings =
                            RandomEncounterSettings()) {
  def withinBounds(x: Float, y: Float) = {
    x < xSize && y < ySize && x >= 0 && y >= 0
  }
  def withinBounds(x: Int, y: Int) = {
    x < xSize && y < ySize && x >= 0 && y >= 0
  }
}

/**
 * @param   name  This is the "id" of the map, and should never be changed
 *                once assigned. Otherwise, we get into these messy issues:
 *                 * Need to rename data file and metadata file
 *                 * Need to update the "parent" field of all its children
 *                 * Need to update all the events.
 *
 *                Instead, we should use the "title" field of the metadata
 *                for all cases where we need to refer to the title.
 */
case class RpgMap(proj: Project, name: String, metadata: RpgMapMetadata)
  extends Resource[RpgMap, RpgMapMetadata] {
  def meta = RpgMap

  def saveMapData(d: RpgMapData) = d.writeToFile(proj, name)

  def readMapData(): Option[RpgMapData] = RpgMapData.readFromDisk(proj, name)

  def id = name.split("\\.").head
  def displayId = "%s [%s]".format(metadata.title, id)
  def displayName =
    if (metadata.title.isEmpty()) "[%s]".format(id) else metadata.title
}

/*
 * An explanation of the data format.
 *
 * Each tile on the map is comprised of 3 bytes.
 *
 * Byte 1 value:
 * -2 = autotile
 * -1 = empty tile
 * 0-127 = one of the 128 tilesets possible
 *
 * Byte 2 value:
 * If autotile, then the autotile number from 0-255
 * If regular tile, then x tile index ranging from 0-255
 * If empty, ignored.
 *
 * Byte 3 value:
 * If autotile, then this byte describes the border configuration.
 *    See Autotile.DirectionMasks for how this works specifically.
 * If regular tile, then the y tile index from 0-255
 * If empty, ignored
 */
object RpgMap extends MetaResource[RpgMap, RpgMapMetadata] {
  def rcType = "rpgmap"
  def mapExt = "rpgmap"
  def keyExts = Array(mapExt)

  val minXSize = 20
  val minYSize = 15

  val maxXSize = 500
  val maxYSize = 500

  val initXSize = 80
  val initYSize = 60

  val bytesPerTile = 3

  val autotileByte: Byte = -2
  val emptyTileByte: Byte = -1

  def autotileSeed = Array[Byte](autotileByte, 0, 0)
  def emptyTileSeed = Array[Byte](emptyTileByte, 0, 0)

  val initFloorTile : Array[Byte] = Array(autotileByte, 38, 1)//ADDED
  val initWallTile : Array[Byte] = Array(autotileByte, 29, 1)//ADDED

  /**
   * Generates an array made the seed bytes, repeated
   */
  def makeRowArray(nTiles: Int, seed: Array[Byte]) = {
    assert(seed.length == bytesPerTile)
    val newArray = Array.tabulate[Byte](nTiles * bytesPerTile)(
      i => seed(i % bytesPerTile))
    assert(newArray.length == nTiles * bytesPerTile)
    newArray
  }

  def generateName(id: Int) =
    Utils.generateFilename("Map", id, mapExt)

  def defaultInstance(proj: Project, name: String) = {
    val idxOfDot = name.indexOf(".")
    val title = if (idxOfDot > 0) name.substring(0, idxOfDot) else name
    val m = RpgMapMetadata(
      "", title,
      initXSize, initYSize)
    apply(proj, name, m)
  }

  def drawLine(a: Array[Array[Byte]], tile: Array[Byte], x1: Int, y1: Int, x2: Int, y2: Int, i: Int): Unit ={
    var ctr = i
    if(y1==y2){//Horizontal
      while(ctr>0){
        var c = x1
        while(c<x2) {
          a(y1+ctr-1)(c*3) = tile(0)
          a(y1+ctr-1)(c*3+1) = tile(1)
          a(y1+ctr-1)(c*3+2) = tile(2)
          c = c + 1
        }
        ctr = ctr - 1
      }
    }
    else if(x1==x2){//Vertical
      while(ctr>0){
        var c = y1
        while(c<y2) {
          a(c)(x1*3) = tile(0)
          a(c)(x1*3+1) = tile(1)
          a(c)(x1*3+2) = tile(2)
          c = c + 1
        }
        ctr = ctr - 1
      }
    }
  }

  def drawRect(a: Array[Array[Byte]], tile: Array[Byte], x: Int, y: Int, w: Int, h: Int, fill :Boolean): Unit ={
    if(fill){
      var ctr = 0
      while(ctr<h){
        drawLine(a, tile, x, y+ctr, x+w, y+ctr, 1)
        ctr = ctr + 1
      }
    }
    else {
      drawLine(a, tile, x, y, x + w, y, 1)
      drawLine(a, tile, x + w, y, x + w, y + h, 1)
      drawLine(a, tile, x, y + h, x + w + 1, y + h, 1) //+1 to fill bottom right corner
      drawLine(a, tile, x, y, x, y + h, 1)
    }
  }

  def generateTree(width :Int, height :Int, iter: Int) ={
    def splitContainer(c: Container, iter: Int): Node[Container] ={
      if(iter == 0) new Node[Container](c, EmptyNode, EmptyNode)
      else {
        val sr = c.randomSplit
        new Node[Container](c, splitContainer(sr(0), iter-1), splitContainer(sr(1), iter-1))
      }
    }
    splitContainer(Container(0, 0, width, height), iter)
  }

  def drawTree(a: Array[Array[Byte]], t: Btree[Container], floorTile: Array[Byte], wallTile: Array[Byte]): Unit ={//Parameters Added
    t.getLeafs.foreach((n: Btree[Container]) =>{
      drawRect(a, wallTile, n.value.x, n.value.y, n.value.w, n.value.h ,true)//29 1
      drawRect(a, floorTile, n.value.room.x, n.value.room.y, n.value.room.w, n.value.room.h ,true)//38 1
      //println("ROOM", "WIDTH: " + n.value.room.w, "HEIGHT: " + n.value.room.h)
    })

    def drawCorridor(b :Btree[Container]){//Drawing Corridors
      if(b != EmptyNode){
        if(b.left!=EmptyNode&&b.right!=EmptyNode){
          val p1 = b.left.value.center
          val p2 = b.right.value.center
          drawLine(a, floorTile, p1.x, p1.y, p2.x, p2.y, 1)
        }
      }
    }
    t.foreach(drawCorridor)
  }

  def emptyMapData(xSize: Int, ySize: Int) = {
    def autoLayer() = {
      // Make a whole row of that autotile triples
      val row = makeRowArray(xSize, autotileSeed)
      // Make multiple rows
      Array.fill(ySize)(row.clone())
    }
    def emptyLayer() = {
      val row = makeRowArray(xSize, emptyTileSeed)
      Array.fill(ySize)(row.clone())
    }

    RpgMapData(autoLayer(), emptyLayer(), emptyLayer(), Map())
  }

  def randomMapData(xSize: Int, ySize: Int, iter:Int, floorTile: Array[Byte], wallTile: Array[Byte]) = {
    def autoLayer() = {
      // Make a whole row of that autotile triples
      val row = makeRowArray(xSize, autotileSeed)
      // Make multiple rows
      val x = Array.fill(ySize)(row.clone())
      val tree = generateTree(xSize, ySize, iter)
      drawTree(x, tree, floorTile, wallTile)
      x
    }
    def emptyLayer() = {
      val row = makeRowArray(xSize, emptyTileSeed)
      Array.fill(ySize)(row.clone())
    }

    RpgMapData(autoLayer(), emptyLayer(), emptyLayer(), Map())
  }

  def defaultMapData() = emptyMapData(initXSize, initYSize)
}
