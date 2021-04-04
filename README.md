# DB Code Pairing Test
There is a scenario where thousands of trades are flowing into one store, assume any way of transmission of trades. We need to create a one trade store, , which stores the trade.

There are couples of validation, we need to provide in the above assignment

During transmission if the lower version is being received by the store it will reject the trade and throw an exception. If the version is same it will override the existing record.
Store should not allow the trade which has less maturity date then today date.
Store should automatically update expire flag if in a store the trade crosses the maturity date.
