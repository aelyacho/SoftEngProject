package core.rpgboss.model.event.creators

import core.UnitSpec
import rpgboss.model.event.Teleport
import rpgboss.model.event.creators.TeleporterCreator
import rpgboss.model.resource.random_map_generation.MapGenerator
import rpgboss.model.resource.{RpgMap, mapInfo}

class TeleporterCreatorSpec extends UnitSpec{
  val btree = MapGenerator.generateTree(RpgMap.maxXSize, RpgMap.maxYSize, 5)
  mapInfo.createRepr(btree)
  val creator = new TeleporterCreator(0)
  val teleporters = creator.createEvent(Array(20, "test"))
  val teleporter1 = teleporters(0)
  val teleporter2 = teleporters(1)

  "TeleporterCreator" should "create an event corresponding to a teleporter" in {
    val teleporter1Behaviour = teleporter1.states(0).cmds(0)
    val teleporter2Behaviour = teleporter2.states(0).cmds(0)
    /** A teleporter is recognisable by its ability to teleport the player */
    teleporter1Behaviour shouldBe a[Teleport]
    teleporter2Behaviour shouldBe a[Teleport]
  }

  "Teleporters" should "be a pair of events" in {
    teleporters.length should be (2)
  }

  "Teleporters" should "respect the minimum distance" in {
    val teleporter1X = teleporter1.x.toInt
    val teleporter1Y = teleporter1.y.toInt
    val teleporter2X = teleporter2.x.toInt
    val teleporter2Y = teleporter2.y.toInt
    (Math.abs(teleporter1X - teleporter2X) >= 20 || Math.abs(teleporter1Y - teleporter2Y) >= 20) should be (true)
  }
}
