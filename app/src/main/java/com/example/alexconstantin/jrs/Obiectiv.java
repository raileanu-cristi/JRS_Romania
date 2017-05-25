package com.example.alexconstantin.jrs;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Gabriel on 08.04.2017.
 */

public class Obiectiv {

    public Integer Id;
    public Double X;
    public Double Y;
    public String Name;
    public String TypeName;
    public Integer TypeId;

    public com.google.android.gms.maps.model.Marker getMarker() {
        return Marker;
    }

    public void setMarker(com.google.android.gms.maps.model.Marker marker) {
        Marker = marker;
    }

    public Marker Marker;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Double getX() {
        return X;
    }

    public void setX(Double x) {
        X = x;
    }

    public Double getY() {
        return Y;
    }

    public void setY(Double y) {
        Y = y;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public Integer getTypeId() {
        return TypeId;
    }

    public void setTypeId(Integer typeId) {
        TypeId = typeId;
    }

    public Obiectiv() {
    }

}
