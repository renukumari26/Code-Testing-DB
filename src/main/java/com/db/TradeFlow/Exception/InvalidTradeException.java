package com.db.TradeFlow.Exception;

public class InvalidTradeException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String tradeId;

	public InvalidTradeException(String tradeId) {
		super("Invalid Trade : " + tradeId);
		this.tradeId = tradeId;
	}
	
	
	public String getTradeId() {
		return this.tradeId;
	}

}
