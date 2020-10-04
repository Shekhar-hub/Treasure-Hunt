import pygame,pygame.locals,random
import sys
import time
pygame.init()
white = (255, 255, 255)
green = (0, 255, 0)
blue = (0, 0, 128)

# assigning values to X and Y variable
X = 400
Y = 400

window=pygame.display.set_mode((460,660))
pygame.display.set_caption('Snake Game')
font = pygame.font.Font('freesansbold.ttf',30)
text = font.render('GeeksForGeeks', True, green, blue)
textRect = text.get_rect()

textRect.center = (X//2,Y//2)

def showlines():
    for i in range(20, 460, 20):
        pygame.draw.line(window, (0, 255, 0), (i,40), (i, 660))
    for i in range(40, 660, 20):
        pygame.draw.line(window, (0, 255, 0), (0, i), (460, i))

def drawsnake():
        global fruitx, fruity
        dot = pygame.Rect(fruitx * 20, fruity *20, 20, 20)
        pygame.draw.rect(window, (0, 0, 255), dot)
        for i in range(0, len(snakedots)):
            snake = pygame.Rect(snakedots[i]["x"] * 20, snakedots[i]["y"] * 20, 20, 20)
            pygame.draw.rect(window, (255, 0, 0), snake)

def movesnake():
        window.fill((0, 0, 0))
        showlines()
        drawsnake()



showlines()
fruitx = random.randint(5, 20)
fruity = random.randint(5, 30)
snakedots = []
snakedots.append({"x": 10, "y": 10})
snakedots.append({"x": 11, "y": 10})
snakedots.append({"x": 12, "y": 10})

drawsnake()

direction = 0
fpsclock = pygame.time.Clock()

while True:
    window.fill(white)

    window.blit(text,textRect)

    for event in pygame.event.get():
        if event.type == 12:
            pygame.quit()
            sys.exit()
        elif event.type == 2 and event.key == 275 and direction != 0:
            print("right key pressed")
            direction = 1
        elif event.type == 2 and event.key == 276 and direction != 1:
            print("left key pressed")
            direction = 0
        elif event.type == 2 and event.key == 273 and direction != 3:
            print("up key pressed")
            direction = 2
        elif event.type == 2 and event.key == 274 and direction != 2:
            print("down key pressed")
            direction = 3

    l1 = []
    l2 = []
    for i in range(0, len(snakedots)):
        l1.append(snakedots[i]["x"])
        l2.append(snakedots[i]["y"])
    if direction == 0:
        snakedots.insert(0, {"x": snakedots[0]['x'] - 1, "y": snakedots[0]['y']})
    elif direction == 1:
        snakedots.insert(0, {"x": snakedots[0]['x'] + 1, "y": snakedots[0]['y']})
    elif direction == 2:
        snakedots.insert(0, {"x": snakedots[0]['x'], "y": snakedots[0]['y'] - 1})
    elif direction == 3:
        snakedots.insert(0, {"x": snakedots[0]['x'], "y": snakedots[0]['y'] + 1})
    for i in range(0, len(l1)):
        if snakedots[0]["x"] == l1[i] and snakedots[0]["y"] == l2[i]:
            pygame.quit()
            sys.exit()

    if snakedots[0]["x"] == fruitx and snakedots[0]["y"] == fruity:
        fruitx = random.randint(5, 20)
        fruity = random.randint(5, 30)
        dot = pygame.Rect(fruitx * 20, fruity * 20, 20, 20)
        pygame.draw.rect(window, (0, 0, 255), dot)

    else:
        del snakedots[-1]
        if snakedots[0]["x"] < 0 or snakedots[0]["x"] > 23 or snakedots[0]["y"] < 0 or snakedots[0]["y"] > 33:
            break
    movesnake()
    pygame.display.update()
    fpsclock.tick(5)