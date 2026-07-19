package com.ticket.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderSubmitDTO {
    private Long matchId;
    private List<Long> seatIds;
    private String contactName;
    private String contactPhone;
    private String note;
}