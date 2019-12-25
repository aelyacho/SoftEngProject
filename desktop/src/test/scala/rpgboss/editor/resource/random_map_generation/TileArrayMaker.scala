package rpgboss.editor.resource.random_map_generation

trait TileArrayMaker {
  /** make2dTileArray:  makes a 2d array of 'tiles' (dummy map)
   * @param w  desired width
   * @param h   desired height
   * @return    A 2d array width dimensions (w*3, h) containing Bytes(0)
   */
  def make2dTileArray(w: Int, h: Int) = {
    val rowArray = Array.tabulate(w*3)((n) => 0.toByte)
    val array = Array.fill(h)(rowArray.clone())
    array
  }
}
