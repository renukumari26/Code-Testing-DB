package com.db.TradeFlow.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.db.TradeFlow.Model.TradeStockModel;

@Repository
public interface TradeStockRepository extends JpaRepository<TradeStockModel, String> {

}
