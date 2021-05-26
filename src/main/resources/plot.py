import matplotlib.pyplot as plt
import csv, sys, getopt

#plot.py -k -f data.csv -c AkimaBlackSmith,AkimaApacheCommons -l 0 -h 100 -t "Curves comparison"
        
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
yAkimaBlackSmith = []
yAkimaApacheCommons = []
yLinearBlackSmith = []
yLinearApacheCommons = []
knotsx = []
knotsy = []

def readFile(filePath):
    with open(filePath, 'r') as file : 
        reader = csv.DictReader(file)
        for row in reader:
            xval = int(row['x'])
            x.append(xval)
            yAkimaBlackSmith.append(float(row['funAkimaBlackSmith']))
            yAkimaApacheCommons.append(float(row['funAkimaApacheCommons']))
            yLinearBlackSmith.append(float(row['funLinearBlackSmith']))
            yLinearApacheCommons.append(float(row['funLinearApacheCommons']))
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
if 'AkimaBlackSmith' in displayCurves:
    plt.plot(x, yAkimaBlackSmith, color='green', label='Akima - Black Smith')
if 'AkimaApacheCommons' in displayCurves:
    plt.plot(x, yAkimaApacheCommons, color='blue', label='Akima - Apache Commons')
if 'LinearBlackSmith' in displayCurves:
    plt.plot(x, yLinearBlackSmith, color='orange', label='Linear - Black Smith')     
if 'LinearApacheCommons' in displayCurves:
    plt.plot(x, yLinearApacheCommons, color='orange', label='Linear - Apache Commons')  
    
# naming the x axis
plt.xlabel('x')
# naming the y axis
plt.ylabel('y')
  
# giving a title to my graph
plt.title(displayTitle)
plt.legend()
# function to show the plot
plt.show()
