
package com.example.ugrekrutacja.repository;

import com.example.ugrekrutacja.entity.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComputerRepository
        extends JpaRepository<Computer, Long>,
                JpaSpecificationExecutor<Computer> {
}

