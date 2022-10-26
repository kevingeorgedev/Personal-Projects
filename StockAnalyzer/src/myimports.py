from __future__ import print_function

import os.path
from turtle import color

#Reddit bot API
import praw

#Python imports
import time as tme
from datetime import *
import atexit
import csv
import os
import json
import sys
import asyncio

#Yahoo Finance imports
import pandas as pd
import yfinance as yf
from yahoofinancials import YahooFinancials

#Google imports
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError
from google.oauth2 import service_account

#Color Text
import colorama
from colorama import Fore
from colorama import Style
colorama.init()
from sys import stdout

