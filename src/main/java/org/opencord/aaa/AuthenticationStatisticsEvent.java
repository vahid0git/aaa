package org.opencord.aaa;

import org.onosproject.event.AbstractEvent;
/**
 * Event indicating the Accounting Data of AAA.
 */
public class AuthenticationStatisticsEvent extends
		AbstractEvent<AuthenticationStatisticsEvent.Type, AaaStatistics>{
	/**
     * Accounting data.
     * AuthenticationMetrixEvent event type.
     */
    public enum Type {
        /**
         * signifies that the Authentication Metrix Event stats has been updated.
         */
    	STATS_UPDATE
    }
	/**
     * Creates a new Accounting event.
     *
     * @param type event type
     * @param connectPoint port
     */
    public AuthenticationStatisticsEvent(Type type, AaaStatistics stats) {
        super(type, stats);
    }
}
