package Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class addPartDto {
    private String Order_ID;
    private String part_Name;
    private double part_Price;
}
