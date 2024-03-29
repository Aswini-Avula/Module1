package com.hcl.fsc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.fsc.mastertables.Location;
import com.hcl.fsc.mastertables.ProjectType;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {

	public Location getByKey(String key);

	public Location findByValue(String value);
	public Location findTopByOrderByUidDesc();
}
