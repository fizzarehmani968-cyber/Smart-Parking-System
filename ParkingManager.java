import java.time.LocalDateTime;
import java.util.*;

public class ParkingManager {

    private static ParkingManager instance;

    private final int TOTAL_SLOTS = 100;

    private Set<String> occupiedSlots = new HashSet<>();
    private Map<String, VehicleEntry> activeVehicles = new HashMap<>();

    // Stores all parking records
    private List<ParkingRecord> records = new ArrayList<>();

    private double totalRevenue = 0;

    private ParkingManager() {
    }

    public static synchronized ParkingManager getInstance() {
        if (instance == null) {
            instance = new ParkingManager();
        }
        return instance;
    }

    public List<ParkingRecord> getRecords() {
        return records;
    }

    // ==========================
    // PARK VEHICLE
    // ==========================
    public String parkVehicle(String vehicleNumber, String owner, String type) {

        if (activeVehicles.containsKey(vehicleNumber)) {
            return "Vehicle already parked!";
        }

        String slot = assignSlot();

        if (slot == null) {
            return "Parking Full!";
        }

        VehicleEntry entry = new VehicleEntry(
                vehicleNumber,
                owner,
                type,
                slot,
                System.currentTimeMillis());

        activeVehicles.put(vehicleNumber, entry);
        occupiedSlots.add(slot);

        ParkingRecord record = new ParkingRecord(
                vehicleNumber,
                owner,
                type,
                slot,
                LocalDateTime.now());

        records.add(record);

        return "Vehicle parked at Slot " + slot;
    }

    // ==========================
    // EXIT VEHICLE
    // ==========================
    public String exitVehicle(String vehicleNumber) {

        VehicleEntry entry = activeVehicles.get(vehicleNumber);

        if (entry == null) {
            return "Vehicle not found!";
        }

        long exitTime = System.currentTimeMillis();

        long duration = exitTime - entry.entryTime;

        double hours = Math.ceil(duration / (1000.0 * 60 * 60));

        double fee = calculateFee(hours, entry.type);

        totalRevenue += fee;

        occupiedSlots.remove(entry.slot);
        activeVehicles.remove(vehicleNumber);

        // Update record
        for (ParkingRecord r : records) {

            if (r.getVehicleNumber().equals(vehicleNumber)
                    && r.getStatus().equals("Parked")) {

                r.checkout(LocalDateTime.now(), fee);
                break;
            }
        }

        return "Vehicle Exited\nFee = Rs. " + fee;
    }

    // ==========================
    // SLOT ASSIGNMENT
    // ==========================
    private String assignSlot() {

        for (int i = 1; i <= TOTAL_SLOTS; i++) {

            String slot = "S" + i;

            if (!occupiedSlots.contains(slot)) {
                return slot;
            }

        }

        return null;
    }

    // ==========================
    // FEE
    // ==========================
    private double calculateFee(double hours, String type) {
        return 200;
    }

    public int getTotalSlots() {
        return TOTAL_SLOTS;
    }

    public int getAvailableSlots() {
        return TOTAL_SLOTS - occupiedSlots.size();
    }

    public int getOccupiedSlots() {
        return occupiedSlots.size();
    }

    public double getRevenue() {
        return totalRevenue;
    }

    public boolean isSlotOccupied(int slotNumber) {
        String slot = "S" + slotNumber;
        return occupiedSlots.contains(slot);
    }

    // ==========================
    // INNER CLASS
    // ==========================
    @SuppressWarnings("unused")
    private static class VehicleEntry {

        String vehicleNumber;
        String owner;
        String type;
        String slot;
        long entryTime;

        VehicleEntry(String vehicleNumber,
                     String owner,
                     String type,
                     String slot,
                     long entryTime) {

            this.vehicleNumber = vehicleNumber;
            this.owner = owner;
            this.type = type;
            this.slot = slot;
            this.entryTime = entryTime;
        }
    }

}