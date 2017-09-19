package id.markirin.markirin.model;

/**
 * Created by faldyikhwanfadila on 8/26/17.
 */

public class User {
    private String uid;
    private String name;
    private String email;
    private String bookingSlotId;
    private String bookingParkiranId;
    private String bookingKey;
    private Double saldo;

    public User() {
    }

    public User(String uid, String name, String email, String bookingSlotId, String bookingParkiranId, String bookingKey, Double saldo) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.bookingSlotId = bookingSlotId;
        this.bookingParkiranId = bookingParkiranId;
        this.bookingKey = bookingKey;
        this.saldo = saldo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBookingSlotId() {
        return bookingSlotId;
    }

    public void setBookingSlotId(String bookingSlotId) {
        this.bookingSlotId = bookingSlotId;
    }

    public String getBookingParkiranId() {
        return bookingParkiranId;
    }

    public void setBookingParkiranId(String bookingParkiranId) {
        this.bookingParkiranId = bookingParkiranId;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getBookingKey() {
        return bookingKey;
    }

    public void setBookingKey(String bookingKey) {
        this.bookingKey = bookingKey;
    }
}
