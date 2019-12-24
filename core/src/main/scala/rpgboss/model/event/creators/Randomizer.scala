package rpgboss.model.event.creators

object Randomizer {
  def getRandomVal(x: Int): Int = scala.util.Random.nextInt(x)
}
