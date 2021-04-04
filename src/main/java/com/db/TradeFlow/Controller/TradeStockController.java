package com.db.TradeFlow.Controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.db.TradeFlow.Exception.InvalidTradeException;
import com.db.TradeFlow.Model.TradeStockModel;
import com.db.TradeFlow.Services.TradeStockService;

@RestController
public class TradeStockController {
	
	@Autowired
	TradeStockService tradeStockservice;
	
	
	/**
	 * Validate and save stock details
	 * 
	 * <a href="http://localhost:8080/trade"> Example test URI </a>
	 * 
	 * @param trade
	 * @return
	 * @throws InvalidTradeException
	 */
	@PostMapping("/trade")
	public ResponseEntity<String> validateAndSaveTrade(@RequestBody TradeStockModel trade) throws InvalidTradeException {
		if(tradeStockservice.validatedStock(trade)) {
			tradeStockservice.saveTradeDetails(trade);
		} else {
			throw new InvalidTradeException(trade.getTradeId() + " is invalid.");
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	 * Fetch all trade details present
	 * 
	 * <a href="http://localhost:8080/trade/details?id=T1"> Example test URI </a>
	 * 
	 * @return
	 */
	@GetMapping("/trade/details")
	public List<TradeStockModel> getAllTradeDetails() {
		return tradeStockservice.getAllTradeStocks();
	}
	
	
	/**
	 * Fetch trade details based on passed id
	 * 
	 * <a href="http://localhost:8080/trade/detail?tradeId=T1"> Example test URI </a>
	 * 
	 * @param tradeId
	 * @return
	 */
	@GetMapping("/trade/detail")
	public ResponseEntity<TradeStockModel> getTradeStockById(@RequestParam String tradeId) {
		TradeStockModel tradeStock = tradeStockservice.getTradeStockById(tradeId);
		
		if(tradeStock != null) {
			return ResponseEntity.status(HttpStatus.OK).body(tradeStock);
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
