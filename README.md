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

Unrealized Gains/Losses (based on the latest transaction price): $200.00

## Advanced Level III task

### Task: 

Conduct an analysis of the portfolio analytics at LHV Pank. Provide insights into what is working well in the current analytics setup and identify any gaps or areas that could be improved. You might analyze how well the current system tracks performance metrics, identify missing data points, or suggest enhancements to existing tools.

### What's working well:

The "My Investments" section is minimalistic and straightforward, making it easy to read and understand the current portfolio.

### Areas for Improvement:

There is no historical chart showing how your portfolio has performed over time. Another great enhancement would be to include a feature that allows you to toggle and highlight specific actions - such as buy and sell transactions or significant stock events like earnings reports directly on the chart.

![image](https://github.com/user-attachments/assets/e0af74dc-a2b3-40c4-b5e0-79ad3ee17f8e)
