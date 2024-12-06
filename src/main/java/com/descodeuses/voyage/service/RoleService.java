package com.descodeuses.voyage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.descodeuses.voyage.model.Role;

@Service
public interface RoleService {
    public List<Role> findAll();
    public Role save(Role role);
}
