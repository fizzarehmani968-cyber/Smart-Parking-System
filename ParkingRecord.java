import java.time.LocalDateTime;

public class ParkingRecord {

    private String vehicleNumber;
    private String ownerName;
    private String vehicleType;
    private String slot;

    private LocalDateTime entryTime;
    private LocalDateTime exitTime;

    private double fee;

    private String status;

    public ParkingRecord(String vehicleNumber,
                         String ownerName,
                         String vehicleType,
                         String slot,
                         LocalDateTime entryTime) {

        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.vehicleType = vehicleType;
        this.slot = slot;
        this.entryTime = entryTime;
        this.status = "Parked";
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getSlot() {
        return slot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getFee() {
        return fee;
    }

    public String getStatus() {
        return status;
    }

    public void checkout(LocalDateTime exitTime, double fee) {
        this.exitTime = exitTime;
        this.fee = fee;
        this.status = "Exited";
    }
}