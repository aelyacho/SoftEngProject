package rpgboss.editor

import java.io.File

import org.sikuli.script._

/**
 * Suite of functional tests using the SikuliX library,
 * which can programmatically interact with RPGBoss's user interface
 */
object GuiTests {

  val s = new Screen

  def testAll(): Unit = {
    loadProject()
  }

  /**
   * Loads a project named "rpgboss-test" (assumes this project exists!)
   */
  def loadProject(): Unit = {
    // Is the splash screen open, or is the full editor open?
    if (s.exists("/Users/soft/Documents/Git/rpgboss/desktop/src/test/resources/images/LoadProject.png") != null) {
      s.click("/Users/soft/Documents/Git/rpgboss/desktop/src/test/resources/images/LoadProject.png")
    } else {
      s.click("/Users/soft/Documents/Git/rpgboss/desktop/src/test/resources/images/Project.png")
      s.`type`(Key.DOWN)
      s.`type`(Key.DOWN)
      s.`type`(Key.ENTER) // Move down to the "Load project" menu entry .. doesn't work via click() for some reason..
    }
    s.doubleClick("/Users/soft/Documents/Git/rpgboss/desktop/src/test/resources/images/ProjectName.png")

    assert(s.exists("/Users/soft/Documents/Git/rpgboss/desktop/src/test/resources/images/MapIsLoaded.png") != null)
  }

  def exitApplication(): Unit = {
    if (System.getProperty("os.name") == "Mac OS X") {
      s.`type`("q", Key.CMD)
    } else {
      s.`type`(Key.F4, Key.CTRL)
    }
  }

  /**
   * Main method to start the functional test suite
   *
   * Functional tests are specified using the SikuliX API:
   * http://sikulix.com/quickstart/#qs8
   * http://sikulix-2014.readthedocs.io/en/latest/faq/030-java-dev.html#how-to-use-sikulix-api-in-your-java-programs-or-java-aware-scripting
   *
   * @param args
   */
  def main(args: Array[String]) {
    println("Current working directory" + new File(".").getAbsolutePath);
    println(new File("desktop/src/test/resources/images/LoadProject.png").exists());

    val startTime = System.currentTimeMillis()
    RpgDesktop.main(new Array[String](0))
    testAll()
    println("Time taken: " + ((System.currentTimeMillis() - startTime)/1000) + "s" )
    exitApplication()
  }

}