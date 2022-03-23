package com.zeiss.gergo.kovacs.dojo.sealed;

public sealed class GamingPlatform implements Platform permits PC, PlayStation, Xbox, Cloud {}
