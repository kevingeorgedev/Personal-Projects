from imports import *

def hex_to_RGB(hex_str):
    """ #FFFFFF -> [255,255,255]"""
    #Pass 16 to the integer function for change of base
    return [int(hex_str[i:i+2], 16) for i in range(1,6,2)]

def get_color_gradient(c1, c2, n):
    """
    Given two hex colors, returns a color gradient
    with n colors.
    """
    assert n > 1
    c1_rgb = np.array(hex_to_RGB(c1))/255
    c2_rgb = np.array(hex_to_RGB(c2))/255
    mix_pcts = [x/(n-1) for x in range(n)]
    rgb_colors = [((1-mix)*c1_rgb + (mix*c2_rgb)) for mix in mix_pcts]
    return ["#" + "".join([format(int(round(val*255)), "02x") for val in item]) for item in rgb_colors]

def bell_curve(data : pd.DataFrame, pos : str, stat1 : str, name : str):

    # Get list of names for specified or unspecified postition.
    if pos == '':
        s = data[stat1]
        s.index = [list(data.loc[data['POS'] == pos]['NAME'])]
    else:
        s = data.loc[data['POS'] == pos][stat1]
        s.index = [list(data.loc[data['POS'] == pos]['NAME'])]

    # Get X and Y values
    s = s.sort_values(axis=0, ascending=False) # Sort names
    vals = s.to_list() # Get vals of series as list
    length = len(vals) # Number of names in list to calculate the mean
    mean = sum(vals)/length # Calculate mean
    stdev = s.std() # Calculate the standard deviation
    x = np.linspace(0, mean+4*stdev, 200) # create numpy array for x values of graph
    y = norm(loc=mean, scale=stdev).pdf(x) # create probability density funtion
    
    #Plotting
    f, ax = plt.subplots(figsize=(4,4))
    ax.plot(x,y,c='blue', label=f"mean={mean}, std={stdev}")
    xmin, xmax, ymin, ymax = ax.axis()
    plt.xlabel(stat1)
    #name = input("Enter Name: ")
    plt.fill_between(x,y,color='lightgray')
    if name != "":
        ax.axvline(s[name],ymin=0, ls='--', c='black')
        num = s[name] # Gets the value of the player
        z = (num - mean)/stdev # Calculate the Z-Score
        p = round(int(norm.cdf(z)*100), 2) # Calculate the percentile
        ax.text(s[name], ymax, f"{name} ({num} {stat1}) Percentile: {p}{['th','st','nd','rd','th','th','th','th','th','th'][p%10]}", ha='center')
        
        x_fill = np.linspace(0, num, 200)
        y_fill = norm.pdf(x_fill, loc=mean, scale=stdev)
        
        ax.fill_between(x_fill,y_fill,color='r')
    return f,ax

def scatter(data : pd.DataFrame, pos : str, stat1 : str, stat2 : str, name : str):

    # Checks if position is specified. If so, set colors of points accordindly.
    if pos != 'All Positions':
        data = data[data.POS == pos]
        colors = [('r' if n == name else 'b') for n in data.NAME]
    else:
        colors = [('r' if flag == 'RB' else ('g' if flag == 'QB' else ('b' if flag == 'WR' else 'purple'))) for flag in data.POS]
    
    # Plotting
    f, ax = plt.subplots(figsize=(4,4))
    x = np.array(data[stat1]) # x-values to plot
    y = np.array(data[stat2]) # y-values to plot
    ax.scatter(x=x, y=y, c=colors)
    slope, intercept, r_value, p_value, std_err = stats.linregress(x, y) # create linear regression line
    ax.plot(x, slope*x + intercept, color='black', ls='solid', label='Best-Fit')
    val = data.loc[data['NAME'] == name][stat1].values[0] # x-value of the player on graph
    v = val*slope + intercept # Calculate the y value of the mean based on player x-value
    val2 = data.loc[data['NAME'] == name][stat2].values[0] # Get actual y-value of the player
    residual = round(val2 - v, 2) # Calculate the difference
    xmin, xmax, ymin, ymax = ax.axis()
    ax.set_xlabel(stat1)
    ax.set_ylabel(stat2)
    ax.vlines(x=val, ymin=min(v, val2), ymax=max(v,val2), color='lightgray', ls='dashed')
    ax.set_title(f"{name} ({val2} {stat2}) Residual: {residual} {stat2}")
    return f,ax

#Sets the team names
def team_names(x, abb : list, data : pd.DataFrame, teams : pd.DataFrame):
    sub = next(substring for substring in abb if substring in x)
    s = teams.loc[teams.Abbreviation == sub]
    data.loc[data['NAME'] == x, 'Abbreviation'] = sub
    data.loc[data['NAME'] == x, 'Name'] = s.iloc[0, 0]
    data.loc[data['NAME'] == x, 'Conference'] = s.iloc[0, 2]
    data.loc[data['NAME'] == x, 'Division'] = s.iloc[0,3]
    return x.replace(sub, '')