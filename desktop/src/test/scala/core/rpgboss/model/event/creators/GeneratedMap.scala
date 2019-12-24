package core.rpgboss.model.event.creators

import rpgboss.model.resource.{Container, Node, RpgMap, mapInfo}
/** trait used to simulate a map during the tests */
trait GeneratedMap {

  val btree: Node[Container] = RpgMap.generateTree(RpgMap.maxXSize, RpgMap.maxYSize, RpgMap.ITER)
  mapInfo.createRepr(btree)

}
