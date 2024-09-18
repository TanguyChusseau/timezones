package com.timezones.api.repository;

import com.timezones.domain.model.TimeZone;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends ListCrudRepository<TimeZone, Long> {
}