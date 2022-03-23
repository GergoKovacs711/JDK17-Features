package com.zeiss.gergo.kovacs.dojo.sealed;

sealed interface Platform permits GamingPlatform {}

sealed class PlayStation extends GamingPlatform permits PS3, PS4, PS5 {}

