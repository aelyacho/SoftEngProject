package core.rpgboss.model.event.creators

import rpgboss.model.resource.random_map_generation.{Container, MapGenerator}
import rpgboss.model.resource.random_map_generation.btree.Node
import rpgboss.model.resource.{RpgMap, mapInfo}
/** trait used to simulate a map during the tests */
trait GeneratedMap {

  val btree = MapGenerator.generateTree(RpgMap.maxXSize, RpgMap.maxYSize, 5)
  mapInfo.createRepr(btree)

}
