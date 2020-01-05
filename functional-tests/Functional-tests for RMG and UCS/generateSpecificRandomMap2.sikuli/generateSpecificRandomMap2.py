# This test tests the scenario where a random map is generated
# with (seed = 0, the default tiles, iter = 4, dimensions 50x50)

#Note: The test only works when a project exists called 'project1'

# if the project is not open, open it
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

# Toggle random 

wait("1575929348048.png")
click("1575929348048.png")

# Modify map dimensions

doubleClick("1578255621642.png")
type("50")
doubleClick("1578255641778.png")
type("50")

# Modify the amount of iterations

doubleClick("1578255658509.png")
type("4")

# Toggle seed, (left on default value 0)

wait("1578234565829.png")
click("1578234565829.png")
click("1575929374662.png")

# Check whether the correct map is generated

wait("1578234654886.png")
click("1578234654886.png")

wait("1578255746651.png")









