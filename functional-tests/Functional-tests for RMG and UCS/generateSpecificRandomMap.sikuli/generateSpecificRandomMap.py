# This test tests the scenario where a random map is generated
# with (seed = 5, some different tiles, default iter, and the default map dimensions 80x60)

#Note: The test only works when a project exists called 'project1'

# if project is not open yet, open it
if not exists("1575929284580.png"):
    
    if exists("1575930613722.png"):
        click("1575930613722.png")
        wait("1575929255152.png")
        doubleClick("1575929255152.png")
    elif exists("1575930258070.png"):
        click("1575930258070.png")
        type(Key.DOWN)
        type(Key.DOWN)
        type(Key.ENTER)
        wait("1575929255152.png")
        doubleClick("1575929255152.png")
    else:
        popError("Cannot find RpgBoss window")

# Once the project is open, create a new map
wait("1575929284580.png")
rightClick("1575929284580.png")
type(Key.DOWN)
type(Key.ENTER)

# Check the random toggle
wait("1575929348048.png")
click("1575929348048.png")

# Select floor tile

click("1578235392346.png")
click("1578237315053.png")
click("1578236810511.png")
click("1578237367355.png")

# Select wall tile
click("1578236425293.png")
click("1578237315053.png")
click("1578236861817.png")

click("1578237367355.png")

# Toggle the configurable seed

wait("1578234565829.png")
click("1578234565829.png")

# Set the seed to '5'
doubleClick("1578235817871.png")
type("5")

click("1575929374662.png")

# Check wether the correct map is generated

wait("1578234654886.png")
click("1578234654886.png")

wait("1578237247504.png")









