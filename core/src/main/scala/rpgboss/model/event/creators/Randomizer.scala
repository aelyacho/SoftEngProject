package rpgboss.model.event.creators

import rpgboss.model.resource.random_map_generation.MapGeneratorConstants


object Randomizer {
  def getRandomVal(x: Int): Int = MapGeneratorConstants.randomizer.nextInt(x)
}
