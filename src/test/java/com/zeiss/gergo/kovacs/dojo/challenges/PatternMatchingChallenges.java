package com.zeiss.gergo.kovacs.dojo.challenges;

import com.zeiss.gergo.kovacs.dojo.BasicFunctionalities;
import com.zeiss.gergo.kovacs.dojo.sealed.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PatternMatchingChallenges implements BasicFunctionalities {

    String findPlatformName(GamingPlatform platform) {
        return switch (platform) {
            case PS3 ps3 -> "ps3";
            case PS4 ps4 -> "ps4";
            case PS5 ps5 -> "ps5";
            case Xbox xbox -> "xbox";
            default -> "pc";
        };
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

    String findPlatformName1(GamingPlatform platform) {
        switch (platform) {
            case PS3 p : return "ps3";
            case PS4 p : return "ps4";
            case PS5 p : return "ps5";
            case Xbox x : return "xbox";
            case Cloud c : return "pc";
            case PC p : return "pc";
            default: return null;
        }
    }

    String findPlatformName2(GamingPlatform platform) {
        return switch (platform) {
            case PS3 ps3 -> "ps3";
            case PS4 ps4 -> "ps4";
            case PS5 ps5 -> "ps5";
            case XboxOneS xbox1s -> "xbox";
            case XboxOneX xbox1x -> "xbox";
            case XboxSeriesS xboxSeriesS -> "xbox";
            case XboxSeriesX xboxSeriesX -> "xbox";
            case PC pc -> "pc";
            case Cloud cloud -> "pc";
            default -> throw new IllegalStateException("Unexpected value: " + platform);
        };
    }

    String findPlatformName3(GamingPlatform platform) {
        final String result = switch (platform) {
            case PlayStation p -> {
                String res = switch (p) {
                    case PS3 ps3 -> "ps3";
                    case PS4 ps4 -> "ps4";
                    case PS5 ps5 -> "ps5";
                    default -> "default";
                };
                yield res;
            }
            case Xbox x -> "xbox";
            case PC pc -> "pc";
            case Cloud c -> "pc";
            case null, default -> "default";
        };
        return result;
    }
}


