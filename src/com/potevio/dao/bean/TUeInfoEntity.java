package com.potevio.dao.bean;

import javax.persistence.*;

/**
 * Created by oeste on 2017/5/8.
 */
@Entity
@Table(name = "tUeInfo", schema = "sagweb")
public class TUeInfoEntity {
    private String ueImsi;
    private String ueIpAddr;
    private Integer ueStatus;
    private String ueLocation;
    private String ueType;
    private Double latitudeXy;
    private Double longitudeXy;
    private Integer eNodeBId;
    private Integer cellId;
    private Integer rsrp;
    private Integer rsrq;
    private Integer snr;
    private String terminalAddr;

    @Id
    @Column(name = "UE_IMSI")
    public String getUeImsi() {
        return ueImsi;
    }

    public void setUeImsi(String ueImsi) {
        this.ueImsi = ueImsi;
    }

    @Basic
    @Column(name = "UeIpAddr")
    public String getUeIpAddr() {
        return ueIpAddr;
    }

    public void setUeIpAddr(String ueIpAddr) {
        this.ueIpAddr = ueIpAddr;
    }

    @Basic
    @Column(name = "UE_STATUS")
    public Integer getUeStatus() {
        return ueStatus;
    }

    public void setUeStatus(Integer ueStatus) {
        this.ueStatus = ueStatus;
    }

    @Basic
    @Column(name = "UE_LOCATION")
    public String getUeLocation() {
        return ueLocation;
    }

    public void setUeLocation(String ueLocation) {
        this.ueLocation = ueLocation;
    }

    @Basic
    @Column(name = "UE_TYPE")
    public String getUeType() {
        return ueType;
    }

    public void setUeType(String ueType) {
        this.ueType = ueType;
    }

    @Basic
    @Column(name = "LATITUDE_XY")
    public Double getLatitudeXy() {
        return latitudeXy;
    }

    public void setLatitudeXy(Double latitudeXy) {
        this.latitudeXy = latitudeXy;
    }

    @Basic
    @Column(name = "LONGITUDE_XY")
    public Double getLongitudeXy() {
        return longitudeXy;
    }

    public void setLongitudeXy(Double longitudeXy) {
        this.longitudeXy = longitudeXy;
    }

    @Basic
    @Column(name = "E_NODE_B_ID")
    public Integer geteNodeBId() {
        return eNodeBId;
    }

    public void seteNodeBId(Integer eNodeBId) {
        this.eNodeBId = eNodeBId;
    }

    @Basic
    @Column(name = "CELL_ID")
    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    @Basic
    @Column(name = "RSRP")
    public Integer getRsrp() {
        return rsrp;
    }

    public void setRsrp(Integer rsrp) {
        this.rsrp = rsrp;
    }

    @Basic
    @Column(name = "RSRQ")
    public Integer getRsrq() {
        return rsrq;
    }

    public void setRsrq(Integer rsrq) {
        this.rsrq = rsrq;
    }

    @Basic
    @Column(name = "SNR")
    public Integer getSnr() {
        return snr;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    @Basic
    @Column(name = "TERMINAL_ADDR")
    public String getTerminalAddr() {
        return terminalAddr;
    }

    public void setTerminalAddr(String terminalAddr) {
        this.terminalAddr = terminalAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TUeInfoEntity that = (TUeInfoEntity) o;

        if (ueImsi != null ? !ueImsi.equals(that.ueImsi) : that.ueImsi != null) return false;
        if (ueIpAddr != null ? !ueIpAddr.equals(that.ueIpAddr) : that.ueIpAddr != null) return false;
        if (ueStatus != null ? !ueStatus.equals(that.ueStatus) : that.ueStatus != null) return false;
        if (ueLocation != null ? !ueLocation.equals(that.ueLocation) : that.ueLocation != null) return false;
        if (ueType != null ? !ueType.equals(that.ueType) : that.ueType != null) return false;
        if (latitudeXy != null ? !latitudeXy.equals(that.latitudeXy) : that.latitudeXy != null) return false;
        if (longitudeXy != null ? !longitudeXy.equals(that.longitudeXy) : that.longitudeXy != null) return false;
        if (eNodeBId != null ? !eNodeBId.equals(that.eNodeBId) : that.eNodeBId != null) return false;
        if (cellId != null ? !cellId.equals(that.cellId) : that.cellId != null) return false;
        if (rsrp != null ? !rsrp.equals(that.rsrp) : that.rsrp != null) return false;
        if (rsrq != null ? !rsrq.equals(that.rsrq) : that.rsrq != null) return false;
        if (snr != null ? !snr.equals(that.snr) : that.snr != null) return false;
        if (terminalAddr != null ? !terminalAddr.equals(that.terminalAddr) : that.terminalAddr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ueImsi != null ? ueImsi.hashCode() : 0;
        result = 31 * result + (ueIpAddr != null ? ueIpAddr.hashCode() : 0);
        result = 31 * result + (ueStatus != null ? ueStatus.hashCode() : 0);
        result = 31 * result + (ueLocation != null ? ueLocation.hashCode() : 0);
        result = 31 * result + (ueType != null ? ueType.hashCode() : 0);
        result = 31 * result + (latitudeXy != null ? latitudeXy.hashCode() : 0);
        result = 31 * result + (longitudeXy != null ? longitudeXy.hashCode() : 0);
        result = 31 * result + (eNodeBId != null ? eNodeBId.hashCode() : 0);
        result = 31 * result + (cellId != null ? cellId.hashCode() : 0);
        result = 31 * result + (rsrp != null ? rsrp.hashCode() : 0);
        result = 31 * result + (rsrq != null ? rsrq.hashCode() : 0);
        result = 31 * result + (snr != null ? snr.hashCode() : 0);
        result = 31 * result + (terminalAddr != null ? terminalAddr.hashCode() : 0);
        return result;
    }
}
