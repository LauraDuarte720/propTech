package co.edu.uniquindio.com.proptech.domain.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClosureReportDto {
    private AgentDtoReturn agent;
    private int totalClosures;
    private double totalValue;
    private double totalCommission;
}