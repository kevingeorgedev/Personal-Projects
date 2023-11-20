from imports import *
from create_graphs import *

#Clean the data by applying the create_graphs.team_names function
def clean_data(data : pd.DataFrame, teams : pd.DataFrame):
    abb = list(teams['Abbreviation'])
    data['NAME'] = data['NAME'].apply(team_names, abb=abb, data=data, teams=teams)
    return data

#Reads in statistics and team data CSVs
data = pd.read_csv("data/Running Back Week 8.csv").drop(columns=['RK'])
teams = pd.read_csv('data/teams.csv').drop(columns=['ID'])

for col in teams.columns:
    data[col] = ''
data = clean_data(data,teams)

#Start GUI
app = ctk.CTk()
app.minsize(500,500)
app.geometry("1000x700")
app.title("Fantasy Visuals")
selected_pos = tk.StringVar(value="Select Position")
positions = ["All Positions",
             "RB",
             "WR",
             "QB",
             "TE"]
positions = ["All Positions"] + list(data['POS'].value_counts().index)
selected_stat = tk.StringVar(value="Select 1st Statistic")
selected_stat2 = tk.StringVar(value="Select 2nd Statistic")

# Set statistics
stats = ["Attempts",
         "Total Yards",
         "Yards/Games",
         "Fumbles",
         "Longest Attempt",
         "Touchdowns"]
stats2 = ["ATT",
          "YDS",
          "YDS/G",
          "FUM",
          "LNG",
          "TD"]

selected_graph = tk.StringVar(value="Select Graph Type")
graphs = ["Scatter",
          "Normal Distribution"]
selected_player = tk.StringVar(value="Select Player")
players = []

# Sets the player dropdown
def set_players(choice):
    if choice == "All Positions":
        p_data : pd.Series = data['NAME']
    else:
        p_data : pd.Series = data.loc[data.POS == choice, 'NAME']
    players = list(p_data)
    players.sort()
    selected_player = tk.StringVar(value="Select Player")
    players_pos.configure(values=players, variable=selected_player)

drop_pos = ctk.CTkComboBox(app, 
                           values=positions, 
                           variable=selected_pos,
                           justify='center',
                           state='readonly',
                           hover=False,
                           command=set_players)
drop_pos.place(relx=0.01,rely=0.20, relwidth=0.2, relheight=0.05)

stats_pos = ctk.CTkComboBox(app,
                            values=stats,
                            justify='center',
                            state='readonly',
                            variable=selected_stat)
stats_pos.place(relx=0.01,rely=0.32, relwidth=0.2, relheight=0.05)

stats2_pos = ctk.CTkComboBox(app,
                             values=stats,
                             justify='center',
                             state='readonly',
                             variable=selected_stat2)
stats2_pos.place(relx=0.01,rely=0.44, relwidth=0.2, relheight=0.05)

graphs_pos = ctk.CTkComboBox(app,
                             values=graphs,
                             justify='center',
                             state='readonly',
                             variable=selected_graph)
graphs_pos.place(relx=0.01,rely=0.56, relwidth=0.2, relheight=0.05)

# On player selection
def player_select(choice):
    selected_player.set(choice)
    players_pos.configure(variable=selected_player)

players_pos = ctk.CTkComboBox(app,
                              values=players,
                              justify='center',
                              state='readonly',
                              variable=selected_player,
                              command=player_select)
players_pos.place(relx=0.01,rely=0.68, relwidth=0.2, relheight=0.05)

# Called when you select to generate graph
def make_graph_button_press():
    plt.close()
    graph_type = selected_graph.get()
    # Creates scatter plt
    if graph_type.lower() == "scatter":
        f, ax = scatter(data=data, 
                        pos=selected_pos.get(), 
                        stat1=stats2[stats.index(selected_stat.get())],
                        stat2=stats2[stats.index(selected_stat2.get())], 
                        name=selected_player.get())
        a = f.add_subplot(ax)
        a.plot()
        canvas = FigureCanvasTkAgg(f, app)
        canvas.get_tk_widget().place(relx=0.25, rely=0.1, relwidth=0.7, relheight=0.8)
        canvas.draw()
    # Creates normal distribution
    elif graph_type.lower() == "normal distribution":
        f, ax = bell_curve(data=data,
                        pos=selected_pos.get(), 
                        stat1=stats2[stats.index(selected_stat.get())],
                        name=selected_player.get())
        a = f.add_subplot(ax)
        a.plot()
        canvas = FigureCanvasTkAgg(f, app)
        canvas.get_tk_widget().place(relx=0.25, rely=0.1, relwidth=0.7, relheight=0.8)
        canvas.draw()
    return

make_graph_button = ctk.CTkButton(app,
                                  text="Make Graph",
                                  command=make_graph_button_press)
make_graph_button.place(relx=0.01,rely=0.80, relwidth=0.2, relheight=0.05)

ctk.set_appearance_mode("System")
ctk.set_default_color_theme("blue")

title = ctk.CTkLabel(app, text="Enter Data Style")
title.pack(padx=10, pady=10)

app.mainloop()