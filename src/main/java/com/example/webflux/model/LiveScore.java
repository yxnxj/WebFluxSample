package com.example.webflux.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LiveScore {
    private String homeTeam;
    private String awayTeam;
    private int homeScore;
    private int awayScore;
    private Date updateDate;

}
