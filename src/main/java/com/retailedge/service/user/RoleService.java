package com.retailedge.service.user;

import com.retailedge.dto.user.RoleDTO;
import com.retailedge.entity.user.Role;
import com.retailedge.repository.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(Integer roleId, RoleDTO roleDetails) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            return null;
        }
        modelMapper.map(roleDetails, role);
        return roleRepository.save(role);
    }

    // Delete a role by ID
    public boolean deleteRole(Integer id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @PostConstruct
    public void addRoles(){
        List<Role> roles = List.of(
                new Role(1,"System", true, Instant.now(), Instant.now()),
                new Role(2,"Super User", true, Instant.now(), Instant.now()),
                new Role(3,"Manager", true, Instant.now(), Instant.now()),
                new Role(4,"Guest", true, Instant.now(), Instant.now())
        );
        try{
            this.roleRepository.saveAll(roles);
        }
        catch (DataIntegrityViolationException ex){
            System.out.println("Role already exists....");
        }

    }

    public Role getSystem(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("System");
        if(optionalRole.isPresent()) return optionalRole.get();
        return null;
    }
    public Role getSuperUser(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("Super User");
        if(optionalRole.isPresent()) return optionalRole.get();
        return null;
    }
    public Role getGuest(){
        Optional<Role> optionalRole = this.roleRepository.findByRoleName("Guest");
        if(optionalRole.isPresent()) return optionalRole.get();
        return null;
    }
}
