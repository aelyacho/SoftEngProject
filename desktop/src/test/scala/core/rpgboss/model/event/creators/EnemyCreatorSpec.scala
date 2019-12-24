package core.rpgboss.model.event.creators

import core.UnitSpec
import rpgboss.model.event.{AnimationType, EventCmd, StartBattle}
import rpgboss.model.event.creators.EnemyCreator

/**
 * test creator, random sprites, animations, actions
 */

class EnemyCreatorSpec extends UnitSpec with GeneratedMap {
  val enemyCreator = new EnemyCreator(0)
  val enemyEvent1State = enemyCreator.createEvent()(0).states(0)
  val enemyEvent2State = enemyCreator.createEvent()(0).states(0)
  val enemyEvent3State = enemyCreator.createEvent()(0).states(0)

  "EnemyCreator" should "create events with the behaviour of an enemy" in {

    val event1Action = enemyEvent1State.cmds(0)
    val event2Action = enemyEvent2State.cmds(0)
    val event3Action = enemyEvent3State.cmds(0)

    /** an enemy should start a battle  */
    event1Action.isInstanceOf[StartBattle] && event2Action.isInstanceOf[StartBattle] && event3Action.isInstanceOf[StartBattle] shouldBe true
  }

  "Enemies" should "have random sprites" in {
    val spriteEvent1 = enemyEvent1State.sprite.get
    val spriteEvent2 = enemyEvent2State.sprite.get
    val spriteEvent3 = enemyEvent3State.sprite.get

    /** Testing the 3 fields of SpriteSpec (direction, spriteIndex & step) except for name
     * to check wheter they are different since they are randomly assigned
     */
    spriteEvent1.equals(spriteEvent2) shouldBe false
    spriteEvent1.equals(spriteEvent3) shouldBe false
    spriteEvent2.equals(spriteEvent3) shouldBe false
  }

  "Enemies" should "have random animations: either move randomly or don't move" in {
    val animEvent1 = enemyEvent1State.animationType
    val animEvent2 = enemyEvent2State.animationType
    val animEvent3 = enemyEvent3State.animationType
    val randomMovement = AnimationType.RANDOM_MOVEMENT.id
    val followPLayer = AnimationType.FOLLOW_PLAYER.id

    (animEvent1==followPLayer || animEvent1==randomMovement) shouldBe true
    (animEvent2==followPLayer || animEvent2==randomMovement) shouldBe true
    (animEvent3==followPLayer || animEvent3==randomMovement) shouldBe true
  }

  "Actions" should "be assigned randomly" in {
    val actionEvent1Id = enemyEvent1State.cmds(0).asInstanceOf[StartBattle]
    val actionEvent2Id = enemyEvent2State.cmds(0).asInstanceOf[StartBattle]
    val actionEvent3Id = enemyEvent3State.cmds(0).asInstanceOf[StartBattle]

    /** Enemies should encounter a random enemy (goblin, basilisk, dragon etc...)*/

    assert(actionEvent1Id!=actionEvent2Id || actionEvent1Id!=actionEvent3Id || actionEvent2Id!=actionEvent3Id)

  }

}
