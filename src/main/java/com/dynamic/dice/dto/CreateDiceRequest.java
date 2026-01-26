package com.dynamic.dice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CreateDiceRequest {
    private String diceName;
    private List<DiceFaceRequest> faces;

    public String getDiceName() {
        return diceName;
    }

    public void setDiceName(String diceName) {
        this.diceName = diceName;
    }

    public List<DiceFaceRequest> getFaces() {
        return faces;
    }

    public void setFaces(List<DiceFaceRequest> faces) {
        this.faces = faces;
    }
}

