package com.example.care.util.SweetAlert;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SwalMessage {
    private String title;
    private String text;
    private SwalIcon icon;
}
