package org.opencord.aaa;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.onosproject.event.AbstractListenerManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;


@Service
@Component(immediate = true)
public class AaaStatisticsManager
		extends AbstractListenerManager<AuthenticationStatisticsEvent, AuthenticationStatisticsEventListener>
		implements AuthenticationStatisticsService {

	private AuthenticationStatisticsDelegate statsDelegate;

	private final Logger log = getLogger(getClass());
	LinkedList<Long> packetRoundTripTimeList = new LinkedList<Long>();
	static Map<Byte, Long> outgoingPacketMap = new HashMap<Byte, Long>();
	AaaStatistics aaaStatisticsInstance;// = AaaStaistics.getInstance();

	@Activate
	public void activate() {
		statsDelegate = new InternalAuthenticationDelegateForStatistics();
		eventDispatcher.addSink(AuthenticationStatisticsEvent.class, listenerRegistry);
		AuthenticationStatisticsEventPublisher.setDelegate(statsDelegate);
		aaaStatisticsInstance = AaaStatistics.getInstance();
	}

	@Deactivate
	public void deactivate() {
		eventDispatcher.removeSink(AuthenticationStatisticsEvent.class);
	}

	public void increaseAcceptPacketsCounter() {
		aaaStatisticsInstance.acceptPacketsCounter.incrementAndGet();
	}

	public void increaseRejectPacketsCounter() {
		aaaStatisticsInstance.rejectPacketsCounter.incrementAndGet();
	}

	public void increaseChallengePacketsCounter() {
		aaaStatisticsInstance.challenegePacketsCounter.incrementAndGet();
	}

	public void increaseAccessRequestPacketsCounter() {
		aaaStatisticsInstance.accessPacketsCounter.incrementAndGet();
	}

	public void increaseOrDecreasePendingCounter(boolean isIncrement) {
		if (isIncrement) {
			aaaStatisticsInstance.pendingRequestCounter.incrementAndGet();
		} else {
			aaaStatisticsInstance.pendingRequestCounter.decrementAndGet();
		}
	}

	public void increaseUnknownPacketsCounter() {
		aaaStatisticsInstance.unknownPacketCounter.incrementAndGet();
	}

	public void increaseMalformedPacketCounter() {
		aaaStatisticsInstance.malformedPacketCounter.incrementAndGet();
	}

	public void increaseInvalidValidatorCounter() {
		aaaStatisticsInstance.invalidValidatorCounter.incrementAndGet();
	}

	public void incrementNumberOfPacketFromUnknownServer() {
		aaaStatisticsInstance.numberOfPacketFromUnknownServer.incrementAndGet();
	}

	public void countNumberOfDroppedPackets() {
		AtomicLong numberOfDroppedPackets = new AtomicLong();
		numberOfDroppedPackets = aaaStatisticsInstance.invalidValidatorCounter;
		numberOfDroppedPackets.addAndGet(aaaStatisticsInstance.unknownPacketCounter.get());
		numberOfDroppedPackets.addAndGet(aaaStatisticsInstance.malformedPacketCounter.get());
		aaaStatisticsInstance.numberOfDroppedPackets = numberOfDroppedPackets;
	}

	public void handleRoundtripTime(long inTimeInMilis, byte inPacketIdentifier) {
		if (outgoingPacketMap.containsKey(inPacketIdentifier)) {
			if(packetRoundTripTimeList.size() > AaaConfig.getPacketsNumberToCountAvgRoundtripTime()) {
				packetRoundTripTimeList.removeFirst();
			}
			packetRoundTripTimeList.add(inTimeInMilis - outgoingPacketMap.get(inPacketIdentifier));	
		}
		calculatePacketRoundtripTime();
	}

	public void calculatePacketRoundtripTime() {
		long sum = 0;
		long avg = 0;
		Iterator<Long> itr = packetRoundTripTimeList.iterator();
		
		while(itr.hasNext()) {
			sum = sum + itr.next();
		}
		avg = sum / packetRoundTripTimeList.size();
		aaaStatisticsInstance.packetRoundtripTimeInMilis = new AtomicLong(avg);
		log.info("aaaStatisticsInstance.packetRoundtripTimeInMilis::"
				+ aaaStatisticsInstance.packetRoundtripTimeInMilis);
	}

	/**
	 * Delegate allowing the StateMachine to notify us of events.
	 */
	private class InternalAuthenticationDelegateForStatistics implements AuthenticationStatisticsDelegate {

		@Override
		public void notify(AuthenticationStatisticsEvent authenticationStatisticsEvent) {
			log.info("Authentication Statistics event {} for {}", authenticationStatisticsEvent.type(),
					authenticationStatisticsEvent.subject());
			post(authenticationStatisticsEvent);
		}
	}

}
