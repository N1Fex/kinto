package ru.n1fex.kinto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActiveSessionsDto {

    private String currentSid;
    private List<SessionDto> sessions;

}
