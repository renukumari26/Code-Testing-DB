package com.db.TradeFlow;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.db.TradeFlow.Controller.TradeStockController;
import com.db.TradeFlow.Exception.InvalidTradeException;
import com.db.TradeFlow.Model.TradeStockModel;
import com.db.TradeFlow.Repository.TradeStockRepository;

@SpringBootTest
class TradeFlowApplicationTests {
	
	@Autowired
	private TradeStockRepository tradeStockRepository;
	
	@Autowired
	private TradeStockController tradeStockController;

	@Test
	public void tradePresent() {
		TradeStockModel trade = createTradeStockObj("1", 1, LocalDate.of(2020, 5, 20));
		tradeStockRepository.save(trade);
		long tradeSize = tradeStockRepository.count();
		
		Assertions.assertEquals(1, tradeSize);
	}
	
	@Test
	public void tradeNotPresent() {
		long tradeSize = tradeStockRepository.count();
		
		Assertions.assertNotEquals(1, tradeSize);
	}
	
	@Test
	public void tradeStockValid() {
		TradeStockModel trade = createTradeStockObj("1", 1, LocalDate.of(2021, 5, 20));
		tradeStockRepository.save(trade);
		Optional<TradeStockModel> tradeById = tradeStockRepository.findById(trade.getTradeId());
		
		if(tradeById.isPresent()) {
			Assertions.assertEquals("T1", tradeById.get().getTradeId());
		}
	}
	
	@Test
	public void tradeStockInvalidVersion() {
		TradeStockModel tradeOld = createTradeStockObj("3", 3, LocalDate.of(2021, 5, 20));
		tradeStockRepository.save(tradeOld);
		try {
			tradeStockController.validateAndSaveTrade(createTradeStockObj("3", 2, LocalDate.of(2021, 6, 20)));
		} catch (InvalidTradeException e) {
			Assertions.assertEquals("Invalid Trade : T3 is invalid.", e.getMessage());
		}
			
	}
	
	@Test
	public void updateStockDetails() {
		TradeStockModel tradeOld = createTradeStockObj("4", 4, LocalDate.of(2021, 5, 20));
		tradeStockRepository.save(tradeOld);
		try {
			tradeStockController.validateAndSaveTrade(createTradeStockObj("4", 5, LocalDate.of(2021, 5, 25)));
		} catch (InvalidTradeException e) {
			Assertions.assertNotEquals("T3 is invalid.", e.getMessage());
		}
	}
	
	@Test
	public void stockMaturityValid() {
		TradeStockModel tradeOld = createTradeStockObj("4", 4, LocalDate.now());
		Assertions.assertEquals(true, LocalDate.now().isEqual(tradeOld.getMaturityDate()));
	}
	
	@Test
	public void stockMaturityInvalid() {
		TradeStockModel tradeOld = createTradeStockObj("4", 4, LocalDate.now());
		tradeStockRepository.save(tradeOld);
	}
	
	private TradeStockModel createTradeStockObj(String tradeId, int version, LocalDate maturityDate) {
		TradeStockModel tradeStock  = new TradeStockModel();
		
		tradeStock.setTradeId("T" + tradeId);
		tradeStock.setVersion(version);
		tradeStock.setCounterPartyId("CP-"+version);
		tradeStock.setBookId("B"+version);
		tradeStock.setMaturityDate(maturityDate);
		tradeStock.setExpiredFlag("N");
		
		return tradeStock;
	}

}
