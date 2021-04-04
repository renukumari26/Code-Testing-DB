package com.db.TradeFlow.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.TradeFlow.Model.TradeStockModel;
import com.db.TradeFlow.Repository.TradeStockRepository;

@Service
public class TradeStockService  {
	
	private static final Logger log = LoggerFactory.getLogger(TradeStockService.class);
	
	@Autowired
	TradeStockRepository tradeStockRepository;
	
	/**
	 * Method to validate stock.
	 * @param tradeStock
	 * @return
	 */
	public boolean validatedStock(TradeStockModel tradeStock) {
		log.info("TradeStockService: validating");
		
		// If maturity date valid check if stock present or not.
		if(validateMaturityDate(tradeStock)) {
			log.info("TradeStockService : Maturity date is valid");
			Optional<TradeStockModel> existingTrade = tradeStockRepository.findById(tradeStock.getTradeId());
			if(existingTrade.isPresent()) {
				// validate stock version
				if(validateStockVersion(tradeStock, existingTrade.get())) {
					return true;
				}
			} else {
				// update the flag
				updateExpiryDate();
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Method to validate maturity date
	 * @param tradeStock
	 * @return
	 */
	public boolean validateMaturityDate(TradeStockModel tradeStock) {
		return tradeStock.getMaturityDate().isBefore(LocalDate.now()) ? false : true;
	}
	
	/**
	 * Method to validate stock version
	 * @param newTrade
	 * @param oldTrade
	 * @return
	 */
	public boolean validateStockVersion(TradeStockModel newTrade, TradeStockModel oldTrade) {
		if(newTrade.getVersion() >= oldTrade.getVersion()) {
			return true;
		}
		log.info("TradeStockService : Stock version is invalid");
		return false;
	}
	
	/**
	 * Method to store stock details in repository
	 * @param tradeStock
	 */
	public void saveTradeDetails(TradeStockModel tradeStock) {
		tradeStock.setCreatedDate(LocalDate.now());
		tradeStockRepository.save(tradeStock);
	}
	
	
	/**
	 * Method to get all trade details
	 * @return
	 */
	public List<TradeStockModel> getAllTradeStocks() {
		return tradeStockRepository.findAll();
	}
	
	
	/** 
	 * Method to get particular trade details.
	 * @param id
	 * @return
	 */
	public TradeStockModel getTradeStockById(String id) {
		Optional<TradeStockModel> stockDetail = tradeStockRepository.findById(id);
		if(stockDetail.isPresent() ) {
			return stockDetail.get();
		}

		return null;
	}
	
	/**
	 * Method to update expiry date
	 * 
	 */
	public void updateExpiryDate() {
		tradeStockRepository.findAll().stream().forEach(trade -> {
			if(trade.getMaturityDate().isEqual(LocalDate.now())) {
				trade.setExpiredFlag("Y");
				tradeStockRepository.save(trade);
			}
		});
	}

}
