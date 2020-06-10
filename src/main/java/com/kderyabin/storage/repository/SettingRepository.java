package com.kderyabin.storage.repository;

import com.kderyabin.storage.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<SettingEntity, Long> {
}
