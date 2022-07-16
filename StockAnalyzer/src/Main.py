from myimports import *

__author__ = "Kevin George"
__credits__ = "Kevin George"
__version__ = "1.0"
__maintainer__ = "Kevin George"
__email__ = "kevingeorgedev@gmail.com"
__status__ = "Development"

#open config file
with open("config.json") as json_data_file:
    data = json.load(json_data_file)

def printUpdate(message):
    stdout.write(Fore.WHITE + "[" + str(datetime.now().replace(microsecond = 0)) + "] " + message + "\n")

# If modifying these scopes, delete the file token.json.
SCOPES = ['https://www.googleapis.com/auth/spreadsheets']

try:    
    SERVICE_ACCOUNT_FILE = data["Google_Spreadsheet_Inputs"]["service_account_file"]
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid 'service_account_file")
    exit()

creds = None
creds = service_account.Credentials.from_service_account_file(
        SERVICE_ACCOUNT_FILE, scopes=SCOPES)

# The ID and range of a sample spreadsheet.
try:
    SPREADSHEET_ID = data["Google_Spreadsheet_Inputs"]["spreadsheet_id"]
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid 'spreadsheet_id'")
    exit()

service = build('sheets', 'v4', credentials=creds)
sheet = service.spreadsheets()

if(data["general_setup"]["updates"]["initialize_reddit_bot"]):
    printUpdate(Fore.GREEN + "Initializing Reddit bot")

#initialize the reddit bot
try:
    reddit = praw.Reddit(
        client_id= data["reddit_bot"]["client_id"],
        client_secret= data["reddit_bot"]["client_secret"],
        user_agent= data["reddit_bot"]["user_agent"],
    )
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid input for 'client_id', 'client_secret', and/or 'user_agent'")
    exit()

if(data["general_setup"]["updates"]["initializing_files"]):
    printUpdate(Fore.GREEN + "Initializing files")

#Get file names for each file
try:
    STOCKS_FILE = data["general_setup"]["stocks_file"]
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid 'stocks_file'")
    exit()

try:
    LAST_UPDATE_FILE = data["general_setup"]["last_update_file"]
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid 'last_update_file")
    exit()

stocks = [] #Formatted as: [["TSLA", 30], ["AAPL", 25], ["NVDA", 13]]

#Set path to files
try:
    path = data["general_setup"]["path_to_files"]
except Exception as e:
    printUpdate(Fore.RED + "ERROR: invalid 'path_to_files'")
    exit()

os.chdir(path)

#last time the stocks were updated
global last_update
last_update = datetime.now()
sys.tracebacklimit = 0

#End of program update last_update_file to new time
def write_to_update_time():
    with open(LAST_UPDATE_FILE, 'w') as f:
        f.write(last_update.isoformat().replace("T", " "))
    if(data["general_setup"]["updates"]["last_update_change"]):
        printUpdate(Fore.YELLOW + "\t'last_update_time' updated")

#End of program update stocks file with new mentions and stocks
def write_to_stocks():
    with open(STOCKS_FILE, 'w') as f:
        for stock in stocks:
            f.writelines(stock[0] + " " + str(stock[1]) + "\n")
        if(data["general_setup"]["updates"]["stocks_update"]):
            printUpdate(Fore.YELLOW + "\t'stocks'           updated")

#Read from LAST_UPDATE_FILE to refresh last_update time
with open(LAST_UPDATE_FILE, 'r') as file:
    if(data["general_setup"]["updates"]["reading_last_update"]):
        printUpdate(Fore.GREEN + "Reading 'last_update_time.txt' file")
    try:
        last_update = datetime.strptime(file.read(), "%Y-%m-%d %H:%M:%S.%f") - timedelta(days=data["reddit_bot"]["days_to_scan"])
    except Exception as e:
        printUpdate(Fore.RED + "ERROR: invalid 'days_to_scan' (must be a positive integer")
        exit()

#Read from STOCKS_FILE
with open(STOCKS_FILE, 'r') as file:
    if(data["general_setup"]["updates"]["reading_from_stocks"]):
        printUpdate(Fore.GREEN + "Reading from 'stocks.txt' file")
    for line in file:
        stripped_stock = line.strip().split()
        name = str(stripped_stock[0])
        mention = int(stripped_stock[1])
        stocks.append([name, mention])

#Returns the datetime of the submission
def get_date(submission):
    time = submission.created
    return datetime.fromtimestamp(time)

#Makes the body of the updated spreadsheet
def makeBody(update):
    tmp = []
    i = 0
    while i < len(stocks):
        try:
            #Only want to print top 10 stocks
            if i == 15: 
                return tmp
            arr = [None] * 9
            tickerTmp = yf.Ticker(stocks[i][0])
            info = tickerTmp.info
            arr[0] = stocks[i][0]
            arr[1] = stocks[i][1]            
            arr[2] = info["currentPrice"]
            arr[3] = info["dayLow"]
            arr[4] = info["dayHigh"]
            arr[5] = info["volume"]
            arr[6] = round(((arr[2] - info["previousClose"]) / info["previousClose"]), 4)
            arr[8] = "finance.yahoo.com/quote/" + arr[0]
            tmp.append(arr)
        except Exception as e:
            #Called when stocks can't be found using yahoo finance
            if(data["general_setup"]["updates"]["error_locating_stocks"]):
                printUpdate(Fore.RED + "Couldn't locate stock '" + stocks[i][0] + "' and has been removed.")
            if data["general_setup"]["updates"]["non_stocks_update"]:
                printUpdate(Fore.RED + "Adding new word to 'non_stocks'")
            del stocks[i]
            i -= 1
            if i == len(stocks) - 1:
                break
        i += 1
    return tmp

#Loop until program ends
while(1):
    with open('non_stocks.txt', 'r') as file:
        non_stocks = file.read()
    non_stocks2 = open('non_stocks.txt', 'a')
    try:
        if len(data["reddit_bot"]["subreddit"]) == 0:
            printUpdate(Fore.RED + "ERROR: Can't be 0 'subreddits'", Fore.RED)
            exit()
        cont = False
        #loop through all subreddits on user input
        for sreddit in data["reddit_bot"]["subreddit"]:
            try:
                subreddit = reddit.subreddit(sreddit)
            except Exception as e:
                printUpdate(Fore.RED + "ERROR: invalid 'subreddit'")
                exit()

            try:
                newposts = subreddit.new(limit=data["reddit_bot"]["amount_to_scan"])
            except Exception as e:
                printUpdate(Fore.RED + "ERROR: invalid 'amount_to_scan', must be integer")
                exit()
            for sub in newposts:

                #Break if post is before the last update
                if get_date(sub) < last_update: 
                    break

                #array of all words split by space in the title
                words = sub.title.split() 

                #Tickers that have already been added to the list to avoid adding repeats from one submission
                visited = []

                #iterate through all words in submission title
                for word in words:
                    #Only worry about words that contain a '$'
                    if "$" in word:
                        #Filter out characters that aren't [A-Z], [a-z] and [0-9]
                        name = ''.join(filter(str.isalnum, word)).strip()

                        #break if there is a character that isn't alphanumeric
                        if not name.isalpha():
                            break

                        #Convert ticker to all uppercase
                        name = name.upper()

                        #break if word is less than 3 or greater than 4 because tickers can be 3 or 4 in length
                        if len(name) > 5 : 
                            #or len(name) < 3
                            break
                        #If name hasn't been visited already
                        elif name not in visited:
                            
                            if name in non_stocks:
                                continue
                            #Loop through 'stocks'
                            for i in range(len(stocks)):
                                #If ticker exists in stocks we update mentions
                                if stocks[i][0] == name:
                                    stocks[i][1] += 1
                                    if(data["general_setup"]["updates"]["stock_mention_increments"]):
                                        printUpdate(Fore.YELLOW + "$" + name + " Incrementing mentions (+1) from subreddit: " + sreddit)
                                    break
                                #If ticker does not exist in stocks we add to the list
                                elif i == len(stocks) - 1:
                                    stocks.append([name, 1])
                                    if(data["general_setup"]["updates"]["stock_added_to_stocks"]):
                                        printUpdate(Fore.YELLOW + "$" + name + " Adding to stock list from subreddit: " + sreddit)
                            visited.append(str(name))
                #New mention or stock added means we want to update spreadsheet
                if not len(visited) == 0:
                    cont = True
        #Update with message when new mention or ticker is added
        if(cont):
            last_update = datetime.now()
            stocks.sort(key=lambda x:x[1])
            stocks.reverse()
            if(data["general_setup"]["updates"]["updating_spreadsheet"]):
                printUpdate(Fore.GREEN + "Updating spreadsheet...")
            request = service.spreadsheets().values().update(spreadsheetId=SPREADSHEET_ID, 
                                    range="stocks!B3", valueInputOption="USER_ENTERED", body={"values":makeBody(True)}).execute()
            if(data["general_setup"]["updates"]["spreadsheet_updated"]):
                printUpdate(Fore.GREEN + "Spreadsheet updated...")
            write_to_update_time()
            write_to_stocks()
        #Update without message to avoid clogging the logs
        else:
            request = service.spreadsheets().values().update(spreadsheetId=SPREADSHEET_ID, 
                                    range="stocks!B3", valueInputOption="USER_ENTERED", body={"values":makeBody(False)}).execute()

        #Sleep for 10 seconds
        tme.sleep(10)

    except HttpError as err:
        print(err)

#Exit handler
def exit_handler():
    printUpdate("Shutting down stock analyzer...")
    printUpdate("Most recently updated post: " + str(last_update))
    printUpdate("Updating files...")
    write_to_update_time()
    write_to_stocks()

atexit.register(exit_handler)