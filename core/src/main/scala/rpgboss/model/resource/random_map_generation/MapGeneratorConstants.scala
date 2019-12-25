package rpgboss.model.resource.random_map_generation

object MapGeneratorConstants {

  /** setSeed is used to change the seed of the randomizer object
   * @param seed the seed(Int)
   */
  def setSeed(seed:Int)={
    randomizer = new scala.util.Random(seed)
  }

  def resetSeed={
    randomizer = new scala.util.Random
  }

  /** randomizer is used to generate a random number [randomizer.nextInt(n)]
   */
  var randomizer = new scala.util.Random

  /**  The ratios are used to make sure the containers have a fairly square shape
   * H_RATIO: Smallest accepted height/width ratio
   * V_RATIO: Smallest accepted width/height ratio
   */
  val H_RATIO = 0.45
  val V_RATIO = 0.45

  /** MIN_CONT_DIMENSION is the minimum dimension a container can have (width>MIN_CONT_DIMENSION && height>MIN_CONT_DIMENSION)
   */
  val MIN_CONT_DIMENSION = 3


  /** MAX_SPLIT_TRIES is the maximum amount of tries to randomly split a container, before an exception gets raised
   */
  val MAX_SPLIT_TRIES = 300

  /** MAX_ITER is the maximum amount of iterations that can be selected
   */
  val MAX_ITER = 10

  val MIN_SEED = -1000
  val MAX_SEED = 1000
}
