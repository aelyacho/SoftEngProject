package core.rpgboss.model.event.creators

import core.UnitSpec
import rpgboss.model.event.creators.NPCCreator
import rpgboss.model.event.{AnimationType, OpenStore, RpgEventState, ShowText}
/**
 * test creator, random sprites, animations, actions
 */
class NPCCreatorSpec extends UnitSpec with GeneratedMap {
  val npcCreator = new NPCCreator(0)
  val npcEvent1State: RpgEventState = npcCreator.createEvent()(0).states(0)
  val npcEvent2State: RpgEventState = npcCreator.createEvent()(0).states(0)
  val npcEvent3State: RpgEventState = npcCreator.createEvent()(0).states(0)

  "NPCCreator" should "create events with the behaviour of a NPC" in {

    val event1Action = npcEvent1State.cmds(0)
    val event2Action = npcEvent2State.cmds(0)
    val event3Action = npcEvent3State.cmds(0)

    /** NPC: show a random line of dialogue, or open the item shop interface*/
    event1Action.isInstanceOf[ShowText] || event1Action.isInstanceOf[OpenStore] shouldBe true
    event2Action.isInstanceOf[ShowText] || event2Action.isInstanceOf[OpenStore] shouldBe true
    event3Action.isInstanceOf[ShowText] || event3Action.isInstanceOf[OpenStore] shouldBe true
  }

  "NPC's" should "have random sprites" in {
    val spriteEvent1 = npcEvent1State.sprite.get
    val spriteEvent2 = npcEvent2State.sprite.get
    val spriteEvent3 = npcEvent3State.sprite.get

  /** Testing the 3 fields of SpriteSpec (direction, spriteIndex & step) except for name to check wheter they are randomly assigned*/
    assert(spriteEvent1.dir!=spriteEvent2.dir || spriteEvent1.dir!=spriteEvent3.dir || spriteEvent2.dir!=spriteEvent3.dir)
    assert(spriteEvent1.spriteIndex!=spriteEvent2.spriteIndex || spriteEvent1.spriteIndex!=spriteEvent3.spriteIndex || spriteEvent2.spriteIndex!=spriteEvent3.spriteIndex)
    assert(spriteEvent1.step!=spriteEvent2.step || spriteEvent1.step!=spriteEvent3.step || spriteEvent2.step!=spriteEvent3.step)
  }

  "NPC'S" should "have random animations: either move randomly or don't move" in {
    val animEvent1 = npcEvent1State.animationType
    val animEvent2 = npcEvent2State.animationType
    val animEvent3 = npcEvent3State.animationType
    val randomMovement = AnimationType.RANDOM_MOVEMENT.id
    val noMovement = AnimationType.NONE.id

    (animEvent1==noMovement || animEvent1==randomMovement) shouldBe true
    (animEvent2==noMovement || animEvent2==randomMovement) shouldBe true
    (animEvent3==noMovement || animEvent3==randomMovement) shouldBe true
  }

  "Actions" should "be assigned randomly" in {
    val actionEvent1 = npcEvent1State.cmds(0)
    val actionEvent2 = npcEvent2State.cmds(0)
    val actionEvent3 = npcEvent3State.cmds(0)

    /** NPC actions should be random*/
    assert(actionEvent1!=actionEvent2 || actionEvent1!=actionEvent3 || actionEvent2!=actionEvent3)

  }


}
