package com.ruoyi.system.domain.enums;


public enum ComponentTypeMapping {


    MU("MU", "MU"),
    KOMBI("KOMBI", "KOMBI"),
    GATEWAY("GATEWAY", "Gateway"),
    GW("GATEWAY", "Gateway"),
    ICAS1("GATEWAY", "Gateway"),
    CONBOX("CONBOX", "Conbox/OCU"),
    OCU("OCU", "Conbox/OCU"),
    BCM1("BCM1", "BCM1"),
    BCM2("BCM2", "BCM2"),
    BCM("BCM", "BCM"),
    ZDC("ZDC", "ZDC"),
    Asterix("Asterix", "Asterix");

    private final String code;
    private final String name;

    ComponentTypeMapping(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}