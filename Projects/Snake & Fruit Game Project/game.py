import pygame,pygame.locals,random
import pygame
import sys
pygame.init()
window=pygame.display.set_mode((460,460))
def showlines():
    for i in range(20,460,20):
        pygame.draw.line(window,(0,255,0),(i,0),(i,460))
    for i in range(20,460,20):
        pygame.draw.line(window,(0,255,0),(0,i),(460,i))
showlines()

fruitx=random.randint(5,23)
fruity=random.randint(5,23)
snakedots=[]
snakedots.append({"x":10,"y":10})
snakedots.append({"x":11,"y":10})
snakedots.append({"x":12,"y":10})

def drawsnake():
    global fruitx,fruity
    dot = pygame.Rect(fruitx * 20, fruity * 20, 20, 20)
    pygame.draw.rect(window, (0, 0, 255), dot)
    for i in range(0,len(snakedots)):
        snake= pygame.Rect(snakedots[i]["x"]*20,snakedots[i]["y"]*20,20,20)
        pygame.draw.rect(window,(255,0,0),snake)
drawsnake()

def movesnake():
    window.fill((0,0,0))
    #showlines()
    drawsnake()
direction=0
fpsclock=pygame.time.Clock()
while True:
    for event in pygame.event.get():
        if event.type == 12:
            pygame.quit()
            sys.exit()
        elif event.type==2 and event.key==275:
            print("right key pressed")
            direction=1
        elif event.type == 2 and event.key == 276:
            print("left key pressed")
            direction= 0
        elif event.type == 2 and event.key == 273:
            print("up key pressed")
            direction = 2
        elif event.type == 2 and event.key == 274:
            print("down key pressed")
            direction = 3
    if direction==0:
        snakedots.insert(0,{"x":snakedots[0]['x']-1,"y":snakedots[0]['y']})
    elif direction==1:
        snakedots.insert(0,{"x":snakedots[0]['x']+1,"y":snakedots[0]['y']})
    elif direction==2:
        snakedots.insert(0,{"x":snakedots[0]['x'],"y":snakedots[0]['y']-1})
    elif direction==3:
        snakedots.insert(0,{"x":snakedots[0]['x'],"y":snakedots[0]['y']+1})
    if snakedots[0]["x"]==fruitx and snakedots[0]["y"]==fruity:
        fruitx=random.randint(5,20)
        fruity=random.randint(5,20)
        dot = pygame.Rect(fruitx * 20, fruity * 20, 20, 20)
        pygame.draw.rect(window, (0, 0, 255), dot)
    else:
        del snakedots[-1]
    if snakedots[0]["x"]<0 or snakedots[0]["x"]>23 or snakedots[0]["y"]<0 or snakedots[0]["y"]>23:
        break



    movesnake()
    pygame.display.update()
    fpsclock.tick(10)
