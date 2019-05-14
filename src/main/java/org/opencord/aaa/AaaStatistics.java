package org.opencord.aaa;

import java.util.concurrent.atomic.AtomicLong;

public class AaaStatistics {

	private AaaStatistics() {
		
	}
	
	private static AaaStatistics aaaStatisticsInstance;
	
	public static AaaStatistics getInstance() {
		if(aaaStatisticsInstance == null) 
			aaaStatisticsInstance = new AaaStatistics();
		return aaaStatisticsInstance;
	}
	
	AtomicLong acceptPacketsCounter = new AtomicLong(); 
	AtomicLong rejectPacketsCounter = new AtomicLong(); 
	AtomicLong challenegePacketsCounter = new AtomicLong(); 
	AtomicLong accessPacketsCounter = new AtomicLong(); 
	AtomicLong pendingRequestCounter = new AtomicLong();
	AtomicLong unknownPacketCounter = new AtomicLong();
	AtomicLong invalidValidatorCounter = new AtomicLong();
	AtomicLong numberOfDroppedPackets = new AtomicLong();
	AtomicLong malformedPacketCounter = new AtomicLong();
	AtomicLong numberOfPacketFromUnknownServer = new AtomicLong();
	AtomicLong packetRoundtripTimeInMilis = new AtomicLong();
	
	public long getNumberOfPacketFromUnknownServer() {
		return numberOfPacketFromUnknownServer.get();
	}
	public long getPacketRoundtripTimeInMilis() {
		return packetRoundtripTimeInMilis.get();
	}
	public long getMalformedPacketCounter() {
		return malformedPacketCounter.get();
	}
	public long getNumberOfDroppedPackets() {
		return numberOfDroppedPackets.get();
	}
	public long getInvalidValidatorCounter() {
		return invalidValidatorCounter.get();
	}
	public long getAcceptPacketsCounter() {
		return acceptPacketsCounter.get();
	}
	public long getRejectPacketsCounter() {
		return rejectPacketsCounter.get();
	}
	public long getChallenegePacketsCounter() {
		return challenegePacketsCounter.get();
	}
	public long getAccessPacketsCounter() {
		return accessPacketsCounter.get();
	}
	public long getPendingRequestCounter() {
		return pendingRequestCounter.get();
	}
	public long getUnknowPacketCounter() {
		return unknownPacketCounter.get();
	}
}
