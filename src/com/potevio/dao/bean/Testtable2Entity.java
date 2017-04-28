package com.potevio.dao.bean;

import javax.persistence.*;

/**
 * Created by oeste on 2017/4/26.
 */
@Entity
@Table(name = "testtable2", schema = "hibernate_test")
public class Testtable2Entity {
    private int param1;
    private String param2;
    private String param3;
    private String param5;

    @Id
    @Column(name = "param1")
    public int getParam1() {
        return param1;
    }

    public void setParam1(int param1) {
        this.param1 = param1;
    }

    @Basic
    @Column(name = "param2")
    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    @Basic
    @Column(name = "param3")
    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    @Basic
    @Column(name = "param5")
    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Testtable2Entity that = (Testtable2Entity) o;

        if (param1 != that.param1) return false;
        if (param2 != null ? !param2.equals(that.param2) : that.param2 != null) return false;
        if (param3 != null ? !param3.equals(that.param3) : that.param3 != null) return false;
        if (param5 != null ? !param5.equals(that.param5) : that.param5 != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = param1;
        result = 31 * result + (param2 != null ? param2.hashCode() : 0);
        result = 31 * result + (param3 != null ? param3.hashCode() : 0);
        result = 31 * result + (param5 != null ? param5.hashCode() : 0);
        return result;
    }
}
