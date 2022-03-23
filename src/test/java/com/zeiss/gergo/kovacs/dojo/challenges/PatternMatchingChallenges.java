package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import com.zeiss.gergo.kovacs.dojo.sealed.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatternMatchingChallenges implements BasicFunctionalities {

    String findPlatformName(GamingPlatform platform) {
        return "";
    }

    /**
     *  Complete findPlatformName with a switch expression that translates the gaming platform names according to the test
     */
    @Test
    void patternMatchingSwitch() {
        final var ps3 = findPlatformName(new PS3());
        final var ps4 = findPlatformName(new PS4());
        final var ps5 = findPlatformName(new PS5());
        final var xbox1s = findPlatformName(new XboxOneS());
        final var xbox1x = findPlatformName(new XboxOneX());
        final var xboxSeriesS = findPlatformName(new XboxSeriesS());
        final var xboxSeriesX = findPlatformName(new XboxSeriesX());
        final var pc = findPlatformName(new PC());
        final var cloud = findPlatformName(new Cloud());

        Assertions.assertEquals("ps3", ps3);
        Assertions.assertEquals("ps4", ps4);
        Assertions.assertEquals("ps5", ps5);
        Assertions.assertEquals("xbox", xbox1s);
        Assertions.assertEquals("xbox", xbox1x);
        Assertions.assertEquals("xbox", xboxSeriesS);
        Assertions.assertEquals("xbox", xboxSeriesX);
        Assertions.assertEquals("pc", cloud);
        Assertions.assertEquals("pc", pc);
    }
}
