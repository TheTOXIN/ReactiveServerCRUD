package com.toxin.reactive.constant;

public enum EmployeeActivity {

    SLEEP,
    DRIVE,
    PLAY,
    WALK,
    TALK,
    WORK,
    WASH,
    EAT,
    PEE,
    OFF;

    public static EmployeeActivity random() {
        EmployeeActivity[] values = EmployeeActivity.values();
        int index = (int)(Math.random() * values.length);

        return values[index];
    }
}
