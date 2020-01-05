# Functional test used to test the deployement of the events
if exists("1578234335227.png"): # look for map properties
    rightClick("1578234335227.png") 
    if exists("1578235447528.png"):
        click("1578247915637.png")  # open map properties
        if exists("1578248020604.png"): 
            click("1578234513973.png") # enable random map gen
            click("1578236969402.png")
            sleep(1)
            click("1578234667008.png")  # open event gen config
          # wait("1578234693789.png"#)
            if exists(Pattern("1578237397929.png").exact()):
                click(Pattern("1578239126667.png").exact().targetOffset(-381,-71))
                type("a",Key.CMD)
                type("5")
                click(Pattern("1578237536367.png").targetOffset(-2,1))
  
        else:
            popError("Cannot find event gen")

else:
    popError("Cannot find random map gen")
     