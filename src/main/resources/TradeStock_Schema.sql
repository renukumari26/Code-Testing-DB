CREATE TABLE TradeStock (
	tradeId VARCHAR(1000) NOT NULL,
	version INT NOT NULL,
	counterPartyId VARCHAR(1000) NOT NULL,
	maturityDate DATE,
	createdDate DATE,
	expiredFlag VARCHAR(2)
);