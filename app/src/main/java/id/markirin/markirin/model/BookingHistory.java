package id.markirin.markirin.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by faldyikhwanfadila on 8/26/17.
 */

public class BookingHistory {
    private String slotParkir;
    private String kantongParkir;
    private String platNo;
    private Date waktuBooking;
    private Date waktuMasuk;
    private Date waktuSelesai;
    private String userId;
    private Double biaya;
    private String tipePembayaran;

    public BookingHistory() {
    }

    public BookingHistory(String slotParkir, String kantongParkir, String platNo, Date waktuBooking, Date waktuMasuk, Date waktuSelesai, String userId, Double biaya, String tipePembayaran) {
        this.slotParkir = slotParkir;
        this.kantongParkir = kantongParkir;
        this.platNo = platNo;
        this.waktuBooking = waktuBooking;
        this.waktuMasuk = waktuMasuk;
        this.waktuSelesai = waktuSelesai;
        this.userId = userId;
        this.biaya = biaya;
        this.tipePembayaran = tipePembayaran;
    }

    public String getSlotParkir() {
        return slotParkir;
    }

    public void setSlotParkir(String slotParkir) {
        this.slotParkir = slotParkir;
    }

    public String getKantongParkir() {
        return kantongParkir;
    }

    public void setKantongParkir(String kantongParkir) {
        this.kantongParkir = kantongParkir;
    }

    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public Date getWaktuBooking() {
        return waktuBooking;
    }

    public void setWaktuBooking(Date waktuBooking) {
        this.waktuBooking = waktuBooking;
    }

    public Date getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(Date waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public Date getWaktuSelesai() {
        return waktuSelesai;
    }

    public void setWaktuSelesai(Date waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getBiaya() {
        return biaya;
    }

    public void setBiaya(Double biaya) {
        this.biaya = biaya;
    }

    public String getTipePembayaran() {
        return tipePembayaran;
    }

    public void setTipePembayaran(String tipePembayaran) {
        this.tipePembayaran = tipePembayaran;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("slotParkir", slotParkir);
        result.put("kantongParkir", kantongParkir);
        result.put("waktuBooking", waktuBooking);
        result.put("platNo", platNo);
        result.put("waktuMasuk", waktuMasuk);
        result.put("waktuSelesai", waktuSelesai);
        result.put("userId", userId);
        result.put("biaya", biaya);
        result.put("tipePembayaran", tipePembayaran);

        return result;
    }
}
