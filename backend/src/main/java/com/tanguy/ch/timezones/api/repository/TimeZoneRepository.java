package com.tanguy.ch.timezones.api.repository;

import com.tanguy.ch.timezones.domain.model.TimeZone;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends ListCrudRepository<TimeZone, Long> {
}