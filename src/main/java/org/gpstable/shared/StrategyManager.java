package org.gpstable.shared;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class StrategyManager {
	
	private Map<String,DeviceHashStrategy>  strategies=new HashMap<String,DeviceHashStrategy>();
	
	public Map<String, DeviceHashStrategy> getStrategies() {
		return strategies;
	}

	public void setStrategies(Map<String, DeviceHashStrategy> strategies) {
		this.strategies = strategies;
	}

	public Strategy getStrategy(String strategy) {
		return this.strategies.get(strategy);
	}
	
}
