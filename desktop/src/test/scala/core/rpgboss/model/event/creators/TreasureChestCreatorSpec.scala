package core.rpgboss.model.event.creators

import core.UnitSpec
import rpgboss.model.resource.{RpgMap, mapInfo}
import rpgboss.model.event.{AddRemoveGold, ShowText}
import rpgboss.model.event.creators.TreasureChestCreator

class TreasureChestCreatorSpec extends UnitSpec {
  val btree = RpgMap.generateTree(RpgMap.maxXSize, RpgMap.maxYSize, RpgMap.ITER)
  mapInfo.createRepr(btree)
  val creator = new TreasureChestCreator(0)
  val chest = creator.createEvent()(0)
  val chestStates = chest.states
  val chestBehaviour1 = chestStates(0).cmds(0)

  "TreasureChestCreator" should "create an event corresponding to a treasure chest" in {
    /** A treasure chest is recognisable by its ability to give gold to the player */
    chestBehaviour1 shouldBe a[AddRemoveGold]
  }

  "An empty chest and a non-empty chest" should "be differentiable" in {
    val chestBehaviour2 = chestStates(1).cmds(0)
    chestStates.length should be (2)
    chestStates(0).runOnceThenIncrementState should be (true)
    chestBehaviour1 shouldBe a[AddRemoveGold]
    chestBehaviour2 shouldBe a[ShowText]
  }

  "A treasure chest" should "give a random amount of gold" in {
    var notSame = false
    var ctr = 50
    while (!notSame || ctr > 0) {
      val newChest = creator.createEvent()(0)
      val gold1 = newChest.states(0).cmds(0).getParameters().head.constant
      val gold2 = chestBehaviour1.getParameters().head.constant
      if (gold1 != gold2)
        notSame = true
      else
        ctr -= 1
    }
    notSame should be (true)
  }
}
