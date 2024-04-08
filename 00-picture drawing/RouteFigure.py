import pylab as pl


def readData(dataFile):
    file = open(dataFile)
    xCoord = []
    yCoord = []
    index = 0
    for line in file:
        index += 1
        if index < 4:
            continue
        line = line.strip()
        line_list = line.split("\t")
        xCoord.append(line_list[1])
        yCoord.append(line_list[2])
    file.close()
    return [xCoord, yCoord]


def readRoute(routeFile):
    file = open(routeFile)
    index = 0
    routes = [];
    for line in file:
        index += 1
        if index < 3:
            continue
        line = line.strip().split("\t")
        subroute = []
        for item in line:
            subroute.append(item)
        routes.append(subroute)
    file.close()
    return routes


def plotRoute(xCoord, yCoord, routes):
    # plot
    for index in range(len(xCoord)):
        pl.plot(xCoord[index], yCoord[index], "go")
        pl.text(xCoord[index], yCoord[index], str(index))
    pl.plot(xCoord[0], yCoord[0], "rs")
    pl.text(xCoord[0], yCoord[0], "0")
    for subRoute in routes:
        x = []
        y = []
        for node in subRoute:
            x.append(xCoord[int(node)])
            y.append(yCoord[int(node)])
        pl.plot(x, y)

    pl.show()


if __name__ == "__main__":
    dataFile = "./data/R101.txt"
    routeFile = "./result/R101result.txt"
    [xCoord, yCoord] = readData(dataFile)
    routes = readRoute(routeFile)
    plotRoute(xCoord, yCoord, routes)
    print("aaa")
