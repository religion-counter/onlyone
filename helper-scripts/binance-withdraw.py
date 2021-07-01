# Script that withdraws BNB on BSC from binance to accounts specified in ACCOUNTS.json
import json
from binance.spot import Spot as Client
import time
import random

with open('ACCOUNTS.json', 'r') as accounts:
    accountsRaw = ''.join(accounts.readlines())

accounts = json.loads(accountsRaw)

with open('BINANCE_API_KEY.txt', 'r') as binapikey:
    binapiRaw = binapikey.readlines()

# print(binapiRaw[1][:-1]) # apikey
# print(binapiRaw[4][:-1]) # scrt

client = Client(base_url='https://api.binance.com',
               key=binapiRaw[1][:-1],
               secret=binapiRaw[4][:-1])

# print(client.account())

for i in range(0, 1000):
    accountToWithdraw = accounts[i]['address']
    withdrawValue = random.uniform(0.033, 0.066)
    print(accountToWithdraw, i, withdrawValue)
    response = client.withdraw(coin="BNB",
                               amount=withdrawValue,
                               address=accountToWithdraw,
                               network='BSC')
    sleepSeconds = random.randint(15, 45)
    print("Sleeping for {} seconds.".format(sleepSeconds))
    time.sleep(sleepSeconds)
