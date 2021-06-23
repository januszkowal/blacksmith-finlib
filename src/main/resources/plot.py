import matplotlib.pyplot as plt
import csv, sys, getopt

#plot.py -k -f data.csv -c AkimaBlacksmith,AkimaApacheCommons -l 0 -h 100 -t "Curves comparison"
        
print('ARGV      :', sys.argv[1:])
opts, arguments = getopt.getopt(sys.argv[1:], 'f:c:t:l:h:k', ["file=","curves=","knots","title","low=","high="])
print('OPTIONS   :', opts)
print('ARGUMENTS :', arguments)

filePath = ''
displayKnots = False
displayCurves = []
displayTitle = 'Curves'
rangeLow = 0
rangeHigh = 0

for (opt, value) in opts:
    print(opt + '#' + value)
    if opt in ('-k','--knots'):
        displayKnots = True
    elif opt in ('-c','--curves'):
        displayCurves = list(value.split(","))
    elif opt in ('-l','--low'):
        rangeLow = int(value)
    elif opt in ('-h','--high'):
        rangeHigh = int(value)
    elif opt in ('-t','--title'):
        displayTitle = value
    elif opt in ('-f','--file'):
        filePath = value

#rowIndex = -1
x = []
yAkimaBlacksmith = []
yAkimaApacheCommons = []
yLinearBlacksmith = []
yLinearApacheCommons = []
yDoubleQuadraticBlacksmith = []
knotsx = []
knotsy = []

def readFile(filePath):
    with open(filePath, 'r') as file : 
        reader = csv.DictReader(file)
        for row in reader:
            xval = int(row['x'])
            x.append(xval)
            yAkimaBlacksmith.append(float(row['funAkimaBlacksmith']))
            yAkimaApacheCommons.append(float(row['funAkimaApacheCommons']))
            yLinearBlacksmith.append(float(row['funLinearBlacksmith']))
            yLinearApacheCommons.append(float(row['funLinearApacheCommons']))
            yDoubleQuadraticBlacksmith.append(float(row['funDoubleQuadraticBlacksmith']))
            #print(row)
            knot = row['knot']
            if (knot):
                knotval = float(row['knot'])
                knotsx.append(xval)
                knotsy.append(knotval)

readFile(filePath)

if rangeHigh ==0:
    rangeHigh = len(x) - 1

print('KNOTS      :', displayKnots)
print('CURVES     :', displayCurves)
print('RANGE-LOW  :', rangeLow)
print('RANGE-HIGH :', rangeHigh)
print('TITLE      :', displayTitle)  

 
plt.xlim([rangeLow, rangeHigh])
# plotting the functions
if displayKnots:
    plt.plot(knotsx, knotsy, 'ro', label='Knots') 
if 'AkimaBlacksmith' in displayCurves:
    plt.plot(x, yAkimaBlacksmith, color='green', label='Akima - Blacksmith')
if 'AkimaApacheCommons' in displayCurves:
    plt.plot(x, yAkimaApacheCommons, color='blue', label='Akima - Apache Commons')
if 'LinearBlacksmith' in displayCurves:
    plt.plot(x, yLinearBlacksmith, color='violet', label='Linear - Blacksmith')
if 'LinearApacheCommons' in displayCurves:
    plt.plot(x, yLinearApacheCommons, color='orange', label='Linear - Apache Commons')
if 'DoubleQuadraticBlacksmith' in displayCurves:
    plt.plot(x, yDoubleQuadraticBlacksmith, color='olive', label='Double Quadratic - Blacksmith')
    
# naming the x axis
plt.xlabel('x')
# naming the y axis
plt.ylabel('y')
  
# giving a title to my graph
plt.title(displayTitle)
plt.legend()
# function to show the plot
plt.show()
