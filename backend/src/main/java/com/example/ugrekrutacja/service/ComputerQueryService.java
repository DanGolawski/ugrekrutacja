
package com.example.ugrekrutacja.service;

import com.example.ugrekrutacja.entity.Computer;
import com.example.ugrekrutacja.repository.ComputerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class ComputerQueryService {

    private final ComputerRepository computerRepository;

    public ComputerQueryService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public Page<Computer> search(
            String name,
            LocalDate accountingDate,
            String sortBy,
            String direction,
            int page,
            int size
    ) {
        validateParameters(sortBy, direction, page, size);

        Specification<Computer> specification =
                (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            String pattern = "%"
                    + name.trim().toLowerCase(Locale.ROOT)
                    + "%";

            specification = specification.and(
                    (root, query, cb) ->
                            cb.like(
                                    cb.lower(root.get("name")),
                                    pattern
                            )
            );
        }

        if (accountingDate != null) {
            specification = specification.and(
                    (root, query, cb) ->
                            cb.equal(
                                    root.get("accountingDate"),
                                    accountingDate
                            )
            );
        }

        Pageable pageable = PageRequest.of(page, size, buildSort(sortBy, direction));
        return computerRepository.findAll(specification, pageable);
    }

    private Sort buildSort(String sortBy, String direction) {
        String sortProperty = "name".equals(sortBy)
                ? "name"
                : "accountingDate";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(sortDirection, sortProperty)
                .and(Sort.by(Sort.Direction.ASC, "id"));
    }

    private void validateParameters(
            String sortBy,
            String direction,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page must be zero or greater"
            );
        }
        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Size must be between 1 and 100"
            );
        }
        if (sortBy != null
                && !sortBy.equals("name")
                && !sortBy.equals("accountingDate")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sortBy value. Allowed values: name, accountingDate"
            );
        }
        if (direction != null
                && !direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid direction value. Allowed values: asc, desc"
            );
        }
    }
}
