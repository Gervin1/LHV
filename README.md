# Investment Profit Calculation - LHV Assignment
This project simulates stock transactions and calculates the associated profit or loss from those transactions, including realized stock gains/losses, dividends, and unrealized gains/losses. The application generates random stock transactions within a given time range and calculates key financial metrics like total profit, stock-based profit, dividend profit, and unrealized gains.

## Features
Transaction Generation: Generates stock transactions (buy/sell) following a normal distribution for stock prices, with fees that scale with the order cost.

Profit Calculation finds:
* Total Profit/Loss
* Profit/Loss from stock transactions (realized gains)
* Profit/Loss from dividends
* Unrealized gains/losses based on the last transaction price

Output to JSON: Generates a list of transactions and writes them to a JSON file if specified.
Flexible Simulations: Users can generate transactions for simulation or calculate profit based on existing transaction data.

## Usage
You can run the program to either:
### 1. Generate Transactions and write them to a JSON file
To generate 1000 transactions and write them to a JSON file, run the program with an argument "generate"

### 2. Calculate Profit from JSON File
To calculate profit/loss based on transactions from a JSON file, run the program with a filename where to read from as an argument (for example transactions.json)
This command reads the transactions from the specified JSON file, calculates the profit/loss, and prints the results.

### 3. Generate transactions and calculate profit
To generate 1000 transactions and find the profit on them, run the program without arguments. 


## Example output:

Total Profit/Loss: $1500.00

Profit/Loss from Stock Gain/Fall: $1000.00

Profit/Loss from Dividends: $500.00

Unrealized Gains/Losses: $200.00
