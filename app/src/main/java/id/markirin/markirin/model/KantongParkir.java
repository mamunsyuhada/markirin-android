package id.markirin.markirin.model;

/**
 * Created by faldyikhwanfadila on 8/26/17.
 */

public class KantongParkir {
    private String uid;
    private String nama;
    private String deskripsi;
    private Double latitude;
    private Double longitude;
    private Integer kapasitas;
    private Integer terisi;
    private Integer perJam;

    public KantongParkir() {
    }

    public KantongParkir(String uid, String nama, String deskripsi, Double latitude, Double longitude, Integer kapasitas, Integer terisi, Integer perJam) {
        this.uid = uid;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kapasitas = kapasitas;
        this.terisi = terisi;
        this.perJam = perJam;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(Integer kapasitas) {
        this.kapasitas = kapasitas;
    }

    public Integer getTerisi() {
        return terisi;
    }

    public void setTerisi(Integer terisi) {
        this.terisi = terisi;
    }

    public Integer getPerJam() {
        return perJam;
    }

    public void setPerJam(Integer perJam) {
        this.perJam = perJam;
    }

    public int getSisa() {
        return kapasitas - terisi;
    }
}
