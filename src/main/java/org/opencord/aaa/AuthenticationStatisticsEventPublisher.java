/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencord.aaa;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public class AuthenticationStatisticsEventPublisher implements Runnable  {
	
    private final Logger log = getLogger(getClass());
	
	public static AuthenticationStatisticsEventPublisher instance;
	
	public static AuthenticationStatisticsEventPublisher getInstance() {
		
		if(instance == null) 
			instance = new AuthenticationStatisticsEventPublisher();
		return instance;
	}
	private static AuthenticationStatisticsDelegate delegate;
	AuthenticationStatisticsEvent authenticationStatisticsEvent;

	static void setDelegate(AuthenticationStatisticsDelegate delegate) {
		AuthenticationStatisticsEventPublisher.delegate = delegate;
	}

	public void run() {
		AaaStatistics instance = AaaStatistics.getInstance();
		log.info("Notifying AuthenticationStatisticsEvent");
//		instance.calculatePacketRoundtripTime();
		log.debug("posting AcceptPacketsCounter:::"+instance.getAcceptPacketsCounter());
		log.debug("posting AccessPacketsCounter:::"+instance.getAccessPacketsCounter());
		log.debug("posting ChallenegePacketsCounter:::"+instance.getChallenegePacketsCounter());
		log.debug("posting InvalidValidatorCounter:::"+instance.getInvalidValidatorCounter());
		log.debug("posting MalformedPacketCounter:::"+instance.getMalformedPacketCounter());
		log.debug("posting NumberOfDroppedPackets:::"+instance.getNumberOfDroppedPackets());
		log.debug("posting NumberOfPacketFromUnknownServer:::"+instance.getNumberOfPacketFromUnknownServer());
		log.debug("posting PacketRoundtripTimeInMilis:::"+instance.getPacketRoundtripTimeInMilis());
		log.debug("posting PendingRequestCounter:::"+instance.getPendingRequestCounter());
		log.debug("posting RejectPacketsCounter:::"+instance.getRejectPacketsCounter());
		log.debug("posting UnknowPacketCounter:::"+instance.getUnknowPacketCounter());
	    delegate.notify(new AuthenticationStatisticsEvent(
	    			AuthenticationStatisticsEvent.Type.STATS_UPDATE, instance));
			   
		}
}

