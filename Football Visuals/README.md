# Fantasy Football Visuals 

## Project Description
The purpose of this project was to benefit me in my fantasy football leagues. I can use this data to add visuals to the statistics of different players. This is useful because you can see how far a player lies from the mean of a position. For example, when a running back is getting a lot of attempts at running the ball but aren't getting many yards it would show in a yards per attempts graph. The data used was pulled from ESPN by copy and pasting their rushing stats page into a spreadsheet and exporting as a CSV file.

**File Tree**:
```
Folder PATH listing
C:.
│   create_graphs.py
│   imports.py
│   main.py
│   README.md
│   
├───data
│       Running Back Week 3.csv
│       Running Back Week 4.csv
│       Running Back Week 8.csv
│       teams.csv
│       
└───__pycache__
        create_graphs.cpython-310.pyc
        imports.cpython-310.pyc
```

### Tkinter
In my project I used a GUI toolkit called Tkinter. Tkinter is a widely used GUI toolkit used by Python developers.

## Graph Types
### **The Scatter Plot**
The scatter plot can be used to display all players with an independent and dependent feature. Also, the scatter plot will display the selected player with another color than the rest and the residual it takes. A positive residual will mean the player is overproducing compared to the mean. A negative residual will mean the player is underproducing. Production is a concept in fantasy that is valuable when choosing players. Depending on the independent variable set a negative residual could be good. Players that overproduce are generally the ones that have big games and even bigger numbers.

**Calculating the residual**
```python
val = data.loc[data['NAME'] == name][stat1].values[0] # x-value of the player on graph
v = val*slope + intercept # Calculate the y value of the mean based on player x-value
val2 = data.loc[data['NAME'] == name][stat2].values[0] # Get actual y-value of the player
residual = round(val2 - v, 2) # Calculate the difference
```
### **The Gaussian Distribution**
The normal distribution plot can be used to see where a player lies over the normal distribution of all players based on a specific statistic. Important statistics in this graph may include yards per game, total yards, or touchdowns. The graph will display an intersecting line of the graph where the selected player fits and the percentile at which they are in. Knowing percentile you can determine if your player is above or below average for the selected statistic.

**Calculating the percentile**
```python
num = s[name] # Gets the value of the player
z = (num - mean)/stdev # Calculate the Z-Score
p = round(int(norm.cdf(z)*100), 2) # Calculate the percentile
```

## File Descriptions
### **create_graphs.py**
This file is in charge of generating the graphs listed above and cleaning the data at the beginning of running the code.
```python
# Creates Gaussian distribution
def bell_curve(data : pd.DataFrame, pos : str, stat1 : str, name : str):
    ...

# Creates scatter plot
def scatter(data : pd.DataFrame, pos : str, stat1 : str, stat2 : str, name : str):
    ...

# Cleans the data by adding team names
def team_names(x, abb : list, data : pd.DataFrame, teams : pd.DataFrame):
    ...
```

### **imports.py**
This file handles the imports each python file uses.

### **main.py**
This file is in charge of creating and running the GUI, calling on create_graph functions for generation, handling the GUI, and initializing arrays.

### **/data/ folder**
Folder that holds the football week data. In order to add to this data you must visit [ESPN's NFL Player Rushing Stats 2023](https://www.espn.com/nfl/stats/player/_/stat/rushing/table/rushing/sort/rushingYards/dir/desc) and copy and paste the entire table into a spreadsheet. Organize the data on the spreadsheet because it will seperate and you will have to drag a chunk of it to the top of the spreadsheet. Finally, export as csv and drop into data folder renaming it accordingly.

&copy; Copyright 2023, Kevin George, All rights reserved.