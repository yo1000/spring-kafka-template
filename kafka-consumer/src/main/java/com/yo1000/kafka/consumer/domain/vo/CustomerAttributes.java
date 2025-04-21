package com.yo1000.kafka.consumer.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAttributes {
    private Genders genders;
    private AgeGroups ageGroup;
}
