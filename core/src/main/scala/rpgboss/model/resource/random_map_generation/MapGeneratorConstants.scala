package rpgboss.model.resource.random_map_generation

object MapGeneratorConstants {

  /** randomizer is used to generate a random number [randomizer.nextInt(n)]
   */
  var randomizer = new scala.util.Random(0)

  /**  The ratios are used to make sure the containers have a fairly square shape
   * H_RATIO: Smallest accepted height/width ratio
   * V_RATIO: Smallest accepted width/height ratio
   */
  val H_RATIO = 0.45
  val V_RATIO = 0.45

  /** MIN_CONT_DIMENSION is the minimum dimension a container can have (width>MIN_CONT_DIMENSION && height>MIN_CONT_DIMENSION)
   */
  val MIN_CONT_DIMENSION = 3
}
