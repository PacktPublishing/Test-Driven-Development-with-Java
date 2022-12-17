package examples;

public class BatteryMonitor {
    public void warnWhenBatteryPowerLow() {
        if ( DeviceApi.getBatteryPercentage() < 10 ) {
            System.out.println("Warning - Battery low");
        }
    }
}
