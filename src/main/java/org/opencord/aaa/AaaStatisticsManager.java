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

	@Activate
	public void activate() {
		statsDelegate = new InternalAuthenticationDelegateForStatistics();
		eventDispatcher.addSink(AuthenticationStatisticsEvent.class, listenerRegistry);
		AuthenticationStatisticsEventPublisher.setDelegate(statsDelegate);
	}

	@Deactivate
	public void deactivate() {
		eventDispatcher.removeSink(AuthenticationStatisticsEvent.class);
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
