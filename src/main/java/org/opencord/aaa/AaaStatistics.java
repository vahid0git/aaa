/*
 * Copyright 2018-present Open Networking Foundation
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class AaaStatistics {
    private AaaStatistics() {
    }
    private static AaaStatistics aaaStatisticsInstance;
    public static AaaStatistics getInstance() {
        if (aaaStatisticsInstance == null) {
            aaaStatisticsInstance = new AaaStatistics();
        }
        return aaaStatisticsInstance;
    }
    LinkedList<Long> packetRoundTripTimeList = new LinkedList<Long>();
    static Map<Byte, Long> outgoingPacketMap = new HashMap<Byte, Long>();
    private AtomicLong acceptPacketsCounter = new AtomicLong();
    private AtomicLong rejectPacketsCounter = new AtomicLong();
    private AtomicLong challenegePacketsCounter = new AtomicLong();
    private AtomicLong accessPacketsCounter = new AtomicLong();
    private AtomicLong pendingRequestCounter = new AtomicLong();
    private AtomicLong unknownPacketCounter = new AtomicLong();
    private AtomicLong invalidValidatorCounter = new AtomicLong();
    private AtomicLong numberOfDroppedPackets = new AtomicLong();
    private AtomicLong malformedPacketCounter = new AtomicLong();
    private AtomicLong numberOfPacketFromUnknownServer = new AtomicLong();
    private AtomicLong packetRoundtripTimeInMilis = new AtomicLong();
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
    public void increaseAcceptPacketsCounter() {
        acceptPacketsCounter.incrementAndGet();
    }

    public void increaseRejectPacketsCounter() {
        rejectPacketsCounter.incrementAndGet();
    }

    public void increaseChallengePacketsCounter() {
        challenegePacketsCounter.incrementAndGet();
    }

    public void increaseAccessRequestPacketsCounter() {
        accessPacketsCounter.incrementAndGet();
    }

    public void increaseOrDecreasePendingCounter(boolean isIncrement) {
        if (isIncrement) {
            pendingRequestCounter.incrementAndGet();
        } else {
            pendingRequestCounter.decrementAndGet();
        }
    }
    public void increaseUnknownPacketsCounter() {
        unknownPacketCounter.incrementAndGet();
    }

    public void increaseMalformedPacketCounter() {
        malformedPacketCounter.incrementAndGet();
    }

    public void increaseInvalidValidatorCounter() {
        invalidValidatorCounter.incrementAndGet();
    }

    public void incrementNumberOfPacketFromUnknownServer() {
        numberOfPacketFromUnknownServer.incrementAndGet();
    }

    public void countNumberOfDroppedPackets() {
        AtomicLong numberOfDroppedPackets = new AtomicLong();
        numberOfDroppedPackets = invalidValidatorCounter;
        numberOfDroppedPackets.addAndGet(unknownPacketCounter.get());
        numberOfDroppedPackets.addAndGet(malformedPacketCounter.get());
        this.numberOfDroppedPackets = numberOfDroppedPackets;
    }

    public void handleRoundtripTime(long inTimeInMilis, byte inPacketIdentifier) {
        if (outgoingPacketMap.containsKey(inPacketIdentifier)) {
            if (packetRoundTripTimeList.size() > AaaConfig.getPacketsNumberToCountAvgRoundtripTime()) {
                packetRoundTripTimeList.removeFirst();
            }
            packetRoundTripTimeList.add(inTimeInMilis - outgoingPacketMap.get(inPacketIdentifier));
        }
    }

    public void calculatePacketRoundtripTime() {
        long sum = 0;
        long avg = 0;
        Iterator<Long> itr = packetRoundTripTimeList.iterator();
        while (itr.hasNext()) {
            sum = sum + itr.next();
        }
        avg = sum / packetRoundTripTimeList.size();
        packetRoundtripTimeInMilis = new AtomicLong(avg);
    }
}
